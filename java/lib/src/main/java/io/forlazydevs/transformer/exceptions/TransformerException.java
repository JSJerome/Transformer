package io.forlazydevs.transformer.exceptions;

public class TransformerException extends Exception {

    public TransformerException(String message) {
        super(message);
    }

    public TransformerException(String message, Throwable err) {
        super(message, err);
    }
    
}
