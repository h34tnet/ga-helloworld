package net.h34t.hw;

public class Evaluator {

    // allow the VM to execute N instructions before rejecting the program
    public static final long INST_LIMIT = 1_024;

    /**
     * Runs the program, rates it and returns it.
     *
     * @param program the program to run
     * @param target  the output to score against
     * @return the program plus its score or null, if the program was invalid
     */
    public static ScoredProgram score(String program, String target) {
        try {
            // execute the program
            String output = new VM().execute(program, () -> (byte) 0, INST_LIMIT);

            // compare the output to the target string and get the similarity score
            double score = similarity(output, target);

            // increase the score depending on program length
            // to favor shorter programs
            score = score + (1d - 1d / (1d + program.length()));

            return new ScoredProgram(score, program, output);

        } catch (VM.GuruMeditationException e) {
            return null;
        }
    }

    /**
     * This fitness function is minimizing, i.e. the better it is, the lower the number.
     * If the source matches the target, it returns 0. Otherwise the more different, the higher the number.
     * <p>
     * Note that this function applies a penalty if strings are of different length.
     *
     * @param s1 candidate string
     * @param s2 target string
     * @return a number indicating the difference between so urce and target strings
     */
    public static double similarity(String s1, String s2) {
        double diff = 0;
        for (int i = 0, ii = Math.min(s1.length(), s2.length()); i < ii; i++) {
            int d = s1.charAt(i) - s2.charAt(i);
            // rewards matches at the front of the string
            // diff += (d * d * (ii + 1)) / (i + 1);

            diff += d * d;
        }

        // adds a penalty for different length
        double ld = Math.abs(s1.length() - s2.length()) * (256 * 256);
        return diff + ld;
    }
}
