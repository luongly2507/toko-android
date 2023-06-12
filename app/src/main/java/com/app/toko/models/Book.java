package com.app.toko.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class Book {
    private UUID id;

    private String title;


    private String language;


    private String description;


    private String edition;


    private BigDecimal price;


    private BigDecimal cost;


    private int quantity;

    private LocalDate publishcationDate;

    private String authors;

    private Category category;

    private String publisher;

    private Set<Album> albums;
}
