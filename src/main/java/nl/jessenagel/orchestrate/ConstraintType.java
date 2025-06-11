package nl.jessenagel.orchestrate;

/**
 * Abstract class representing a numerical variable type.
 * This class defines three types of numerical variables: Bool, Int, and Float.
 * It uses a private constructor to prevent instantiation and initializes the types
 * as static final instances.
 */
public abstract class ConstraintType {
    /**
     * Represents a boolean numerical variable type.
     */
    public static final ConstraintType Ge;

    /**
     * Represents an integer numerical variable type.
     */
    public static final ConstraintType Le;

    /**
     * Represents a floating-point numerical variable type.
     */
    public static final ConstraintType Eq;

    /**
     * Private constructor to prevent instantiation of the abstract class.
     */
    ConstraintType() {
        // Constructor is private to prevent instantiation
    }

    // Static block to initialize the numerical variable types
    static {
        Ge = new ConstraintType() {
            @Override
            public String toString() {
                return "Ge";
            }
        };

        Le = new ConstraintType() {
            @Override
            public String toString() {
                return "Le";
            }
        };
        Eq = new ConstraintType() {
            @Override
            public String toString() {
                return "eq";
            }
        };
    }
}