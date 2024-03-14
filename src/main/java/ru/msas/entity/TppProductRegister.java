package ru.msas.entity;

import jakarta.persistence.*;
import java.util.Map;

@Entity
@Table(name="tpp_product_register")
public class TppProductRegister {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="product_id")
    Long productId;

    public void setAccount(Long account) {
        this.account = account;
    }

    @Column(name="account")
    Long account;

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Column(name="account_number")
    String accountNumber;
    public void setType(String type) {
        this.type = type;
    }

    @Column(name="type")
    String type;

    @Column(name="currency_code")
    String currencyCode;

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name="state")
    String state;

    TppProductRegister(){}
    public TppProductRegister(Map<String, Object> rqMap){
        String sTr = (String) rqMap.get("instanceId");
        this.productId = Long.parseLong(sTr);
        this.type = (String) rqMap.get("type");

    }
}