package net.h34t.hw;

import java.util.Random;
import java.util.stream.Stream;

public class HillClimberMain {

    public static void main(String[] args) {
        // what we want the program to print
        final String target = "Hello, World!";
        Random r = new Random();

        // "" is a valid program, so we know this works even though the score method might return null
        ScoredProgram candidate = Evaluator.score("", target);

        // prevents icky null warnings
        if (candidate == null) return;

        int gen = 0;

        // loop until we get a program that outputs the desired output
        while (!candidate.output.equals(target)) {

            final String parentProgram = candidate.program;
            final double parentScore = candidate.score;

            // generate children until one is fitter than the parent
            candidate = Stream.generate(() -> Mutator.mutate(r, parentProgram, 0.025d, VM.INSTRUCTIONS))
                    .map(c -> Evaluator.score(c, target))
                    .filter(c -> c != null && c.score < parentScore)
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("This must not happen."));

            System.out.printf("%8d: %s%n", gen, candidate);
            gen++;
        }

        System.out.println("Solution found in generation " + gen);
        System.out.println(candidate);

        try {
            System.out.println(new VM().execute(candidate.program, () -> (byte) 0, 1_024));
        } catch (Exception ignored) {
        }
    }
}
