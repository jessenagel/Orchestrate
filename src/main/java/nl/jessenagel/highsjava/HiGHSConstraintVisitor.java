package nl.jessenagel.highsjava;

public class HiGHSConstraintVisitor implements ConstraintVisitor {
    private final HiGHSConstraint target;

    public HiGHSConstraintVisitor(HiGHSConstraint target) {
        this.target = target;
    }

    @Override
    public void visit(HiGHSNumExpr expr) {
        throw new HiGHSException("Cannot visit HiGHSNumExpr in HiGHSConstraintVisitor");

    }

    @Override
    public void visit(HiGHSIntExpr expr) {
        throw new HiGHSException("Cannot visit HiGHSIntExpr in HiGHSConstraintVisitor");

    }

    @Override
    public void visit(HiGHSIntVar expr) {
        throw new HiGHSException("Cannot visit HiGHSIntVar in HiGHSConstraintVisitor");
    }

    @Override
    public void visit(HiGHSNumVar expr) {
        throw new HiGHSException("Cannot visit HiGHSNumVar in HiGHSConstraintVisitor");
    }

    @Override
    public void visit(HiGHSConstraint constraint) {
        target.setName(constraint.getName());
        target.lhs = constraint.lhs;
        target.rhs = constraint.rhs;
        target.type = constraint.type;
    }

    @Override
    public void visit(HiGHSSumExpr hiGHSSumExpr) {
        throw new HiGHSException("Cannot visit HiGHSSumVar in HiGHSConstraintVisitor");

    }
    @Override
    public void visit(HiGHSProdExpr hiGHSProdExpr) {
        throw new HiGHSException("Cannot visit HiGHSProdExpr in HiGHSConstraintVisitor");

    }
}
