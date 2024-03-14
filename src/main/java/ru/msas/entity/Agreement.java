package ru.msas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "agreement")
public class Agreement {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="number")
    String number;

    @Column(name="product_id")
    Long productId;


    public Agreement(Long instanceId){
        this.productId = instanceId;

    }
}
