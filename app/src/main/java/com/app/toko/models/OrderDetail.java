package com.app.toko.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail implements Serializable {
    Book book;
    BigDecimal quantity;
    BigDecimal price;
}

