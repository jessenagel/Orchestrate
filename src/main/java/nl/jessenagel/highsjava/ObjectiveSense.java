package nl.jessenagel.highsjava;

public class ObjectiveSense {
    public static final ObjectiveSense Minimize;
    public static final ObjectiveSense Maximize;


    private ObjectiveSense() {
        // Prevent instantiation
    }

    static {
        Minimize = new ObjectiveSense() {
            @Override
            public String toString() {
                return "Minimize";
            }
        };

        Maximize = new ObjectiveSense() {
            @Override
            public String toString() {
                return "Maximize";
            }
        };

    }
}
