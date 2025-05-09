package nl.jessenagel.highsjava;

/**
 * Represents a boolean variable in the HiGHS model.
 * A boolean variable is a specialized integer variable with bounds [0, 1].
 */
public class HiGHSBoolVar extends HiGHSIntVar {
    /**
     * The type of the variable, which is set to boolean.
     */
    NumVarType type = NumVarType.Bool;

    /**
     * Constructs a new HiGHSBoolVar with default bounds [0, 1] and a generated name.
     */
    public HiGHSBoolVar() {
        this.name = "BoolVar_" + HiGHSCounter.getNextVarCounter();
        this.max = 1;
        this.min = 0;
    }

}