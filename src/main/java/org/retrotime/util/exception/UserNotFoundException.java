package org.retrotime.util.exception;

public class UserNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 8917400374313334339L;

    public UserNotFoundException(int userId) {
        super("User with id : " + userId + " was not found.");

    }

    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException(String msg, Throwable th) {
        super(msg, th);
    }


}
