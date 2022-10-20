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

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ComicDao {
    @Insert
    public Completable InsertComics(Comic... comics);

    @Update
    public Completable UpdateComics(Comic... comics);

    @Delete
    public Completable DeleteComics(Comic... comics);

    @Query("SELECT *, rowid FROM comics")
    Single<List<Comic>> getAllComics();

    @Transaction
    @Query("SELECT *, rowid FROM comics")
    public Single<List<ComicWithBookmarks>> getComicsWithBookmarks();
}
