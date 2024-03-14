package ru.msas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tpp_ref_product_class")
public class TppRefProductClass {
    @Id
    @Column(name="internal_id")
    Long id;

    public String getValue() {
        return value;
    }

    @Column(name="value")
    String value;
}
