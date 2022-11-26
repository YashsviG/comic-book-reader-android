package com.comicshack.model.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ComicWithBookmarks {
    @Embedded
    public Comic comic;

    @Relation(
            parentColumn = "rowid",
            entityColumn = "comicID"
    )

    public List<Bookmark> bookmarks;
}
