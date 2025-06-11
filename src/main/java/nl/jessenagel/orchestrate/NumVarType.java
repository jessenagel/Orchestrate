package nl.jessenagel.orchestrate;

/**
 * Abstract class representing a numerical variable type.
 * This class defines three types of numerical variables: Bool, Int, and Float.
 * It uses a private constructor to prevent instantiation and initializes the types
 * as static final instances.
 */
public abstract class NumVarType {
    /**
     * Represents a boolean numerical variable type.
     */
    public static final NumVarType Bool;

    /**
     * Represents an integer numerical variable type.
     */
    public static final NumVarType Int;

    /**
     * Represents a floating-point numerical variable type.
     */
    public static final NumVarType Float;

    /**
     * Private constructor to prevent instantiation of the abstract class.
     */
    NumVarType() {
        // Constructor is private to prevent instantiation
    }

    // Static block to initialize the numerical variable types
    static {
        Bool = new NumVarType() {
            @Override
            public String toString() {
                return "Bool";
            }
        };

        Int = new NumVarType() {
            @Override
            public String toString() {
                return "Int";
            }
        };
        Float = new NumVarType() {
            @Override
            public String toString() {
                return "Float";
            }
        };
    }
}