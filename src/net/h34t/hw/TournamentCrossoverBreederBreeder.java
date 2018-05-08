package net.h34t.hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A breeder that uses tournament style parentage selection and crossover breeding
 */
public class TournamentCrossoverBreederBreeder implements Breeder {

    private final int tests;
    private final Random random;
    private final List<ScoredProgram> parents;

    /**
     * @param random The randomizer to use
     * @param tests  How many candidates to compare for tournament parent selection
     */
    public TournamentCrossoverBreederBreeder(Random random, int tests) {
        this.random = random;
        this.tests = tests;
        this.parents = new ArrayList<>();
    }

    /**
     * Selects the fittest one of N (tests) randomly selected candidates. The bigger n is, the faster it converges.
     *
     * @param pop all candidates
     * @return a reasonably fit candidate
     */
    private ScoredProgram selectParent(List<ScoredProgram> pop) {
        ScoredProgram best = pop.get(0);
        for (int i = 0; i < tests; i++) {
            ScoredProgram candidate = pop.get(random.nextInt(pop.size()));
            if (candidate.score < best.score)
                best = candidate;
        }

        return best;
    }

    /**
     * A simple crossover breeding strategy (https://en.wikipedia.org/wiki/Crossover_(genetic_algorithm)) with a single point.
     *
     * @return a new dna
     */
    @Override
    public String breed() {
        ScoredProgram father = selectParent(parents);
        ScoredProgram mother = selectParent(parents);

        int cut = random.nextInt(Math.min(father.program.length(), mother.program.length()));

        return father.program.substring(0, cut) + mother.program.substring(cut);
    }

    /**
     * @param pop the population
     */
    @Override
    public void setParentPopulation(List<ScoredProgram> pop) {
        parents.addAll(pop);
    }
}
