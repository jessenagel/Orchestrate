package nl.jessenagel.highsjava;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents an integer expression in the HiGHS model.
 * An integer expression consists of a name, a list of integer variables,
 * a list of integer coefficients, and a constant term.
 */
public class HiGHSIntExpr implements IntExpr {
    /**
     * The name of the integer expression.
     */
    String name;

    /**
     * The list of integer variables in the expression.
     */
    List<IntVar> variables;

    /**
     * The list of integer coefficients corresponding to the variables.
     */
    List<Integer> coefficients;

    /**
     * The constant term in the integer expression.
     */
    Integer constant;

    /**
     * Gets the name of the integer expression.
     *
     * @return The name of the expression.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the integer expression.
     *
     * @param name The new name of the expression.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Constructs a new HiGHSIntExpr with no variables, coefficients, or constant.
     * The name is generated automatically.
     */
    HiGHSIntExpr() {
        this.name = "IntExpr_" + HiGHSCounter.getNextVarCounter();
        this.variables = new ArrayList<>();
        this.coefficients = new ArrayList<>();
        this.constant = 0;
    }

    /**
     * Constructs a new HiGHSIntExpr by copying an existing integer expression.
     *
     * @param expr The integer expression to copy.
     * @throws HiGHSException If the expression type is invalid.
     */
    HiGHSIntExpr(IntExpr expr) {
        this.name = expr.getName() + "_" + HiGHSCounter.getNextVarCounter();
        if (expr instanceof HiGHSIntExpr expr_cast) {
            this.coefficients = new ArrayList<>(expr_cast.coefficients);
            this.variables = new ArrayList<>(expr_cast.variables);
            this.constant = expr_cast.constant;
        }
        if (expr instanceof HiGHSNumExpr expr_cast) {
            throw new HiGHSException("Invalid expression type, converting NumExpr to IntExpr is not defined: " + expr.getClass() + ", " + expr_cast.getClass());
        }
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