package nl.jessenagel.orchestrate;

    /**
     * Represents an integer variable in the Orchestrate Java library.
     * This interface extends the {@link NumVar} interface, since
     * an integer variable is a specialized type of numerical variable.
     */
    public interface IntVar extends NumVar, IntExpr{

        /**
         * Gets the maximum value of the integer variable.
         *
         * @return The maximum value of the variable.
         */
        int getMax();

        /**
         * Gets the minimum value of the integer variable.
         *
         * @return The minimum value of the variable.
         */
        int getMin();

        /**
         * Sets the maximum value of the integer variable.
         *
         * @param max The new maximum value of the variable.
         * @return The updated maximum value of the variable.
         */
        int setMax(int max);

        /**
         * Sets the minimum value of the integer variable.
         *
         * @param min The new minimum value of the variable.
         * @return The updated minimum value of the variable.
         */
        int setMin(int min);
    }