package service;

/**
 * Reusable math methods for the Math Whiz game.
 * Every answer in the game is calculated with these methods
 * (no answers are stored manually).
 */
public class MathService {

    /** Allowed difference when comparing decimal answers. */
    public static final double TOLERANCE = 0.01;

    private MathService() { }

    /** Returns 1 if number is prime, otherwise 0. Numbers below 2 are not prime. */
    public static int isPrime(int number) {
        if (number < 2) {
            return 0;
        }
        for (int i = 2; (long) i * i <= number; i++) {
            if (number % i == 0) {
                return 0;
            }
        }
        return 1;
    }

    /** Returns a % b. Throws IllegalArgumentException when the divisor is zero. */
    public static int calculateMod(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Divisor must not be zero.");
        }
        return a % b;
    }

    /** sine of an angle given in degrees. */
    public static double calculateSin(double degree) {
        return Math.sin(Math.toRadians(degree));
    }

    /** cosine of an angle given in degrees. */
    public static double calculateCos(double degree) {
        return Math.cos(Math.toRadians(degree));
    }

    /** tangent of an angle given in degrees. Returns NaN where tan is undefined (90, 270, ...). */
    public static double calculateTan(double degree) {
        double r = ((degree % 180) + 180) % 180;   // bring angle into 0..180
        if (Math.abs(r - 90) < 1e-9) {
            return Double.NaN;
        }
        return Math.tan(Math.toRadians(degree));
    }

    /** Picks sin / cos / tan by name. */
    public static double calculateTrig(String fn, double degree) {
        switch (fn) {
            case "sin": return calculateSin(degree);
            case "cos": return calculateCos(degree);
            case "tan": return calculateTan(degree);
            default: throw new IllegalArgumentException("Unknown function: " + fn);
        }
    }

    /** True when two decimal values differ by no more than TOLERANCE. */
    public static boolean isClose(double expected, double actual) {
        return Math.abs(expected - actual) <= TOLERANCE;
    }
}
