package com.comicshack.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks")
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int comicID;

    private int location;

    public Bookmark(int id, int comicID, int location) {
        this.id = id;
        this.comicID = comicID;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComicID() {
        return comicID;
    }

    public void setComicID(int comicID) {
        this.comicID = comicID;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
