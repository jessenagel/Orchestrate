package nl.jessenagel.orchestrate;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of the IntExprVisitor interface for integer expressions.
 * This visitor transfers the state from a source integer expression to a target integer expression.
 * Only integer expressions and variables can be visited; attempting to visit numerical expressions
 * or constraints will result in an exception.
 */
public class OrchIntExprVisitor implements IntExprVisitor {
    /**
     * The target integer expression to be populated by this visitor.
     */
    private final OrchIntExpr target;

    /**
     * Constructs a new OrchIntExprVisitor with the specified target expression.
     *
     * @param target The target integer expression to populate with data from visited expressions
     */
    public OrchIntExprVisitor(OrchIntExpr target) {
        this.target = target;
    }

    /**
     * Rejects visits from numerical expressions as they are incompatible with integer expressions.
     *
     * @param expr The numerical expression attempting to be visited
     * @throws OrchException Always thrown with appropriate error message
     */
    @Override
    public void visit(OrchNumExpr expr) {
        throw new OrchException("Cannot visit OrchNumExpr in OrchIntExprVisitor");
    }

    /**
     * Copies the data from the visited integer expression to the target.
     *
     * @param expr The integer expression to visit and copy data from
     */
    @Override
    public void visit(OrchIntExpr expr) {
        target.variables = expr.variables;
        target.coefficients = expr.coefficients;
        target.constant = expr.constant;
        target.numberOfVariables = expr.numberOfVariables;
    }

    /**
     * Converts an integer variable to an integer expression representation in the target.
     * Sets up the target with a single variable and coefficient.
     *
     * @param expr The integer variable to convert to an expression
     */
    @Override
    public void visit(OrchIntVar expr) {
        target.variables = new int[1];
        target.coefficients = new double[1];
        target.variables[0] = expr.getIndex();
        target.coefficients[0] = 1.0;
        target.constant = 0;
        target.numberOfVariables = 1;
    }

    /**
     * Rejects visits from numerical variables as they are incompatible with integer expressions.
     *
     * @param expr The numerical variable attempting to be visited
     * @throws OrchException Always thrown with appropriate error message
     */
    @Override
    public void visit(OrchNumVar expr) {
        throw new OrchException("Cannot visit OrchNumExpr in OrchIntExprVisitor");
    }

    /**
     * Rejects visits from constraints as they are incompatible with integer expressions.
     *
     * @param orchConstraint The constraint attempting to be visited
     * @throws OrchException Always thrown with appropriate error message
     */
    @Override
    public void visit(OrchConstraint orchConstraint) {
        throw new OrchException("Cannot visit OrchNumExpr in OrchIntExprVisitor");
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
        int tempConstant = 0;

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
                tempConstant += expr_cast.constant;
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
        for (Map.Entry<Integer, Double> entry : tempVariablesAndCoefficients.entrySet()) {
            target.variables[index] = entry.getKey();
            target.coefficients[index] = entry.getValue();
            index++;
        }
        target.constant = tempConstant;
        target.numberOfVariables = tempVariablesAndCoefficients.size();
    }
}