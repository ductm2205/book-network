package se2.BookNetwork.exceptions;

public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException(String msg) {
        super(msg);
    }
}