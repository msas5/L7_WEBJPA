package ru.msas.exceptions;

public class ProcessTppProductRegisterException extends RuntimeException{

   public ProcessTppProductRegisterException(String errorMessage){
      super(errorMessage);
   }
}
