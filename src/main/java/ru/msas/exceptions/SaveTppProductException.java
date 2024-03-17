package ru.msas.exceptions;

public class SaveTppProductException extends RuntimeException{

    public SaveTppProductException(String errorMessage){
        super(errorMessage);
    }
}