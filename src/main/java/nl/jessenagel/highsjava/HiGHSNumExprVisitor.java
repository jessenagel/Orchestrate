package nl.jessenagel.highsjava;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class HiGHSNumExprVisitor implements NumExprVisitor {
    private final HiGHSNumExpr target;

    public HiGHSNumExprVisitor(HiGHSNumExpr target) {
        this.target = target;
    }

    @Override
    public void visit(HiGHSNumExpr expr) {
        target.variablesAndCoefficients = new LinkedHashMap<>(expr.variablesAndCoefficients);
        target.constant = expr.constant;
    }

    @Override
    public void visit(HiGHSIntExpr expr) {
        target.variablesAndCoefficients = new LinkedHashMap<>();
        for(Entry<IntVar, Integer> entry : expr.variablesAndCoefficients.entrySet()) {
            target.variablesAndCoefficients.put(entry.getKey(), entry.getValue().doubleValue());
        }
        target.constant = (double) expr.constant;
    }

    @Override
    public void visit(HiGHSIntVar expr) {
        target.variablesAndCoefficients = new LinkedHashMap<>();
        target.variablesAndCoefficients.put(expr, 1.0);
        target.constant = 0.0;
    }

    @Override
    public void visit(HiGHSNumVar expr) {
        target.variablesAndCoefficients = new LinkedHashMap<>();
        target.variablesAndCoefficients.put(expr, 1.0);
        target.constant = 0.0;
    }

    @Override
    public void visit(HiGHSConstraint hiGHSConstraint) {
        // No action needed for HiGHSConstraint
    }
}
