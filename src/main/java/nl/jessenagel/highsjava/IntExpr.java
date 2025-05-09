package nl.jessenagel.highsjava;

/**
 * Represents an integer expression in the HiGHS Java library.
 * This interface extends the {@link NumExpr} interface, since
 * an integer expression is a specialized type of numerical expression.
 */
public interface IntExpr extends NumExpr {

    /**
     * Accepts a visitor to perform operations on this integer expression.
     *
     * @param visitor The visitor to accept.
     */
    void accept(IntExprVisitor visitor);
}