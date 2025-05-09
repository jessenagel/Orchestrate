package nl.jessenagel.highsjava;

import java.util.ArrayList;
import java.util.List;

public class HiGHSIntExprVisitor implements IntExprVisitor {
    private final HiGHSIntExpr target;

    public HiGHSIntExprVisitor(HiGHSIntExpr target) {
        this.target = target;
    }


    @Override
    public void visit(HiGHSNumExpr expr) {
        throw new HiGHSException("Cannot visit HiGHSNumExpr in HiGHSIntExprVisitor");
    }

    @Override
    public void visit(HiGHSIntExpr expr) {
        target.coefficients = new ArrayList<>(expr.coefficients);
        target.variables = new ArrayList<>(expr.variables);
        target.constant = expr.constant;
    }

    @Override
    public void visit(HiGHSIntVar expr) {
        target.coefficients = new ArrayList<>();
        target.coefficients.add(1);
        target.variables = new ArrayList<>();
        target.variables.add(expr);
        target.constant = 0;
    }

    @Override
    public void visit(HiGHSNumVar expr) {
        throw new HiGHSException("Cannot visit HiGHSNumExpr in HiGHSIntExprVisitor");

    }

    @Override
    public void visit(HiGHSConstraint hiGHSConstraint) {
        throw new HiGHSException("Cannot visit HiGHSNumExpr in HiGHSIntExprVisitor");
    }
}
