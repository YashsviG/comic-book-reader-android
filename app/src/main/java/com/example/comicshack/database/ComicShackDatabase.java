package com.example.comicshack.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.comicshack.dao.BookmarkDao;
import com.example.comicshack.dao.ComicDao;
import com.example.comicshack.entities.Bookmark;
import com.example.comicshack.entities.Comic;

@Database(entities = {Comic.class, Bookmark.class}, version = 1)
public abstract class ComicShackDatabase extends RoomDatabase {
    public abstract ComicDao comicDao();

    public abstract BookmarkDao bookmarkDao();
}

