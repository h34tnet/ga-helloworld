package net.h34t.hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TruncationBreeder implements Breeder {

    private final Random random;
    private final double parentageSelectionCutoff;
    private final List<ScoredProgram> parentPopulationPool;

    /**
     * @param random The randomizer to use
     */
    public TruncationBreeder(Random random, double parentageSelectionCutoff) {
        this.random = random;
        this.parentageSelectionCutoff = parentageSelectionCutoff;
        this.parentPopulationPool = new ArrayList<>();

    }

    private List<ScoredProgram> selectParents(List<ScoredProgram> pop) {
        return pop.stream()
                .sorted(ScoredProgram.CMP)
                .limit((int) (pop.size() * parentageSelectionCutoff))
                .collect(Collectors.toList());
    }

    /**
     * A simple crossover breeding strategy (https://en.wikipedia.org/wiki/Crossover_(genetic_algorithm)) with a single point.
     *
     * @return a new dna
     */
    @Override
    public String breed() {
        ScoredProgram father = parentPopulationPool.get(random.nextInt(parentPopulationPool.size()));
        ScoredProgram mother = parentPopulationPool.get(random.nextInt(parentPopulationPool.size()));

        int cut = random.nextInt(Math.min(father.program.length(), mother.program.length()));

        return father.program.substring(0, cut) + mother.program.substring(cut);
    }

    /**
     * Preselects the pool of potential parents for performance reasons
     *
     * @param pop the population
     */
    @Override
    public void setParentPopulation(List<ScoredProgram> pop) {
        this.parentPopulationPool.clear();
        this.parentPopulationPool.addAll(selectParents(pop));
    }
}
