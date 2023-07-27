package com.app.toko.models;

import java.io.Serializable;

public class Album implements Serializable {
    private AlbumId id;

    private Book book;

    private boolean isPresentation;
}
