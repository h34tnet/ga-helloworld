package net.h34t.hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticMain {

    private static final int POPSIZE = 1024;

    public static void main(String[] args) {
        final String target = "Hello, World!";

        List<ScoredProgram> population = new ArrayList<>();

        while (population.size() < POPSIZE) {
            ScoredProgram candidate = HWGen.score(HWGen.generateRandomProgram(ThreadLocalRandom.current(), 32), target);
            if (candidate != null)
                population.add(candidate);
        }

        ScoredProgram champion = null;

        int gen = 0;
        while (true) {
            gen++;

            List<ScoredProgram> nextGeneration = new ArrayList<>();

            if (champion != null)
                nextGeneration.add(champion);

            while (nextGeneration.size() < POPSIZE) {
                String matChild = HWGen.mate(ThreadLocalRandom.current(), population);
                String mutChild = HWGen.mutate(ThreadLocalRandom.current(), matChild, 0.025d);

                if (mutChild.length() <= 0)
                    continue;

                ScoredProgram child = HWGen.score(mutChild, target);

                if (child != null)
                    nextGeneration.add(child);
            }

            population = nextGeneration;

            Optional<ScoredProgram> best = population.parallelStream()
                    .min(ScoredProgram.CMP);

            if (best.isPresent() && champion == null || (best.get().score < champion.score)) {
                champion = best.get();
                System.out.printf("%8d: %s%n", gen, champion);

                if (false) break;

//                if (champion.output.equals(target)) {
//                    break;
//                }
            }
        }


        System.out.println(champion);

        if (champion.program.contains("$")) {
            System.out.println(champion.program.substring(0, champion.program.indexOf('$')));

        } else {
            System.out.println(champion.program);
        }
    }
}
