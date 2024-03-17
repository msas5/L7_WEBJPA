package ru.msas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ProcessExceptionModule {
    Map<String,Object> rMessage = new HashMap<String,Object>();
    @ExceptionHandler(SaveTppProductRegisterException.class)
    public ResponseEntity<ru.msas.Model> saveTppProductRegisterException(SaveTppProductRegisterException e){
        rMessage.put("Error",(Object) e.getMessage());
        return new ResponseEntity(rMessage,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProcessTppProductRegisterException.class)
    public ResponseEntity<ru.msas.Model> processTppProductRegisterException(ProcessTppProductRegisterException e){
        rMessage.put("Error",(Object) e.getMessage());
        return new ResponseEntity(rMessage,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ru.msas.Model> badRequestException(BadRequestException e){
        rMessage.put("Error",(Object) e.getMessage());
        return new ResponseEntity(rMessage,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ru.msas.Model> notFoundException(NotFoundException e){
        rMessage.put("Error",(Object) e.getMessage());
        return new ResponseEntity(rMessage,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProcessAccountPoolException.class)
    public ResponseEntity<ru.msas.Model> processAccountPoolException(ProcessAccountPoolException e){
        rMessage.put("Error",(Object) e.getMessage());
        return new ResponseEntity(rMessage,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaveAgreementException.class)
    public ResponseEntity<ru.msas.Model> saveAgreementException(SaveAgreementException e){
        rMessage.put("Error",(Object) e.getMessage());
        return new ResponseEntity(rMessage,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaveTppProductException.class)
    public ResponseEntity<ru.msas.Model> saveTppProductException(SaveTppProductException e){
        rMessage.put("Error",(Object) e.getMessage());
        return new ResponseEntity(rMessage,HttpStatus.BAD_REQUEST);
    }
}