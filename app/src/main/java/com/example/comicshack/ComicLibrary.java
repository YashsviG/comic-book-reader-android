package com.example.comicshack;

import android.content.Context;

import androidx.room.Room;

import com.example.comicshack.database.ComicShackDatabase;
import com.example.comicshack.entities.Comic;

import java.util.ArrayList;
import java.util.List;


/**
 * Container class for comics. Should be used for browsing comics in the library section
 */
public class ComicLibrary {
    private static List<Comic> library;
    private List<String> directories; //list of directories imported into the library
    private static ComicShackDatabase db;

    //should hold our pointer to database
    //should be able to rebuild itself from a database at init.

    public ComicLibrary() {
        directories = new ArrayList<String>();
    }

    public static List<Comic> getLibrary() {
        if (library == null)
        {
            library = new ArrayList<Comic>();
        }

        return library;
    }

    public List<String> getDirectories() {
        return directories;
    }

    public static ComicShackDatabase getDb(Context context)
    {
        if (db == null)
        {
            db = Room.databaseBuilder(context, ComicShackDatabase.class, "comicshack_db").build();
        }

        return db;
    }

}
