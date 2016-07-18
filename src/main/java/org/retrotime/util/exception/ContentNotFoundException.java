package org.retrotime.util.exception;

public class ContentNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -1023589635948344660L;

    public ContentNotFoundException(int contentId) {
        super("Content with id : " + contentId + " was not found.");

    }

    public ContentNotFoundException(String msg) {
        super(msg);
    }

    public ContentNotFoundException(String msg, Throwable th) {
        super(msg, th);
    }

}
