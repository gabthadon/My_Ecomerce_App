package com.example.my_ecomerce_app;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class Product {

    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            initialValue = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )

    @Id
    public Long id;
    public String name;
    public String description;
    public String image;
    public Integer rating;

}
