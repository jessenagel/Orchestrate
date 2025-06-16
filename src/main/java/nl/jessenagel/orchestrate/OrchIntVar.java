package nl.jessenagel.orchestrate;


/**
 * Represents an integer variable in the Orchestrate model.
 * An integer variable has a name, minimum bound, maximum bound, and a type.
 */
public class OrchIntVar implements IntVar {
    /**
     * The name of the integer variable.
     */
    String name;
    /**
     * The unique identifier for the integer variable. This is the column index in the model.
     */
    int index;
    /**
     * The maximum bound of the integer variable.
     */
    int max;

    /**
     * The minimum bound of the integer variable.
     */
    int min;

    /**
     * The type of the variable, which is set to integer.
     */
    NumVarType type = NumVarType.Int;


    /**
     * Constructs a new OrchIntVar with default bounds and a generated name.
     * The minimum bound is set to 0, and the maximum bound is set to Integer.MAX_VALUE.
     */
    public OrchIntVar(int index) {
        this.index = index;
        this.name = "IntVar_" + OrchCounter.getNextVarCounter();
        this.max = Integer.MAX_VALUE;
        this.min = 0;
    }

    /**
     * Constructs a new OrchIntVar with specified minimum and maximum bounds.
     *
     * @param min The minimum bound of the integer variable.
     * @param max The maximum bound of the integer variable.
     */
    public OrchIntVar(int index, int min, int max) {
        this.name = "IntVar_" + OrchCounter.getNextVarCounter();
        this.max = max;
        this.min = min;
    }

    /**
     * Constructs a new OrchIntVar with a specified maximum bound.
     * The minimum bound is set to 0.
     *
     * @param max The maximum bound of the integer variable.
     */
    public OrchIntVar(int index, int max) {
        this.name = "IntVar_" + OrchCounter.getNextVarCounter();
        this.max = max;
        this.min = 0;
    }

    /**
     * Gets the maximum bound of the integer variable.
     *
     * @return The maximum bound of the variable.
     */
    @Override
    public int getMax() {
        return max;
    }

    /**
     * Gets the minimum bound of the integer variable.
     *
     * @return The minimum bound of the variable.
     */
    @Override
    public int getMin() {
        return min;
    }

    /**
     * Sets the maximum bound of the integer variable.
     *
     * @param max The new maximum bound of the variable.
     * @return The updated maximum bound.
     */
    @Override
    public int setMax(int max) {
        this.max = max;
        return max;
    }

    /**
     * Sets the minimum bound of the integer variable.
     *
     * @param min The new minimum bound of the variable.
     * @return The updated minimum bound.
     */
    @Override
    public int setMin(int min) {
        this.min = min;
        return min;
    }

    /**
     * Gets the lower bound of the integer variable as a double.
     *
     * @return The lower bound of the variable.
     */
    @Override
    public double getLB() {
        return min;
    }

    /**
     * Gets the upper bound of the integer variable as a double.
     *
     * @return The upper bound of the variable.
     */
    @Override
    public double getUB() {
        return max;
    }

    /**
     * Gets the type of the integer variable.
     *
     * @return The type of the variable, or null if not specified.
     */
    @Override
    public NumVarType getType() {
        return null;
    }

    /**
     * Gets the name of the integer variable.
     *
     * @return The name of the variable.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the lower bound of the integer variable. This method is not implemented.
     *
     * @param lb The new lower bound of the variable.
     */
    @Override
    public void setLB(double lb) {
        if (lb > this.max) {
            throw new IllegalArgumentException("Lower bound cannot be greater than the current upper bound (" + this.max + ").");
        }
        this.min = (int) lb;
    }

    /**
     * Sets the upper bound of the integer variable. This method is not implemented.
     *
     * @param ub The new upper bound of the variable.
     */
    @Override
    public void setUB(double ub) {
        if (ub < this.min) {
            throw new IllegalArgumentException("Upper bound cannot be smaller than the current lower bound (" + this.min + ").");
        }
        this.max = (int) ub;
    }

    /**
     * Sets the name of the integer variable.
     *
     * @param name The new name of the variable.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void accept(NumExprVisitor visitor) {
        visitor.visit(this);
    }


    public int getIndex() {
        return index;
    }
}