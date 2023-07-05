package com.app.toko.payload.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumResponse implements Serializable {
    @SerializedName("imageSource")
    @Expose
    private String imageSource;
    @SerializedName("presentation")
    @Expose
    private boolean isPresentation;
}