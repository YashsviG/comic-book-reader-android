package com.comicshack.comic;

public class ComicPage {

    private byte[]data;
    private long dataSize;
    private String filetype;




    public ComicPage(byte[] data, long size) {

        this.dataSize = size;
        this.data = data.clone();



        //determine what kind of file (probably jpg) and stuff during creation



    }
}
