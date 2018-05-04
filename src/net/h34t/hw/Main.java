package net.h34t.hw;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        final String target = "Hello, World!";

        ScoredProgram champion = HWGen.score("", target);

        ScoredProgram parent = champion;

        System.out.println("initial: " + champion.toString());

        int noimp = 0;

        int gen = 0;
        while (noimp < 1_000) {
            gen++;

            final String cprogram = parent.program;
            ScoredProgram child = IntStream.range(0, 10_000)
                    .parallel()
                    .mapToObj(i -> HWGen.mutate(ThreadLocalRandom.current(), cprogram))
                    .map(c -> HWGen.score(c, target))
                    .filter(Objects::nonNull)
                    .min(ScoredProgram.CMP)
                    .orElseThrow(() -> new RuntimeException("All programs invalid, sorry"));

            // we allow some regression to get over local minima
            if (child.score < parent.score * 1.1d) {
                parent = child;
            }

            // still, if there's not a new champion in N generations, give up
            if (child.score < champion.score) {
                champion = child;
                System.out.printf("%8d: %s%n", gen, champion);
                noimp = 0;
            } else
                noimp++;
        }

        System.out.println(champion.program);
    }
}
