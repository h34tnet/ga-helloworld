package net.h34t.hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneticMain {

    private static final int POPSIZE = 256;
    private static final boolean ELITISM = true;

    public static void main(String[] args) {
        final String target = "Hello, World!";

        final char[] INSTRUCTIONS = "<>+-.[]".toCharArray();

        // prepare the initial population with random working programs until the pool is full
        final List<ScoredProgram> population = Stream.generate(() ->
                Evaluator.score(Mutator.generateRandomProgram(ThreadLocalRandom.current(), 32, INSTRUCTIONS), target))
                .filter(Objects::nonNull)
                .limit(POPSIZE)
                .collect(Collectors.toList());

        // get the initial champion
        ScoredProgram champion = population.stream()
                .min(ScoredProgram.CMP)
                .orElseThrow(() -> new RuntimeException("This wont happen."));

        final List<ScoredProgram> childPopulation = new ArrayList<>();

        int gen = 0;

        // repeat until we get an individual that outputs the target string
        while (!champion.output.equals(target)) {

            // generate children and add them to the next generation until it reaches POPSIZE
            childPopulation.addAll(Stream.generate(() -> {
                String cdna = Breeder.breed(ThreadLocalRandom.current(), population, (int) Math.sqrt(POPSIZE));
                cdna = Mutator.mutate(ThreadLocalRandom.current(), cdna, 0.025d, INSTRUCTIONS);
                return Evaluator.score(cdna, target);
            })
                    // remove invalid programs
                    .filter(Objects::nonNull)
                    // until we have POPSIZE individuals
                    .limit(POPSIZE)
                    .collect(Collectors.toList()));

            // elitism: we always re-introduce the best candidate yet into the gene pool
            if (ELITISM && champion != null)
                childPopulation.add(champion);

            // find the best child of the new generation
            Optional<ScoredProgram> best = childPopulation.parallelStream()
                    .min(ScoredProgram.CMP);

            // if a candidate who produces the correct output is found, stop
            if (best.isPresent() && best.get().score < champion.score) {
                champion = best.get();
                System.out.printf("%8d: %s%n", gen, champion);
            }

            // make the child generation the new parent generation and repeat
            population.clear();
            population.addAll(childPopulation);
            childPopulation.clear();

            gen++;
        }

        System.out.println("Solution found in generation " + gen);
        System.out.println(champion);
    }
}
