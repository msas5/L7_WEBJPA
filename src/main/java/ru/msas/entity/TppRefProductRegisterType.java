package ru.msas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tpp_ref_product_register_type")
public class TppRefProductRegisterType {
    @Id
    @Column(name="internal_id")
    Long id;

    public String getValue() {
        return value;
    }

    @Column(name="value")
    String value;

    @Column(name="product_class_code")
    String productClassCode;

    @Column(name="account_type")
    String accountType;
}
