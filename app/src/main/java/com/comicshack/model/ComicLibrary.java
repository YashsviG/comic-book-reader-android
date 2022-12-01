package com.comicshack.model;

import android.content.Context;

import androidx.room.Room;

import com.comicshack.model.database.ComicShackDatabase;
import com.comicshack.model.entities.Comic;

import java.util.ArrayList;
import java.util.List;


/**
 * Container class for comics. Should be used for browsing comics in the library section
 */
public class ComicLibrary {
    private static List<Comic> library;
    private static ComicShackDatabase db;
    private static List<String> directories; //list of directories imported into the library

    //should hold our pointer to database
    //should be able to rebuild itself from a database at init.

    private ComicLibrary() {
    }

    public static List<Comic> getLibrary() {
        if (library == null) {
            library = new ArrayList<Comic>();
        }

        return library;
    }

    public static ComicShackDatabase getDb(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, ComicShackDatabase.class, "comicshack_db").build();
        }

        return db;
    }

    public static List<String> getDirectories() {
        if (directories == null)
        {
            directories = new ArrayList<String>();
        }

        return directories;
    }

}
