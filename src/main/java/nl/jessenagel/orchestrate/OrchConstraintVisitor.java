package nl.jessenagel.orchestrate;

public class OrchConstraintVisitor implements ConstraintVisitor {
    private final OrchConstraint target;

    public OrchConstraintVisitor(OrchConstraint target) {
        this.target = target;
    }

    @Override
    public void visit(OrchNumExpr expr) {
        throw new OrchException("Cannot visit OrchNumExpr in OrchConstraintVisitor");

    }

    @Override
    public void visit(OrchIntExpr expr) {
        throw new OrchException("Cannot visit OrchIntExpr in OrchConstraintVisitor");

    }

    @Override
    public void visit(OrchIntVar expr) {
        throw new OrchException("Cannot visit OrchIntVar in OrchConstraintVisitor");
    }

    @Override
    public void visit(OrchNumVar expr) {
        throw new OrchException("Cannot visit OrchNumVar in OrchConstraintVisitor");
    }

    @Override
    public void visit(OrchConstraint constraint) {
        target.setName(constraint.getName());
        target.lhs = constraint.lhs;
        target.rhs = constraint.rhs;
        target.type = constraint.type;
    }

    @Override
    public void visit(OrchSumExpr orchSumExpr) {
        throw new OrchException("Cannot visit OrchSumExpr in OrchConstraintVisitor");

    }

}
