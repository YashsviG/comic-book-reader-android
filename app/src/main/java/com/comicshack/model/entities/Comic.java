package com.comicshack.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;


@Entity(tableName = "comics")
@Fts4
public class Comic {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    private int id;

    private String name;

    private String author;

    private String series;

    private int releaseYear;

    private int currentPage;

    private String fileLocation;

    private boolean isManga;


    public Comic(String name,
                 String author,
                 String series,
                 int releaseYear,
                 String fileLocation,
                 boolean isManga) {
        this.name = name;
        this.author = author;
        this.series = series;
        this.releaseYear = releaseYear;
        this.currentPage = 0;
        this.fileLocation = fileLocation;
        this.isManga = isManga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public boolean getIsManga() {
        return isManga;
    }

    public void setIsManga(boolean isManga) {
        this.isManga = isManga;
    }
}
