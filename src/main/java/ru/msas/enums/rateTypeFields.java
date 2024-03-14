package ru.msas.enums;

public enum rateTypeFields {
    DIFFERENCE(0),
    PROGRESSIVE(1);
    Integer Value;

    rateTypeFields(Integer Value){
        this.Value = Value;
    }
}
