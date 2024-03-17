package ru.msas;

import ru.msas.entity.Agreement;
import ru.msas.entity.TppProductRegister;
import ru.msas.entity.TppRefProductRegisterType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Model {

    public void setError(Boolean error) {
        isError = error;
    }

    public Boolean getError() {
        return isError;
    }

    private Boolean isError;


    public void setInstanceExists(Boolean instanceExists) {
        isInstanceExists = instanceExists;
    }

    public Boolean getInstanceExists() {
        return isInstanceExists;
    }

    private Boolean isInstanceExists;

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


    public List<TppRefProductRegisterType> getlProductRegistertType() {
        return lProductRegistertType;
    }

    public void setlProductRegistertType(List<TppRefProductRegisterType> lProductRegistertType) {
        this.lProductRegistertType = lProductRegistertType;
    }

    private List<TppRefProductRegisterType> lProductRegistertType;
    private ArrayList<Map<String, Object>> alArrangement;
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

    public Agreement getAgreementForSave() {
        return agreementForSave;
    }

    public void setAgreementForSave(Agreement agreementForSave) {
        this.agreementForSave = agreementForSave;
    }

    private Agreement agreementForSave;

    public void setRqMap(Map<String, Object> rqMap) {
        this.rqMap = rqMap;
    }
}