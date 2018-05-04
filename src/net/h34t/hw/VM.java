package net.h34t.hw;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class VM {

    public static final char[] INSTRUCTIONS = "<>+-.[]".toCharArray();

    private byte[] mem;

    private int dp;

    private int ip;

    public VM() {
        mem = new byte[64];
        dp = 0;
        ip = 0;
    }

    public VM(int ramsize) {
        mem = new byte[ramsize];
        dp = 0;
        ip = 0;
    }

    public String execute(String program, long instLimit) throws GuruMeditationException {
        StringBuilder output = new StringBuilder();

        while (ip < program.length() && ip > -1) {
            char inst = program.charAt(ip);
            exec(inst, program, output::append);

            if (instLimit-- < 0)
                throw new GuruMeditationException("Program takes too long to finish");
        }

        return output.toString();
    }

    private void exec(char inst, String program, Consumer<Character> printer) throws GuruMeditationException {
        switch (inst) {
            case '<':
                dp--;
                if (dp < 0) throw new GuruMeditationException("Memory error: " + dp);
                ip++;
                return;
            case '>':
                dp++;
                if (dp >= mem.length)
                    throw new GuruMeditationException("Memory error: " + dp);
                ip++;
                return;
            case '+':
                mem[dp]++;
                ip++;
                return;
            case '-':
                mem[dp]--;
                ip++;
                return;
            case '.':
                printer.accept(new String(new byte[]{mem[dp]}, StandardCharsets.ISO_8859_1).charAt(0));
                ip++;
                return;
            case ',':
                // we do not accept input
                ip++;
                return;
            case '[':
                // if the byte at the data pointer is zero, then instead of moving the instruction pointer forward to
                // the next command, jump it forward to the command after the matching ] command.
                if (mem[dp] == 0) {
                    ip = findMatchingCloser(ip, program) + 1;
                } else
                    ip++;
                return;
            case ']':
                // if the byte at the data pointer is nonzero, then instead of moving the instruction pointer forward
                // to the next command, jump it back to the command after the matching [ command.
                if (mem[dp] != 0) {
                    ip = findMatchingOpener(ip, program) + 1;
                } else
                    ip++;

                return;
//            case '$':
//                ip = -1;
//                return;
            default:
                throw new RuntimeException("Illegal instruction " + inst);
        }
    }

    private int findMatchingOpener(int ip, String program) throws GuruMeditationException {
        int depth = 0;

        if (ip > 0) {
            for (int p = ip - 1; p >= 0; p--) {
                char c = program.charAt(p);
                if (c == ']') {
                    depth++;
                } else if (c == '[' && depth > 0) {
                    depth--;

                } else if (c == '[' && depth == 0) {
                    return p;
                }
            }
        }

        throw new GuruMeditationException("No matching [ for ]");
    }


    private int findMatchingCloser(int ip, String program) throws GuruMeditationException {
        int depth = 0;

        if (ip < program.length() - 1) {
            for (int p = ip + 1; p < program.length(); p++) {
                char c = program.charAt(p);
                if (c == '[') {
                    depth++;

                } else if (c == ']' && depth > 0) {
                    depth--;

                } else if (c == ']' && depth == 0) {
                    return p;
                }
            }
        }

        throw new GuruMeditationException("No matching ] for [");
    }

    public static class GuruMeditationException extends Exception {
        public GuruMeditationException(String msg) {
            super(msg);
        }
    }
}