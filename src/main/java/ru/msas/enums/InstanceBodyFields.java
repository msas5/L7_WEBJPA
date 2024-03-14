package ru.msas.enums;

public enum InstanceBodyFields {
    instanceId(false, true),
    productType(true, true),
    productCode(true, true),
    registerType(true, true),
    mdmCode(true, true),
    contractNumber(true, true),
    contractDate(true, true),
    priority(true, true),
    interestRatePenalty(false, true),
    minimalBalance(false, true),
    thresholdAmount(false, true),
    accountingDetails(false, true),
    rateType(false, true),
    taxPercentageRate(false, true),
    technicalOverdraftLimitAmount(false, true),
    contractId(true, true),
    branchCode(true, true),
    isoCurrencyCode(true, true),
    urgencyCode(true, true),
    referenceCode(false, true),
    additionalPropertiesVip(false, true),
    arrangement (false, true);
    Boolean isRequired;
    Boolean isBody;

    InstanceBodyFields(Boolean isRequired, Boolean isBody){
        this.isRequired = isRequired;this.isBody = isBody;
    }
}