package net.h34t.hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HWGen {

    public static final long INST_LIMIT = 1_000;

    public static String generateRandomProgram(Random r, int length) {
        return IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(VM.INSTRUCTIONS[r.nextInt(VM.INSTRUCTIONS.length)]))
                .collect(Collectors.joining());
    }

    public static String mutateOnce(Random r, String dna) {
        if (dna.length() == 0) {
            dna = "" + VM.INSTRUCTIONS[r.nextInt(VM.INSTRUCTIONS.length)];

        } else if (r.nextBoolean()) {
            // remove random op
            int idx = r.nextInt(dna.length());
            dna = dna.substring(0, idx) + dna.substring(idx + 1);
        } else {
            // add random op
            int idx = r.nextInt(dna.length());
            dna = dna.substring(0, idx) + VM.INSTRUCTIONS[r.nextInt(VM.INSTRUCTIONS.length)] + dna.substring(idx);
        }

        return dna;
    }

    public static String mutate(Random r, String dna) {
        for (int i = 0; i < Math.max(1, r.nextGaussian()); i++)
            dna = mutateOnce(r, dna);

        return dna;
    }

    public static ScoredProgram score(String program, String target) {
        try {
            String output = new VM().execute(program, INST_LIMIT);
            double score = cmp(output, target);

            score = score + (1d - 1d / (1d + program.length()));

            return new ScoredProgram(score, program, output);

        } catch (VM.GuruMeditationException e) {
            return null;
        }
    }

    static ScoredProgram bestOf(List<ScoredProgram> pop, int count, Random r) {
        ScoredProgram best = pop.get(0);
        for (int i = 0; i < count; i++) {
            ScoredProgram candidate = pop.get(r.nextInt(pop.size()));
            if (candidate.score < best.score)
                best = candidate;
        }

        return best;
    }

    public static String mate(Random r, List<ScoredProgram> pop) {
        ScoredProgram father = bestOf(pop, 3, r);
        ScoredProgram mother = bestOf(pop, 3, r);

        int cut = r.nextInt(Math.min(father.program.length(), mother.program.length()));

        return father.program.substring(0, cut) + mother.program.substring(cut);
    }

    public static String mutate(Random r, String program, double rate) {
        List<Character> prog = new ArrayList<>(program.length());
        for (char c : program.toCharArray()) {
            if (r.nextDouble() < rate) {
                switch (r.nextInt(3)) {
                    case 0:
                        prog.add(selectRandom(r, VM.INSTRUCTIONS));
                        break;
                    case 1:
                        prog.add(c);
                        prog.add(selectRandom(r, VM.INSTRUCTIONS));
                        break;
                    case 2:
                        break;

                }
            } else {
                prog.add(c);
            }
        }

        return prog.stream().map(String::valueOf).collect(Collectors.joining());
    }

    public static double cmp(String s1, String s2) {
        double diff = 0;
        for (int i = 0, ii = Math.min(s1.length(), s2.length()); i < ii; i++) {
            int d = s1.charAt(i) - s2.charAt(i);
            diff += d * d;
        }

        double ld = Math.abs(s1.length() - s2.length()) * 128 * 128;
        return diff + ld;
    }

    private static char selectRandom(Random r, char[] chars) {
        return chars[r.nextInt(chars.length)];
    }

    public static String cutProgram(String program) {
        return program.contains("$") ? program.substring(0, program.indexOf('$')) : program;
    }
}
