package ru.msas.enums;

public enum AccountBodyFields {

    instanceId(true, false),
    registryTypeCode(true, false),
    accountType(false, false),
    currencyCode(true, false),
    branchCode(true, false),
    priorityCode(true, false),
    mdmCode(true, false),
    clientCode(false, false),
    trainRegion(false, false),
    counter(false, false),
    salesCode(false, false);

    public Boolean isRequired;
    public Boolean isBody;

    AccountBodyFields(Boolean isRequired, Boolean isBody){
        this.isBody  = isBody;
        this.isRequired = isRequired;
    }
}