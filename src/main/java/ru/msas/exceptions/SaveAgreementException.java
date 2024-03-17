package ru.msas.exceptions;

public class SaveAgreementException extends RuntimeException{

    public SaveAgreementException(String errorMessage){
        super(errorMessage);
    }
}
