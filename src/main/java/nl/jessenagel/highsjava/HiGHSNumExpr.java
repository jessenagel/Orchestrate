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
        if (expr instanceof HiGHSNumExpr expr_cast) {
            this.coefficients = new ArrayList<>(expr_cast.coefficients);
            this.variables = new ArrayList<>(expr_cast.variables);
            this.constant = expr_cast.constant;
        } else if (expr instanceof HiGHSIntExpr expr_cast) {
            this.coefficients = new ArrayList<>();
            for (Integer value : expr_cast.coefficients) {
                this.coefficients.add(value.doubleValue());
            }
            this.variables = new ArrayList<>(expr_cast.variables);
            this.constant = (double) expr_cast.constant;
        } else if (expr instanceof HiGHSIntVar expr_cast) {
            this.coefficients = new ArrayList<>();
            this.coefficients.add(1.0);
            this.variables = new ArrayList<>();
            this.variables.add(expr_cast);
            this.constant = 0.0;
        } else if (expr instanceof HiGHSNumVar expr_cast) {
            this.coefficients = new ArrayList<>();
            this.coefficients.add(1.0);
            this.variables = new ArrayList<>();
            this.variables.add(expr_cast);
            this.constant = 0.0;
        } else {
            throw new HiGHSException("Invalid expression type: " + expr.getClass());
        }
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