package nl.jessenagel.highsjava;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a numerical expression in the HiGHS model.
 * A numerical expression consists of variables, coefficients, and a constant term.
 */
public class HiGHSNumExpr implements NumExpr {
    /**
     * The name of the numerical expression.
     */
    String name;

    /**
     * The list of variables in the numerical expression.
     */
    List<NumVar> variables;

    /**
     * The list of coefficients corresponding to the variables.
     */
    List<Double> coefficients;

    /**
     * The constant term in the numerical expression.
     */
    Double constant;

    /**
     * Constructs a new HiGHSNumExpr by copying an existing numerical expression.
     *
     * @param expr The numerical expression to copy.
     * @throws HiGHSException If the expression type is invalid.
     */
    public HiGHSNumExpr(NumExpr expr) {
        name = expr.getName() + "_" + HiGHSCounter.getNextVarCounter();
        expr.accept(new HiGHSNumExprVisitor(this));
    }

    /**
     * Constructs a new HiGHSNumExpr for a single variable with a coefficient of 1.
     *
     * @param var The variable to include in the expression.
     */
    public HiGHSNumExpr(NumVar var) {
        this.name = "NumExpr_" + HiGHSCounter.getNextVarCounter();
        this.variables = new ArrayList<>();
        this.coefficients = new ArrayList<>();
        this.constant = 0.0;
        this.variables.add(var);
        this.coefficients.add(1.0);
    }

    /**
     * Constructs a new HiGHSNumExpr with no variables, coefficients, or constant.
     */
    HiGHSNumExpr() {
        this.name = "NumExpr_" + HiGHSCounter.getNextVarCounter();
        this.variables = new ArrayList<>();
        this.coefficients = new ArrayList<>();
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
        for (int i = 0; i < coefficients.size(); i++) {
            if (coefficients.get(i) < 0) {
                result.append("- ");
            } else {
                result.append("+ ");
            }
            result.append(Math.abs(coefficients.get(i))).append(" ").append(variables.get(i).getName()).append(" ");
        }
        result.append("+ ").append(constant);
        return result.toString();
    }
}