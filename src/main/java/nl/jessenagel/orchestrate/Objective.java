package nl.jessenagel.orchestrate;

/**
 * Represents an objective in the Orchestrate Java library.
 * An objective is a specialized fragment that consists of a constant term
 * and a numerical expression.
 */
public interface Objective extends Fragment {
    /**
     * Resets the objective, setting it to 0
     */
    void clearExpr();

    /**
     * Gets the constant term of the objective.
     *
     * @return The constant term as a double.
     */
    double getConstant();

    /**
     * Set the constant factor of the objective to a value.
     *
     * @param v The value to set the constant factor to
     */
    void setConstant(double v);

    /**
     * Gets the numerical expression of the objective.
     *
     * @return The numerical expression as a {@link NumExpr}.
     */
    NumExpr getExpr();

    /**
     * Sets the expression of this objective, replacing any expression that might be there
     *
     * @param expr The {@link NumExpr} to set the expression to.
     */
    void setExpr(NumExpr expr);

    /**
     * Gets the ObjectiveSense of the objective.
     *
     * @return The objective sense as a {@link ObjectiveSense}
     */
    ObjectiveSense getSense();

    /**
     * Sets the sense of this objective (min or max).
     *
     * @param sense The objective sense to set as a {@link ObjectiveSense}
     */
    void setSense(ObjectiveSense sense);


}