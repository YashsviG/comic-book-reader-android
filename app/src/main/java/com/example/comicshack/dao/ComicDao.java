package com.example.comicshack.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.comicshack.entities.Comic;
import com.example.comicshack.entities.ComicWithBookmarks;

import java.util.List;

@Dao
public interface ComicDao {
    @Insert
    public void InsertComics(Comic... comics);

    @Update
    public void UpdateComics(Comic... comics);

    @Delete
    public void DeleteComics(Comic... comics);

    @Query("SELECT *, rowid FROM comics")
    List<Comic> getAllComics();

    @Transaction
    @Query("SELECT *, rowid FROM comics")
    public List<ComicWithBookmarks> getComicsWithBookmarks();
}
