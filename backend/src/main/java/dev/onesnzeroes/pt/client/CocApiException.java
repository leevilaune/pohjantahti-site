package dev.onesnzeroes.pt.client;

/** The Clash of Clans API could not be reached or answered with an error. */
public class CocApiException extends RuntimeException {

    private final int status;

    public CocApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    public CocApiException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    /** HTTP status returned by the API, or 0 if it could not be reached. */
    public int getStatus() {
        return status;
    }
}
