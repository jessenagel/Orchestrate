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
     * The list of integer variables in the expression.
     */
    LinkedHashMap<IntVar, Integer> variablesAndCoefficients;

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
        this.variablesAndCoefficients = new LinkedHashMap<>();
        this.constant = 0;
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
    public String toString() {
        StringBuilder result = new StringBuilder(" ");
        for(Entry<IntVar, Integer> entry : variablesAndCoefficients.entrySet()) {
            if (entry.getValue() < 0) {
                result.append("- ");
            } else {
                result.append("+ ");
            }
            result.append(Math.abs(entry.getValue())).append(" ").append(entry.getKey().getName()).append(" ");
        }
        result.append("+ ").append(constant);
        return result.toString();
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