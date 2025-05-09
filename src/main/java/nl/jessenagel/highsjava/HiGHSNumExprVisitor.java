package nl.jessenagel.highsjava;

import java.util.ArrayList;
import java.util.List;

public class HiGHSNumExprVisitor implements NumExprVisitor {
    private final HiGHSNumExpr target;

    public HiGHSNumExprVisitor(HiGHSNumExpr target) {
        this.target = target;
    }

    @Override
    public void visit(HiGHSNumExpr expr) {
        target.coefficients = new ArrayList<>(expr.coefficients);
        target.variables = new ArrayList<>(expr.variables);
        target.constant = expr.constant;
    }

    @Override
    public void visit(HiGHSIntExpr expr) {
        target.coefficients = new ArrayList<>();
        for (Integer value : expr.coefficients) {
            target.coefficients.add(value.doubleValue());
        }
        target.variables = new ArrayList<>(expr.variables);
        target.constant = (double) expr.constant;
    }

    @Override
    public void visit(HiGHSIntVar expr) {
        target.coefficients = new ArrayList<>();
        target.coefficients.add(1.0);
        target.variables = new ArrayList<>();
        target.variables.add(expr);
        target.constant = 0.0;
    }

    @Override
    public void visit(HiGHSNumVar expr) {
        target.coefficients = new ArrayList<>();
        target.coefficients.add(1.0);
        target.variables = new ArrayList<>();
        target.variables.add(expr);
        target.constant = 0.0;
    }

    @Override
    public void visit(HiGHSConstraint hiGHSConstraint) {
        // No action needed for HiGHSConstraint
    }
}
