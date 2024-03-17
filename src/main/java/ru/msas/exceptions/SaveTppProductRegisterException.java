package ru.msas.exceptions;

public class SaveTppProductRegisterException extends RuntimeException{

    public SaveTppProductRegisterException(String errorMessage){
        super(errorMessage);
    }
}
