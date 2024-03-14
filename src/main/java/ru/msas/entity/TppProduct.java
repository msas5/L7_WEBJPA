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

    public TppProduct(){}

    public TppProduct(Map<String, Object> rqMap){
        this.number = (String) rqMap.get("contractNumber");
        this.type = (String) rqMap.get("productType");
    }
}
