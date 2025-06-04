package nl.jessenagel.highsjava;

import java.util.*;
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

    @Override
    public void visit(HiGHSSumExpr hiGHSSumExpr) {
        Map<NumVar, Double> tempVariablesAndCoefficients = new LinkedHashMap<>();
        double tempConstant = 0.0;

        Deque<NumExpr> stack = new ArrayDeque<>(hiGHSSumExpr.exprs);
        while (!stack.isEmpty()) {
            NumExpr expr = stack.pop();

            if (expr instanceof HiGHSSumExpr) {
                stack.addAll(((HiGHSSumExpr) expr).exprs);
            } else {
                expr.accept(this);
                HiGHSNumExpr result = new HiGHSNumExpr(expr);

                for (Entry<NumVar, Double> entry : result.variablesAndCoefficients.entrySet()) {
                    tempVariablesAndCoefficients.merge(entry.getKey(), entry.getValue(), Double::sum);
                }
                tempConstant += result.constant;
            }
        }

        target.variablesAndCoefficients = tempVariablesAndCoefficients;
        target.constant = tempConstant;
    }

    @Override
    public void visit(HiGHSProdExpr hiGHSProdExpr) {
        Map<NumVar, Double> tempVariablesAndCoefficients = new LinkedHashMap<>();
        double tempConstant = 0.0;

        Deque<NumExpr> stack = new ArrayDeque<>(hiGHSProdExpr.exprs);
        while (!stack.isEmpty()) {
            NumExpr expr = stack.pop();

            if (expr instanceof HiGHSSumExpr) {
                stack.addAll(((HiGHSSumExpr) expr).exprs);
            } else {
                expr.accept(this);
                HiGHSNumExpr result = new HiGHSNumExpr(expr);

                for (Entry<NumVar, Double> entry : result.variablesAndCoefficients.entrySet()) {
                    tempVariablesAndCoefficients.merge(entry.getKey(), entry.getValue(), (existing, newValue) -> existing * newValue);
                }
                tempConstant += result.constant;
            }
        }

        target.variablesAndCoefficients = tempVariablesAndCoefficients;
        target.constant = tempConstant;
    }
}
