package com.app.toko.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {
    private UUID id;

    private String title;


    private String language;


    private String description;


    private String edition;


    private BigDecimal price;


    private BigDecimal cost;


    private int quantity;

    private String publishcationDate;

    private String authors;

    private Category category;

    private String publisher;

    private Set<Album> albums;
}
