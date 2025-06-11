package nl.jessenagel.orchestrate;

/**
 * Utility class for managing counters.
 */
public class OrchCounter {
    private static int varCounter = 0;
    private static int constraintCounter = 0;
    private static int objCounter = 0;

    /**
     * Retrieves the next value of the global counter and increments it.
     *
     * @return The next value of the counter.
     */
    public static synchronized int getNextVarCounter() {
        return varCounter++;
    }

    /**
     * Retrieves the current value of the global counter without incrementing it.
     *
     * @return The current value of the counter.
     */
    public static synchronized int getCurrentVarCounter() {
        return varCounter;
    }

    /**
     * Retrieves the next value of the constraint counter and increments it.
     *
     * @return The next value of the constraint counter.
     */
    public static synchronized int getNextConstraintCounter() {
        return constraintCounter++;
    }

    /**
     * Retrieves the current value of the constraint counter without incrementing it.
     *
     * @return The current value of the constraint counter.
     */
    public static synchronized int getCurrentConstraintCounter() {
        return constraintCounter;
    }

    /**
     * Retrieves the next value of the objective counter and increments it.
     *
     * @return The next value of the objective counter.
     */
    public static synchronized int getNextObjCounter() {
        return objCounter++;
    }

    /**
     * Retrieves the current value of the objective counter without incrementing it.
     *
     * @return The current value of the objective counter.
     */
    public static synchronized int getCurrentObjCounter() {
        return objCounter;
    }
}
