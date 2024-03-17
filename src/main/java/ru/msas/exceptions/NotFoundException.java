package ru.msas.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String errorMessage){
        super(errorMessage);
    }
}
