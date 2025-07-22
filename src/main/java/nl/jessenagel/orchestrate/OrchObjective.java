package nl.jessenagel.orchestrate;

/**
 * Represents an objective in the Orchestrate model.
 * An objective consists of a numerical expression, a sense (maximize or minimize), and a name.
 */
public class OrchObjective implements Objective {
    /**
     * The name of the objective.
     */
    String name;

    /**
     * The numerical expression representing the objective.
     */
    NumExpr expr;

    /**
     * The sense of the objective (maximize or minimize).
     */
    ObjectiveSense sense;

    /**
     * Constructs a new OrchObjective with the specified expression and sense.
     * The name is generated automatically.
     *
     * @param expr  The numerical expression representing the objective.
     * @param sense The sense of the objective (maximize or minimize).
     */
    public OrchObjective(NumExpr expr, ObjectiveSense sense) {
        this.expr = expr;
        this.sense = sense;
        this.name = "Objective_" + OrchCounter.getNextObjCounter();
    }

    @Override
    public void clearExpr() {
        this.expr = new OrchNumExpr();
    }

    /**
     * Gets the constant term of the objective's numerical expression.
     *
     * @return The constant term of the expression.
     * @throws OrchException If the expression type is invalid.
     */
    @Override
    public double getConstant() {
        if (expr instanceof OrchNumExpr expr_cast) {
            return expr_cast.constant;
        }
        if (expr instanceof OrchIntExpr expr_cast) {
            return expr_cast.constant.doubleValue();
        }
        if (expr instanceof OrchSumExpr expr_cast) {
            return new OrchNumExpr(expr_cast).constant;
        }
        throw new OrchException("Invalid expression type for constant: " + expr.getClass());
    }

    @Override
    public void setConstant(double v) {

    }

    /**
     * Gets the numerical expression of the objective.
     *
     * @return The numerical expression.
     */
    @Override
    public NumExpr getExpr() {
        return expr;
    }

    @Override
    public void setExpr(NumExpr expr) {
        this.expr =expr;
    }

    @Override
    public ObjectiveSense getSense() {
        return this.sense;
    }

    @Override
    public void setSense(ObjectiveSense sense) {
        this.sense = sense;
    }

    /**
     * Gets the name of the objective.
     *
     * @return The name of the objective.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the objective.
     *
     * @param name The new name of the objective.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(" ");
        if (sense == ObjectiveSense.Maximize) {
            result.append("Maximize ");
        } else {
            result.append("Minimize ");
        }
        result.append(expr.toString());
        return result.toString();
    }
}
