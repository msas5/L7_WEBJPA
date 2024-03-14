package ru.msas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="account_pool")
public class AccountPool {


    public Long getId() {
        return id;
    }

    @Id
    @Column(name="id")
    Long id;

    @Column(name="branch_code")
    String branchCode;
    @Column(name="currency_code")
    String currencyCode;
    @Column(name="mdm_code")
    String mdmCode;
    @Column(name="priority_code")
    String priorityCode;
    @Column(name="registry_type_code")
    String registryTypeCode;
}
