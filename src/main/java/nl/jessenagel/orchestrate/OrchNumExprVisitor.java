package nl.jessenagel.orchestrate;

import java.util.*;
import java.util.Map.Entry;

public class OrchNumExprVisitor implements NumExprVisitor {
    private final OrchNumExpr target;

    public OrchNumExprVisitor(OrchNumExpr target) {
        this.target = target;
    }

    @Override
    public void visit(OrchNumExpr expr) {
        target.variablesAndCoefficients = new LinkedHashMap<>(expr.variablesAndCoefficients);
        target.constant = expr.constant;
    }

    @Override
    public void visit(OrchIntExpr expr) {
        target.variablesAndCoefficients = new LinkedHashMap<>();
        for(Entry<IntVar, Integer> entry : expr.variablesAndCoefficients.entrySet()) {
            target.variablesAndCoefficients.put(entry.getKey(), entry.getValue().doubleValue());
        }
        target.constant = (double) expr.constant;
    }

    @Override
    public void visit(OrchIntVar expr) {
        target.variablesAndCoefficients = new LinkedHashMap<>();
        target.variablesAndCoefficients.put(expr, 1.0);
        target.constant = 0.0;
    }

    @Override
    public void visit(OrchNumVar expr) {
        target.variablesAndCoefficients = new LinkedHashMap<>();
        target.variablesAndCoefficients.put(expr, 1.0);
        target.constant = 0.0;
    }

    @Override
    public void visit(OrchConstraint orchConstraint) {
        // No action needed for OrchConstraint
    }

    @Override
    public void visit(OrchSumExpr orchSumExpr) {
        Map<NumVar, Double> tempVariablesAndCoefficients = new LinkedHashMap<>();
        double tempConstant = 0.0;

        Deque<NumExpr> stack = new ArrayDeque<>(orchSumExpr.exprs);
        while (!stack.isEmpty()) {
            NumExpr expr = stack.pop();

            if (expr instanceof OrchSumExpr) {
                stack.addAll(((OrchSumExpr) expr).exprs);
            } else {
                expr.accept(this);
                OrchNumExpr result = new OrchNumExpr(expr);

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
    public void visit(OrchProdExpr orchProdExpr) {
        Map<NumVar, Double> tempVariablesAndCoefficients = new LinkedHashMap<>();
        double tempConstant = 0.0;

        Deque<NumExpr> stack = new ArrayDeque<>(orchProdExpr.exprs);
        while (!stack.isEmpty()) {
            NumExpr expr = stack.pop();

            if (expr instanceof OrchSumExpr) {
                stack.addAll(((OrchSumExpr) expr).exprs);
            } else {
                expr.accept(this);
                OrchNumExpr result = new OrchNumExpr(expr);

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
