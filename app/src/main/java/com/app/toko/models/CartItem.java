package com.app.toko.models;

import com.app.toko.payload.response.AlbumResponse;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.payload.response.CartResponse;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private String title;
    private BigDecimal price;
    private int quantity;
    private String imgSource;

    public static CartItem fromCartResponse(CartResponse cartResponse) {
        BookResponse bookResponse = cartResponse.getBookResponse();
        String imgSource = "";
        for (AlbumResponse album : bookResponse.getAlbums()) {
            imgSource = album.getImageSource();
            if (album.isPresentation()) {
                imgSource = album.getImageSource();
                break;
            }
        }

        return CartItem.builder()
                .title(bookResponse.getTitle())
                .price(bookResponse.getPrice())
                .quantity(cartResponse.getQuantity())
                .imgSource(imgSource)
                .build();
    }
}
