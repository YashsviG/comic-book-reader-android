package com.example.comicshack;

import android.graphics.Bitmap;

public class SliderItem {
    private Bitmap image;
    private int comicID;

    public SliderItem(Bitmap image, int comicID) {
        this.image = image;
        this.comicID = comicID;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getComicID() {
        return comicID;
    }
}
