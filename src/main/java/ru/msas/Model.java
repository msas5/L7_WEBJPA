package ru.msas;

import ru.msas.entity.TppProductRegister;

import java.util.ArrayList;
import java.util.Map;

public class Model {

    public void setError(Boolean error) {
        isError = error;
    }

    public Boolean getError() {
        return isError;
    }

    private Boolean isError;

    public Map<String, Object> getrMessage() {
        return rMessage;
    }

    public void setrMessage(Map<String, Object> rMessage) {
        this.rMessage = rMessage;
    }

    private Map<String,Object> rMessage;

    public void setAlArrangement(ArrayList<Map<String, Object>> alArrangement) {
        this.alArrangement = alArrangement;
    }

    public ArrayList<Map<String, Object>> getAlArrangement() {
        return alArrangement;
    }

    ArrayList<Map<String, Object>> alArrangement;
    private Map<String,Object> rqMap;
    public Map<String, Object> getRqMap() {
        return rqMap;
    }

    public TppProductRegister getTppProductRegisterForSave() {
        return tppProductRegisterForSave;
    }

    public void setTppProductRegisterForSave(TppProductRegister tppProductRegisterForSave) {
        this.tppProductRegisterForSave = tppProductRegisterForSave;
    }

    private TppProductRegister tppProductRegisterForSave;

    public void setRqMap(Map<String, Object> rqMap) {
        this.rqMap = rqMap;
    }
}