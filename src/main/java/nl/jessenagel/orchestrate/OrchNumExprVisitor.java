package nl.jessenagel.orchestrate;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


public class OrchNumExprVisitor implements NumExprVisitor {
    private final OrchNumExpr target;

    public OrchNumExprVisitor(OrchNumExpr target) {
        this.target = target;
    }

    @Override
    public void visit(OrchNumExpr expr) {
        target.variables = expr.variables.clone();
        target.coefficients = expr.coefficients.clone();
        target.constant = expr.constant;
        target.numberOfVariables = expr.numberOfVariables;
    }

    @Override
    public void visit(OrchIntExpr expr) {
        target.variables = expr.variables.clone();
        target.coefficients = expr.coefficients.clone();
        target.constant = (double) expr.constant;
        target.numberOfVariables = expr.numberOfVariables;
    }

    @Override
    public void visit(OrchIntVar expr) {
        target.variables = new int[1];
        target.coefficients = new double[1];
        target.variables[0] = expr.getIndex();
        target.coefficients[0] = 1.0;
        target.constant = 0.0;
        target.numberOfVariables = 1;
    }

    @Override
    public void visit(OrchNumVar expr) {
        target.variables = new int[1];
        target.coefficients = new double[1];
        target.variables[0] = expr.getIndex();
        target.coefficients[0] = 1.0;
        target.constant = 0.0;
        target.numberOfVariables = 1;
    }

    @Override
    public void visit(OrchConstraint orchConstraint) {
        // No action needed for OrchConstraint
    }

    @Override
    public void visit(OrchSumExpr orchSumExpr) {
        Map<Integer, Double> tempVariablesAndCoefficients = new LinkedHashMap<>();
        double tempConstant = 0.0;

        Deque<NumExpr> stack = new ArrayDeque<>(orchSumExpr.exprs);
        while (!stack.isEmpty()) {
            NumExpr expr = stack.pop();
            if (expr instanceof OrchSumExpr) {
                stack.addAll(((OrchSumExpr) expr).exprs);
            } else if (expr instanceof OrchNumExpr expr_cast) {
                for (int i = 0; i < expr_cast.variables.length; i++) {
                    int finalI = i;
                    tempVariablesAndCoefficients.compute(expr_cast.variables[i], (k, v) -> v == null ? expr_cast.coefficients[finalI] : v + expr_cast.coefficients[finalI]);
                }
                tempConstant += expr_cast.constant;
            } else if (expr instanceof OrchIntExpr expr_cast) {
                for (int i = 0; i < expr_cast.variables.length; i++) {
                    int finalI = i;
                    tempVariablesAndCoefficients.compute(expr_cast.variables[i], (k, v) -> v == null ? expr_cast.coefficients[finalI] : v + expr_cast.coefficients[finalI]);
                }
                tempConstant += expr_cast.constant.doubleValue();
            } else if (expr instanceof OrchNumVar numVar) {
                tempVariablesAndCoefficients.merge(numVar.getIndex(), 1.0, Double::sum);
            } else if (expr instanceof OrchIntVar intVar) {
                tempVariablesAndCoefficients.merge(intVar.getIndex(), 1.0, Double::sum);
            }
             else {
                throw new OrchException("Unsupported expression type: " + expr.getClass().getSimpleName());
            }
        }
        target.coefficients = new double[tempVariablesAndCoefficients.size()];
        target.variables = new int[tempVariablesAndCoefficients.size()];
        int index = 0;
        for (Entry<Integer, Double> entry : tempVariablesAndCoefficients.entrySet()) {
            target.variables[index] = entry.getKey();
            target.coefficients[index] = entry.getValue();
            index++;
        }
        target.constant = tempConstant;
        target.numberOfVariables = tempVariablesAndCoefficients.size();
    }


}
