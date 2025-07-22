package nl.jessenagel.orchestrate;

/**
 * Interface for visiting numerical expressions in the visitor pattern.
 * Provides methods to visit different types of numerical expressions.
 */
public interface NumExprVisitor {

    /**
     * Visits a numerical expression.
     *
     * @param expr The numerical expression to visit
     */
    void visit(OrchNumExpr expr);

    /**
     * Visits an integer expression.
     *
     * @param expr The integer expression to visit
     */
    void visit(OrchIntExpr expr);

    void visit(OrchIntVar expr);

    /**
     * Visits a numerical variable.
     *
     * @param expr The numerical variable to visit
     */
    void visit(OrchNumVar expr);

    /**
     * Visits a constraint.
     *
     * @param orchConstraint The constraint to visit
     */
    void visit(OrchConstraint orchConstraint);

    /**
     * Visits a sum expression.
     *
     * @param orchSumExpr The sum expression to visit
     */
    void visit(OrchSumExpr orchSumExpr);
}
