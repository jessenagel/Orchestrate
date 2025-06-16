package nl.jessenagel.orchestrate;

/**
 * Represents a boolean variable in the Orchestrate model.
 * A boolean variable is a specialized integer variable with bounds [0, 1].
 */
public class OrchBoolVar extends OrchIntVar {
    /**
     * The type of the variable, which is set to boolean.
     */
    NumVarType type = NumVarType.Bool;
    /**
     * Constructs a new OrchBoolVar with default bounds [0, 1] and a generated name.
     */
    public OrchBoolVar(int index) {
        super(index);
        this.name = "BoolVar_" + OrchCounter.getNextVarCounter();
        this.max = 1;
        this.min = 0;
        this.index = index;
    }


}