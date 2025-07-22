package nl.jessenagel.orchestrate;

/**
 * Interface for visiting constraint objects in the visitor pattern.
 * Extends {@link NumExprVisitor} to inherit methods for visiting numerical expressions.
 * This visitor handles both constraints and integer expressions.
 */
public interface ConstraintVisitor extends NumExprVisitor {
    /**
     * Visits an orchestrated constraint.
     *
     * @param constraint The constraint to visit
     */
    void visit(OrchConstraint constraint);

    /**
     * Visits an integer expression.
     *
     * @param expr The integer expression to visit
     */
    void visit(OrchIntExpr expr);

    /**
     * Visits an integer variable.
     *
     * @param expr The integer variable to visit
     */
    void visit(OrchIntVar expr);

    /**
     * Visits a numerical variable.
     *
     * @param expr The numerical variable to visit
     */
    void visit(OrchNumVar expr);
}