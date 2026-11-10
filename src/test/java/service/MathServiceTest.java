package service;

import model.Question;

import java.util.List;
import java.util.Random;

/**
 * Simple test runner (no Maven or JUnit needed).
 * Covers the test cases of Task 09.
 */
public class MathServiceTest {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean ok) {
        if (ok) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            failed++;
            System.out.println("FAIL  " + name);
        }
    }

    public static void main(String[] args) {
        // Prime number
        check("isPrime(7) = 1", MathService.isPrime(7) == 1);
        check("isPrime(8) = 0", MathService.isPrime(8) == 0);
        check("isPrime(1) = 0", MathService.isPrime(1) == 0);
        check("isPrime(0) = 0", MathService.isPrime(0) == 0);
        check("isPrime(-5) = 0", MathService.isPrime(-5) == 0);
        check("isPrime(2) = 1", MathService.isPrime(2) == 1);
        check("isPrime(97) = 1", MathService.isPrime(97) == 1);

        // Modulus
        check("17 % 5 = 2", MathService.calculateMod(17, 5) == 2);
        check("-17 % 5 = -2", MathService.calculateMod(-17, 5) == -2);
        check("17 % -5 = 2", MathService.calculateMod(17, -5) == 2);
        check("0 % 7 = 0", MathService.calculateMod(0, 7) == 0);
        boolean threw = false;
        try {
            MathService.calculateMod(10, 0);
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check("divisor 0 -> validation error", threw);

        // Trigonometry
        check("sin(30) = 0.5", Math.abs(MathService.calculateSin(30) - 0.5) < 1e-9);
        check("cos(60) = 0.5", Math.abs(MathService.calculateCos(60) - 0.5) < 1e-9);
        check("tan(45) = 1.0", Math.abs(MathService.calculateTan(45) - 1.0) < 1e-9);
        check("tan(90) is undefined (NaN)", Double.isNaN(MathService.calculateTan(90)));
        check("tan(270) is undefined (NaN)", Double.isNaN(MathService.calculateTan(270)));
        check("sin^2 + cos^2 = 1 (x=37)",
                Math.abs(Math.pow(MathService.calculateSin(37), 2)
                       + Math.pow(MathService.calculateCos(37), 2) - 1) < 1e-9);

        // Game logic
        GameService gs = new GameService(new Random(1));
        List<Question> qs = gs.createQuestions();
        check("4 questions created", qs.size() == 4);
        check("question types: prime, mod, trig, trig",
                qs.get(0).getType().equals(Question.PRIME)
             && qs.get(1).getType().equals(Question.MOD)
             && qs.get(2).getType().equals(Question.TRIG)
             && qs.get(3).getType().equals(Question.TRIG));
        check("two trig questions use different functions",
                !qs.get(2).getFn().equals(qs.get(3).getFn()));

        boolean[] all = new boolean[4];
        boolean[] none = new boolean[4];
        for (int i = 0; i < 4; i++) {
            all[i] = GameService.isCorrect(qs.get(i), GameService.expectedAnswer(qs.get(i)));
            none[i] = false;
        }
        check("all answers correct", all[0] && all[1] && all[2] && all[3]);
        check("all correct -> 10 marks", GameService.totalScore(qs, all) == 10);
        check("none correct -> 0 marks", GameService.totalScore(qs, none) == 0);

        boolean[] partial = {true, false, true, false};   // prime 2 + trig 3
        check("prime + trig1 correct -> 5 marks", GameService.totalScore(qs, partial) == 5);

        check("wrong prime answer is rejected",
                !GameService.isCorrect(qs.get(0), String.valueOf(1 - MathService.isPrime(qs.get(0).getA()))));
        check("invalid text answer gives validation message",
                GameService.validate(qs.get(1), "abc") != null);
        check("decimal is not allowed for modulus",
                GameService.validate(qs.get(1), "2.5") != null);
        check("prime answer 5 is not allowed",
                GameService.validate(qs.get(0), "5") != null);

        // Many random games: the calculated answer must always be accepted
        boolean ok = true;
        GameService many = new GameService();
        for (int g = 0; g < 2000; g++) {
            for (Question q : many.createQuestions()) {
                if (!GameService.isCorrect(q, GameService.expectedAnswer(q))) {
                    ok = false;
                }
                if (q.getType().equals(Question.MOD) && q.getB() == 0) {
                    ok = false;
                }
            }
        }
        check("2000 random games: calculated answers always accepted", ok);

        System.out.println();
        System.out.println("Passed: " + passed + "   Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }
}
