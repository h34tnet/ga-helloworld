package net.h34t.hw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Mutator {


    public static String generateRandomProgram(Random r, int length, final char[] ops) {
        return IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(selectRandom(r, ops)))
                .collect(Collectors.joining());
    }

    public static String mutate(Random r, String program, double rate, final char[] ops) {
        if (program.length() == 0) {
            return String.valueOf(selectRandom(r, ops));

        } else {
            List<Character> prog = new ArrayList<>(program.length());
            for (char c : program.toCharArray()) {
                if (r.nextDouble() < rate) {
                    switch (r.nextInt(3)) {
                        case 0:
                            prog.add(selectRandom(r, ops));
                            break;
                        case 1:
                            if (r.nextBoolean()) {
                                prog.add(c);
                                prog.add(selectRandom(r, ops));
                                
                            } else {
                                prog.add(selectRandom(r, ops));
                                prog.add(c);
                            }
                            break;
                        case 3:
                            break;
                    }

                } else {
                    prog.add(c);
                }
            }

            return prog.stream().map(String::valueOf).collect(Collectors.joining());
        }
    }

    private static char selectRandom(Random r, char[] chars) {
        return chars[r.nextInt(chars.length)];
    }
}
