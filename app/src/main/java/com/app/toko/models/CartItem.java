package com.app.toko.models;

import com.app.toko.payload.response.AlbumResponse;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.payload.response.CartResponse;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable {
    private String bookId;
    private String title;
    private BigDecimal price;
    private int cartQuantity;
    private int stockQuantity;
    private String imgSource;
    private boolean isChecked;

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(cartQuantity));
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


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
                .bookId(bookResponse.getId().toString())
                .title(bookResponse.getTitle())
                .price(bookResponse.getPrice())
                .cartQuantity(cartResponse.getQuantity())
                .stockQuantity(bookResponse.getQuantity())
                .imgSource(imgSource)
                .build();
    }
}
