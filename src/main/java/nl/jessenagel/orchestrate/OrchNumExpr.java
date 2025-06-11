package nl.jessenagel.orchestrate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
/**
 * Represents a numerical expression in the Orchestrate model.
 * A numerical expression consists of variables, coefficients, and a constant term.
 */
public class OrchNumExpr implements NumExpr {
    /**
     * The name of the numerical expression.
     */
    String name;

    /**
     * The list of numerical variables in the expression.
     */
    Map<NumVar, Double> variablesAndCoefficients;

    /**
     * The constant term in the numerical expression.
     */
    Double constant;

    /**
     * Constructs a new OrchNumExpr by copying an existing numerical expression.
     *
     * @param expr The numerical expression to copy.
     * @throws OrchException If the expression type is invalid.
     */
    public OrchNumExpr(NumExpr expr) {
        name = expr.getName() + "_" + OrchCounter.getNextVarCounter();
        expr.accept(new OrchNumExprVisitor(this));
    }

    /**
     * Constructs a new OrchNumExpr for a single variable with a coefficient of 1.
     *
     * @param var The variable to include in the expression.
     */
    public OrchNumExpr(NumVar var) {
        this.name = "NumExpr_" + OrchCounter.getNextVarCounter();
        this.variablesAndCoefficients = new LinkedHashMap<>();
        this.constant = 0.0;
        this.variablesAndCoefficients.put(var,1.0);
    }

    /**
     * Constructs a new OrchNumExpr with no variables, coefficients, or constant.
     */
    OrchNumExpr() {
        this.name = "NumExpr_" + OrchCounter.getNextVarCounter();
        this.variablesAndCoefficients = new LinkedHashMap<>();
        this.constant = 0.0;
    }

    @Override
    public void accept(NumExprVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Gets the name of the numerical expression.
     *
     * @return The name of the expression.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the numerical expression.
     *
     * @param name The new name of the expression.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(" ");
        for(Entry<NumVar,Double> entry : variablesAndCoefficients.entrySet()) {
            NumVar variable = entry.getKey();
            Double coefficient = entry.getValue();
            if (coefficient < 0) {
                result.append("- ");
            } else {
                result.append("+ ");
            }
            result.append(Math.abs(coefficient)).append(" ").append(variable.getName()).append(" ");
        }
        result.append("+ ").append(constant);
        return result.toString();
    }
}