package com.comicshack.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.comicshack.model.entities.Bookmark;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface BookmarkDao {

    @Insert
    public Completable InsertBookmarks(Bookmark... bookmarks);

    @Update
    public Completable UpdateBookmarks(Bookmark... bookmarks);

    @Delete
    public Completable DeleteBookmarks(Bookmark... bookmarks);
}
