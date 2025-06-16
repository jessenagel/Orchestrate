package nl.jessenagel.orchestrate;


import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * Represents an integer expression in the Orchestrate model.
 * An integer expression consists of a name, a list of integer variables,
 * a list of integer coefficients, and a constant term.
 */
public class OrchIntExpr implements IntExpr {
    /**
     * The name of the integer expression.
     */
    String name;

    /**
     * An array of coefficients for the integer variables in the expression.
     */
    double[] coefficients;
    /**
     * An array of integer variables in the expression.
     */
    int[] variables;

    int numberOfVariables;
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
     * Constructs a new OrchIntExpr with no variables, coefficients, or constant.
     * The name is generated automatically.
     */
    OrchIntExpr() {
        this.name = "IntExpr_" + OrchCounter.getNextVarCounter();
        this.variables = new int[1];
        this.coefficients = new double[1];
        this.constant = 0;
        this.numberOfVariables = 0;
    }

    /**
     * Constructs a new OrchIntExpr by copying an existing integer expression.
     *
     * @param expr The integer expression to copy.
     * @throws OrchException If the expression type is invalid.
     */
    OrchIntExpr(IntExpr expr) {
        this.name = expr.getName() + "_" + OrchCounter.getNextVarCounter();
        expr.accept(new OrchIntExprVisitor(this));
    }


    @Override
    public void accept(IntExprVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(NumExprVisitor visitor) {
        visitor.visit(this);
    }
}