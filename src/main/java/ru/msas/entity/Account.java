package ru.msas.entity;

import jakarta.persistence.*;

@Entity
@Table(name="account")
public class Account {
    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="account_number")
    String accountNumber;

    @Column(name="account_pool_id")
    Long accountPoolId;

    @Column(name="bussy")
    Boolean bussy;

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }



    public String getAccountNumber() {
        return accountNumber;
    }



    public void setBusy(Boolean bussy){
        this.bussy = bussy;
    }
}
