package com.app.toko.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    UUID id;
    LocalDateTime date;
    BigDecimal totalPrice;
    Contact contact;
    List<OrderDetail> orderDetails;
}
