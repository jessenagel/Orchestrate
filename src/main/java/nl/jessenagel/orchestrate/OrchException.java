package nl.jessenagel.orchestrate;

    /**
     * Represents a custom exception for the Orchestrate Java library.
     * This exception extends the {@link RuntimeException} class and provides
     * constructors for various use cases, such as specifying a message, a cause,
     * or both.
     */
    public class OrchException extends RuntimeException {

        /**
         * Constructs a new OrchException with no detail message or cause.
         */
        public OrchException() {
            super();
        }

        /**
         * Constructs a new OrchException with the specified detail message.
         *
         * @param s The detail message.
         */
        public OrchException(String s) {
            super(s);
        }

        /**
         * Constructs a new OrchException with the specified detail message and cause.
         *
         * @param message The detail message.
         * @param cause   The cause of the exception.
         */
        public OrchException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * Constructs a new OrchException with the specified cause.
         *
         * @param cause The cause of the exception.
         */
        public OrchException(Throwable cause) {
            super(cause);
        }
    }
