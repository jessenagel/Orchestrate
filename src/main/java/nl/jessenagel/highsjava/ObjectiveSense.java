package nl.jessenagel.highsjava;

/**
 * Represents the sense of an objective in the HiGHS Java library.
 * The objective sense can either be to minimize or maximize.
 * This class provides two predefined constants.
 */
public class ObjectiveSense {
    /**
     * Represents the objective sense to minimize.
     */
    public static final ObjectiveSense Minimize;

    /**
     * Represents the objective sense to maximize.
     */
    public static final ObjectiveSense Maximize;

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private ObjectiveSense() {
        // Prevent instantiation
    }

    static {
        // Initialize the Minimize constant with a custom toString implementation.
        Minimize = new ObjectiveSense() {
            @Override
            public String toString() {
                return "Minimize";
            }
        };

        // Initialize the Maximize constant with a custom toString implementation.
        Maximize = new ObjectiveSense() {
            @Override
            public String toString() {
                return "Maximize";
            }
        };
    }
}