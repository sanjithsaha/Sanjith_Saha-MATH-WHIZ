package service;

import model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/** Builds the four questions and evaluates the player's answers. */
public class GameService {

    public static final String DIFFICULTY = "NORMAL";

    private static final String[] FUNCTIONS = {"sin", "cos", "tan"};
    // 90 and 270 are left out because tan is undefined there
    private static final int[] ANGLES = {0, 30, 45, 60, 120, 135, 150, 180, 210, 225, 240, 300, 315, 330};

    private final Random random;

    public GameService() {
        this(new Random());
    }

    public GameService(Random random) {
        this.random = random;
    }

    /** Q1 prime (2 marks), Q2 modulus (2 marks), Q3 and Q4 trigonometry (3 marks each). */
    public List<Question> createQuestions() {
        List<Question> list = new ArrayList<>();

        int number = 2 + random.nextInt(98);                 // 2..99
        list.add(new Question(Question.PRIME, 2, number, 0, null, 0));

        int dividend = 10 + random.nextInt(90);              // 10..99
        int divisor = 2 + random.nextInt(11);                // 2..12 (never zero)
        list.add(new Question(Question.MOD, 2, dividend, divisor, null, 0));

        int f1 = random.nextInt(FUNCTIONS.length);
        int f2 = (f1 + 1 + random.nextInt(FUNCTIONS.length - 1)) % FUNCTIONS.length; // different function
        list.add(trig(FUNCTIONS[f1]));
        list.add(trig(FUNCTIONS[f2]));
        return list;
    }

    private Question trig(String fn) {
        int angle = ANGLES[random.nextInt(ANGLES.length)];
        return new Question(Question.TRIG, 3, 0, 0, fn, angle);
    }

    /** The correct answer, always calculated with MathService. */
    public static String expectedAnswer(Question q) {
        switch (q.getType()) {
            case Question.PRIME:
                return String.valueOf(MathService.isPrime(q.getA()));
            case Question.MOD:
                return String.valueOf(MathService.calculateMod(q.getA(), q.getB()));
            default:
                String text = String.format(Locale.US, "%.2f",
                        MathService.calculateTrig(q.getFn(), q.getAngle()));
                return text.equals("-0.00") ? "0.00" : text;
        }
    }

    /** Returns an error message if the raw answer is not valid, otherwise null. */
    public static String validate(Question q, String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return "Answer is required.";
        }
        String s = raw.trim();
        try {
            switch (q.getType()) {
                case Question.PRIME:
                    int p = Integer.parseInt(s);
                    return (p == 0 || p == 1) ? null : "Enter only 1 (prime) or 0 (not prime).";
                case Question.MOD:
                    Integer.parseInt(s);
                    return null;
                default:
                    double d = Double.parseDouble(s);
                    return (Double.isNaN(d) || Double.isInfinite(d)) ? "Enter a valid number." : null;
            }
        } catch (NumberFormatException e) {
            return q.getType().equals(Question.TRIG) ? "Enter a valid number." : "Enter a whole number.";
        }
    }

    /** True when the (already validated) answer is correct. */
    public static boolean isCorrect(Question q, String raw) {
        if (validate(q, raw) != null) {
            return false;
        }
        String s = raw.trim();
        switch (q.getType()) {
            case Question.PRIME:
                return Integer.parseInt(s) == MathService.isPrime(q.getA());
            case Question.MOD:
                return Integer.parseInt(s) == MathService.calculateMod(q.getA(), q.getB());
            default:
                return MathService.isClose(
                        MathService.calculateTrig(q.getFn(), q.getAngle()), Double.parseDouble(s));
        }
    }

    /** Adds the marks of every correct question (maximum 10). */
    public static int totalScore(List<Question> questions, boolean[] correct) {
        int totalScore = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (correct[i]) {
                totalScore += questions.get(i).getMarks();
            }
        }
        return totalScore;
    }
}
