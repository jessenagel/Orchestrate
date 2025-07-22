package nl.jessenagel.orchestrate;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Implementation of the NumExprVisitor interface for numerical expressions.
 * This visitor transfers the state from source expressions to a target numerical expression,
 * handling various types of expressions including numerical expressions, integer expressions,
 * variables, and sum expressions.
 */
public class OrchNumExprVisitor implements NumExprVisitor {
    /** The target numerical expression to be populated by this visitor. */
    private final OrchNumExpr target;

    /**
     * Constructs a new OrchNumExprVisitor with the specified target expression.
     *
     * @param target The target numerical expression to populate with data from visited expressions
     */
    public OrchNumExprVisitor(OrchNumExpr target) {
        this.target = target;
    }

    /**
     * Copies the data from the visited numerical expression to the target.
     * Creates copies of the variable indices and coefficients arrays.
     *
     * @param expr The numerical expression to visit and copy data from
     */
    @Override
    public void visit(OrchNumExpr expr) {
        target.variables = expr.variables.clone();
        target.coefficients = expr.coefficients.clone();
        target.constant = expr.constant;
        target.numberOfVariables = expr.numberOfVariables;
    }

    /**
     * Converts an integer expression to a numerical expression and copies its data to the target.
     * Creates copies of the variable indices and coefficients arrays.
     *
     * @param expr The integer expression to convert and copy data from
     */
    @Override
    public void visit(OrchIntExpr expr) {
        target.variables = expr.variables.clone();
        target.coefficients = expr.coefficients.clone();
        target.constant = (double) expr.constant;
        target.numberOfVariables = expr.numberOfVariables;
    }

    /**
     * Converts an integer variable to a numerical expression representation in the target.
     * Sets up the target with a single variable and coefficient.
     *
     * @param expr The integer variable to convert to a numerical expression
     */
    @Override
    public void visit(OrchIntVar expr) {
        target.variables = new int[1];
        target.coefficients = new double[1];
        target.variables[0] = expr.getIndex();
        target.coefficients[0] = 1.0;
        target.constant = 0.0;
        target.numberOfVariables = 1;
    }

    /**
     * Converts a numerical variable to a numerical expression representation in the target.
     * Sets up the target with a single variable and coefficient.
     *
     * @param expr The numerical variable to convert to a numerical expression
     */
    @Override
    public void visit(OrchNumVar expr) {
        target.variables = new int[1];
        target.coefficients = new double[1];
        target.variables[0] = expr.getIndex();
        target.coefficients[0] = 1.0;
        target.constant = 0.0;
        target.numberOfVariables = 1;
    }

    /**
     * No action for constraint objects as they are not directly convertible to numerical expressions.
     *
     * @param orchConstraint The constraint that was visited
     */
    @Override
    public void visit(OrchConstraint orchConstraint) {
        // No action needed for OrchConstraint
    }

    /**
     * Processes a sum expression by flattening it and combining like terms.
     * Handles nested sum expressions by recursively processing their components.
     * Builds a consolidated representation in the target expression.
     *
     * @param orchSumExpr The sum expression to process
     * @throws OrchException If an unsupported expression type is encountered
     */
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
            } else {
                throw new OrchException("Unsupported expression type: " + expr.getClass().getSimpleName());
            }
        }

        // Create final arrays from the consolidated map
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