package nl.jessenagel.highsjava;

/**
 * Represents an objective in the HiGHS model.
 * An objective consists of a numerical expression, a sense (maximize or minimize), and a name.
 */
public class HiGHSObjective implements Objective {
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
     * Constructs a new HiGHSObjective with the specified expression and sense.
     * The name is generated automatically.
     *
     * @param expr  The numerical expression representing the objective.
     * @param sense The sense of the objective (maximize or minimize).
     */
    public HiGHSObjective(NumExpr expr, ObjectiveSense sense) {
        this.expr = expr;
        this.sense = sense;
        this.name = "Objective_" + HiGHSCounter.getNextObjCounter();
    }

    /**
     * Gets the constant term of the objective's numerical expression.
     *
     * @return The constant term of the expression.
     * @throws HiGHSException If the expression type is invalid.
     */
    @Override
    public double getConstant() {
        if (expr instanceof HiGHSNumExpr expr_cast) {
            return expr_cast.constant;
        }
        if (expr instanceof HiGHSIntExpr expr_cast) {
            return expr_cast.constant.doubleValue();
        }
        if (expr instanceof HiGHSSumExpr expr_cast) {
            return new HiGHSNumExpr(expr_cast).constant;
        }
        throw new HiGHSException("Invalid expression type for constant: " + expr.getClass());
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
