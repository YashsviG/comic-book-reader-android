package com.example.comicshack;


/**
 * Class for containing everything needed to be displayed on a currently viewed comic page
 */
public class ComicPage {

    private byte[]data;
    private long dataSize;
    private String filetype;




    public ComicPage(byte[] data, long size, String filetype) {

        this.dataSize = size;
        this.data = data.clone();
        this.filetype = filetype;



        //determine what kind of file (probably jpg) and stuff during creation



    }

    public byte[] getData() {
        return data;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }
}
