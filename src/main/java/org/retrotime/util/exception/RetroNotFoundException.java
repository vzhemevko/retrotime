package org.retrotime.util.exception;

public class RetroNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -301546403441373610L;

    public RetroNotFoundException(int retroId) {
        super("Retro with id : " + retroId + " was not found.");
    }

    public RetroNotFoundException(String msg) {
        super(msg);
    }

    public RetroNotFoundException(String msg, Throwable th) {
        super(msg, th);
    }
}
