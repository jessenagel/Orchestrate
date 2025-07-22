package nl.jessenagel.orchestrate;

/**
 * Implementation of the ConstraintVisitor interface for constraints.
 * This visitor transfers the state from a source constraint to a target constraint.
 * Only constraint objects can be visited; attempting to visit expressions or variables
 * will result in an exception.
 */
public class OrchConstraintVisitor implements ConstraintVisitor {
    /** The target constraint to be populated by this visitor. */
    private final OrchConstraint target;

    /**
     * Constructs a new OrchConstraintVisitor with the specified target constraint.
     *
     * @param target The target constraint to populate with data from visited constraints
     */
    public OrchConstraintVisitor(OrchConstraint target) {
        this.target = target;
    }

    /**
     * Rejects visits from numerical expressions as they are incompatible with constraints.
     *
     * @param expr The numerical expression attempting to be visited
     * @throws OrchException Always thrown with appropriate error message
     */
    @Override
    public void visit(OrchNumExpr expr) {
        throw new OrchException("Cannot visit OrchNumExpr in OrchConstraintVisitor");
    }

    /**
     * Rejects visits from integer expressions as they are incompatible with constraints.
     *
     * @param expr The integer expression attempting to be visited
     * @throws OrchException Always thrown with appropriate error message
     */
    @Override
    public void visit(OrchIntExpr expr) {
        throw new OrchException("Cannot visit OrchIntExpr in OrchConstraintVisitor");
    }

    /**
     * Rejects visits from integer variables as they are incompatible with constraints.
     *
     * @param expr The integer variable attempting to be visited
     * @throws OrchException Always thrown with appropriate error message
     */
    @Override
    public void visit(OrchIntVar expr) {
        throw new OrchException("Cannot visit OrchIntVar in OrchConstraintVisitor");
    }

    /**
     * Rejects visits from numerical variables as they are incompatible with constraints.
     *
     * @param expr The numerical variable attempting to be visited
     * @throws OrchException Always thrown with appropriate error message
     */
    @Override
    public void visit(OrchNumVar expr) {
        throw new OrchException("Cannot visit OrchNumVar in OrchConstraintVisitor");
    }

    /**
     * Copies the data from the visited constraint to the target constraint.
     * Transfers the name, left-hand side, right-hand side, and constraint type.
     *
     * @param constraint The constraint to visit and copy data from
     */
    @Override
    public void visit(OrchConstraint constraint) {
        target.setName(constraint.getName());
        target.lhs = constraint.lhs;
        target.rhs = constraint.rhs;
        target.type = constraint.type;
    }

    /**
     * Rejects visits from sum expressions as they are incompatible with constraints.
     *
     * @param orchSumExpr The sum expression attempting to be visited
     * @throws OrchException Always thrown with appropriate error message
     */
    @Override
    public void visit(OrchSumExpr orchSumExpr) {
        throw new OrchException("Cannot visit OrchSumExpr in OrchConstraintVisitor");
    }
}