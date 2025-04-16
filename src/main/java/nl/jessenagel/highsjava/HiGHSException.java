package nl.jessenagel.highsjava;

public class HiGHSException extends RuntimeException{
    public HiGHSException() {
        super();
    }
    public HiGHSException(String s) {
        super(s);
    }
    public HiGHSException(String message, Throwable cause) {
        super(message, cause);
    }
    public HiGHSException(Throwable cause) {
        super(cause);
    }
}
