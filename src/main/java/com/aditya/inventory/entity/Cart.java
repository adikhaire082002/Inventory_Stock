package com.aditya.inventory.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer cart_id;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> products = new ArrayList<>();

    private float price ;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
