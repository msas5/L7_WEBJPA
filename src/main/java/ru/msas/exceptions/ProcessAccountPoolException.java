package ru.msas.exceptions;

public class ProcessAccountPoolException extends RuntimeException{
    public ProcessAccountPoolException(String errorMessage) {
        super(errorMessage);
    }
}
