package nl.jessenagel.orchestrate;

/**
 * Represents a numerical variable in the Orchestrate Java library.
 * This interface extends the {@link NumExpr} interface, since
 * a numerical variable is a specialized type of numerical expression.
 */
public interface NumVar extends NumExpr {

    /**
     * Gets the lower bound of the numerical variable.
     *
     * @return The lower bound of the variable.
     */
    double getLB();

    /**
     * Gets the upper bound of the numerical variable.
     *
     * @return The upper bound of the variable.
     */
    double getUB();

    /**
     * Gets the type of the numerical variable.
     *
     * @return The type of the variable as a {@link NumVarType}.
     */
    NumVarType getType();

    /**
     * Gets the name of the numerical variable.
     *
     * @return The name of the variable.
     */
    String getName();

    /**
     * Sets the lower bound of the numerical variable.
     *
     * @param lb The new lower bound of the variable.
     */
    void setLB(double lb);

    /**
     * Sets the upper bound of the numerical variable.
     *
     * @param ub The new upper bound of the variable.
     */
    void setUB(double ub);

    /**
     * Sets the name of the numerical variable.
     *
     * @param name The new name of the variable.
     */
    void setName(String name);
}