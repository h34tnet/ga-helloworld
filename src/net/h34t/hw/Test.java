package net.h34t.hw;

public class Test {

    public static void main(String... args) throws VM.GuruMeditationException {
        String dna = "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.";

        String res = new VM()
                .execute(dna, 100000);

        System.out.println(res);
    }
}
