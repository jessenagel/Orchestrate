package nl.jessenagel.highsjava;

import java.util.Objects;

/**
 * Represents a numerical variable in the HiGHS model.
 * A numerical variable has a name, lower bound, and upper bound.
 */
public class HiGHSNumVar implements NumVar {
    /**
     * The name of the numerical variable.
     */
    String name;

    /**
     * The lower bound of the numerical variable.
     */
    double lb;

    /**
     * The upper bound of the numerical variable.
     */
    double ub;

    /**
     * Constructs a new HiGHSNumVar with default bounds and a generated name.
     * The lower bound is set to 0, and the upper bound is set to Double.MAX_VALUE.
     */
    HiGHSNumVar() {
        this.name = "NumVar_" + HiGHSCounter.getNextVarCounter();
        this.lb = 0;
        this.ub = Double.MAX_VALUE;
    }

    /**
     * Gets the lower bound of the numerical variable.
     *
     * @return The lower bound of the variable.
     */
    @Override
    public double getLB() {
        return this.lb;
    }

    /**
     * Gets the upper bound of the numerical variable.
     *
     * @return The upper bound of the variable.
     */
    @Override
    public double getUB() {
        return this.ub;
    }

    /**
     * Gets the type of the numerical variable.
     *
     * @return The type of the variable, or null if not specified.
     */
    @Override
    public NumVarType getType() {
        return null;
    }

    /**
     * Gets the name of the numerical variable.
     *
     * @return The name of the variable.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the lower bound of the numerical variable.
     *
     * @param lb The new lower bound of the variable.
     */
    @Override
    public void setLB(double lb) {
        if (lb > this.ub) {
            throw new IllegalArgumentException("Lower bound cannot be greater than the current upper bound (" + this.ub + ").");
        }
        this.lb = lb;    }

    /**
     * Sets the upper bound of the numerical variable.
     *
     * @param ub The new upper bound of the variable.
     */
    @Override
    public void setUB(double ub) {
        if (ub < this.lb) {
            throw new IllegalArgumentException("Upper bound cannot be smaller than the current lower bound (" + this.lb + ").");
        }
        this.ub = ub;
    }

    /**
     * Sets the name of the numerical variable.
     *
     * @param name The new name of the variable.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void accept(NumExprVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NumVar numVar = (NumVar) obj;
        return Objects.equals(name, numVar.getName()); // Compare based on variable name
    }

    @Override
    public int hashCode() {
        return Objects.hash(name); // Generate hash based on variable name
    }
}
