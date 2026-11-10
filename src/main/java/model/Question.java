package model;

import java.io.Serializable;

/** One game question. Only the inputs are stored; the answer is always calculated. */
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PRIME = "PRIME";
    public static final String MOD = "MOD";
    public static final String TRIG = "TRIG";

    private final String type;
    private final int marks;
    private final int a;          // number (prime), dividend (mod)
    private final int b;          // divisor (mod)
    private final String fn;      // sin / cos / tan (trig)
    private final int angle;      // degrees (trig)

    public Question(String type, int marks, int a, int b, String fn, int angle) {
        this.type = type;
        this.marks = marks;
        this.a = a;
        this.b = b;
        this.fn = fn;
        this.angle = angle;
    }

    public String getType() { return type; }
    public int getMarks() { return marks; }
    public int getA() { return a; }
    public int getB() { return b; }
    public String getFn() { return fn; }
    public int getAngle() { return angle; }

    /** Text shown to the player. */
    public String getText() {
        switch (type) {
            case PRIME:
                return "Is " + a + " a prime number? Enter 1 for Prime or 0 for Not Prime.";
            case MOD:
                return "What is the remainder of " + a + " % " + b + " ? (integer only)";
            default:
                return "Find the value of " + fn + "(" + angle + "\u00B0). Round to 2 decimal places.";
        }
    }

    /** Hint shown under the input box. */
    public String getHint() {
        switch (type) {
            case PRIME: return "Allowed answers: 1 or 0";
            case MOD:   return "Whole number, e.g. 3 or -2";
            default:    return "Decimal number, e.g. 0.87";
        }
    }
}
