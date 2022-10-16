package com.example.comicshack.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.comicshack.entities.Bookmark;
import com.example.comicshack.entities.Comic;

@Dao
public interface BookmarkDao {

    @Insert
    public void InsertBookmarks(Bookmark... bookmarks);

    @Update
    public void UpdateBookmarks(Bookmark... bookmarks);

    @Delete
    public void DeleteBookmarks(Bookmark... bookmarks);
}
