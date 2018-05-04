package net.h34t.hw;

import java.util.Comparator;

public class ScoredProgram {

    public static final Comparator<ScoredProgram> CMP = (p1, p2) -> Double.compare(p1.score, p2.score);

    public final double score;
    public String program;
    public final String output;

    public ScoredProgram(double score, String program, String output) {
        this.score = score;
        this.program = program;
        this.output = output;
    }

    @Override
    public String toString() {
        return String.format("%.8f: \"%s\" (%d) \"%s\"",
                this.score,
                this.output,
                this.program.length(),
                this.program);
    }
}
