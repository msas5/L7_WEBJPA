package ru.msas.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Map;


@Entity
@Table(name="tpp_product")
public class TppProduct {
    public String getId() {
        return id.toString();
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //IDENTITY,AUTO, SEQUENCE, TABLE
    Long id;

    @Column(name="number")
    String number;

    String type;

    @Column(name="date_of_conclusion")
    Date contractDate;

    /*


    (true, true),
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
    Boolean isBody;    *
    * */

    public TppProduct(){}

    public TppProduct(Map<String, Object> rqMap){
       this.number = (String) rqMap.get("contractNumber");
       this.type = (String) rqMap.get("productType");
       //this.contractDate = (Date)rqMap.get("contractDate");
    }

}
