package ru.game.frankenstein;

/**
 * Base exception class
 */
public class FrankensteinException extends Exception
{
    public FrankensteinException(String message) {
        super(message);
    }

    public FrankensteinException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrankensteinException(Throwable cause) {
        super(cause);
    }

    public FrankensteinException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
