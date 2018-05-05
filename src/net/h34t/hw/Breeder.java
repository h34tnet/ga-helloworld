package net.h34t.hw;

import java.util.List;
import java.util.Random;

public class Breeder {

    private static ScoredProgram bestOf(List<ScoredProgram> pop, int count, Random r) {
        ScoredProgram best = pop.get(0);
        for (int i = 0; i < count; i++) {
            ScoredProgram candidate = pop.get(r.nextInt(pop.size()));
            if (candidate.score < best.score)
                best = candidate;
        }

        return best;
    }

    public static String breed(Random r, List<ScoredProgram> pop, int bestOf) {
        ScoredProgram father = bestOf(pop, bestOf, r);
        ScoredProgram mother = bestOf(pop, bestOf, r);

        int cut = r.nextInt(Math.min(father.program.length(), mother.program.length()));

        return father.program.substring(0, cut) + mother.program.substring(cut);
    }
}
