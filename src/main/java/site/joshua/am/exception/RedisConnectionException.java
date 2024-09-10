package site.joshua.am.exception;

public class RedisConnectionException extends RuntimeException {

    public RedisConnectionException(String message) {
        super(message);
    }

    public RedisConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
