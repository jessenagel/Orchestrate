package nl.jessenagel.highsjava;

    /**
     * Represents a custom exception for the HiGHS Java library.
     * This exception extends the {@link RuntimeException} class and provides
     * constructors for various use cases, such as specifying a message, a cause,
     * or both.
     */
    public class HiGHSException extends RuntimeException {

        /**
         * Constructs a new HiGHSException with no detail message or cause.
         */
        public HiGHSException() {
            super();
        }

        /**
         * Constructs a new HiGHSException with the specified detail message.
         *
         * @param s The detail message.
         */
        public HiGHSException(String s) {
            super(s);
        }

        /**
         * Constructs a new HiGHSException with the specified detail message and cause.
         *
         * @param message The detail message.
         * @param cause   The cause of the exception.
         */
        public HiGHSException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * Constructs a new HiGHSException with the specified cause.
         *
         * @param cause The cause of the exception.
         */
        public HiGHSException(Throwable cause) {
            super(cause);
        }
    }
