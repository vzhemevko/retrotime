package org.retrotime.util.exception;

public class TeamNotFoundException extends RuntimeException {


    public TeamNotFoundException(int teamId) {
        super("Content with id : " + teamId + " was not found.");

    }

    public TeamNotFoundException(String msg) {
        super(msg);
    }

    public TeamNotFoundException(String msg, Throwable th) {
        super(msg, th);
    }

}
