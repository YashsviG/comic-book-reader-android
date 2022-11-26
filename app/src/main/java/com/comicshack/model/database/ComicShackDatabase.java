package com.comicshack.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.comicshack.model.dao.BookmarkDao;
import com.comicshack.model.dao.ComicDao;
import com.comicshack.model.entities.Bookmark;
import com.comicshack.model.entities.Comic;

@Database(entities = {Comic.class, Bookmark.class}, version = 1)
public abstract class ComicShackDatabase extends RoomDatabase {
    public abstract ComicDao comicDao();

    public abstract BookmarkDao bookmarkDao();
}

