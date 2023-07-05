package com.app.toko.payload.response;

import android.os.Parcelable;

import com.app.toko.models.Album;
import com.app.toko.models.Category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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
public class BookResponse implements Serializable {

    private UUID id;


    private String title;


    private String language;


    private String description;


    private String edition;


    private BigDecimal price;


    private int quantity;


    private String publishcationDate;


    private String authors;


    private Category category;


    private String publisher;


    private Set<AlbumResponse> albums;
}
