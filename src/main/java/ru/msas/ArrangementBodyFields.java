package ru.msas;

public enum ArrangementBodyFields {
    generalAgreementId(false, true),
    supplementaryAgreementId(false, true),
    arrangementType(false, true),
    shedulerJobId(false, true),
    number(true, true),
    openingDate(true, true),
    closingDate(false, true),
    cancelDate(false, true),
    validityDuration(false, true),
    cancellationReason(false, true),
    Status(false, true),
    interestCalculationDate(false, true),
    interestRate(false, true),
    coefficient(false, true),
    coefficientAction(false, true),
    minimumInterestRate(false, true),
    minimumInterestRateCoefficient(false, true),
    minimumInterestRateCoefficientAction(false, true),
    maximalnterestRate(false, true),
    maximalnterestRateCoefficient(false, true),
    maximalnterestRateCoefficientAction(false, true);

    Boolean isRequired;
    Boolean isBody;


    ArrangementBodyFields(Boolean isRequired, Boolean isBody){
        this.isBody   = isBody;
        this.isRequired = isRequired;
    }

}
