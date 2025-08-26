package kr.co.shortenurlservice.domain;

public class NotFoundShortenUrlException extends RuntimeException {

    public NotFoundShortenUrlException() {
    }

    public NotFoundShortenUrlException(String message) {
        super(message);
    }

    public NotFoundShortenUrlException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundShortenUrlException(Throwable cause) {
        super(cause);
    }
}
