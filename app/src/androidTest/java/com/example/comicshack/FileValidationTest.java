package com.example.comicshack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.comicshack.model.dao.ComicDao;
import com.comicshack.model.database.ComicShackDatabase;
import com.comicshack.model.entities.Comic;
import com.comicshack.presenter.tools.FileArchive;
import com.comicshack.model.ComicPage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;



@RunWith(AndroidJUnit4.class)
public class FileValidationTest {
    FileArchive fileArchive;
    String filePath = "/storage/emulated/14/Download/Seconds by Bryan Lee O'Malley (2014) (digital).cbz";
    String badPath = "badstring.txt";

    @Before
    public void createArchive(){

        fileArchive = new FileArchive(new File(filePath));
    }

    @Test
    public void archiveIllegalIndexTest() throws Exception{

        Exception except = assertThrows(IllegalArgumentException.class, () -> {
            fileArchive.setCurrentEntryIndex(-1);
            });

        Exception except2 = assertThrows(IllegalArgumentException.class, () -> {
            fileArchive.setCurrentEntryIndex(999999);
        });

        //should fail because our validation is bad

    }

    public void archiveIllegalFilenameTest() throws Exception {

        Exception except = assertThrows(IOException.class, () -> {
            FileArchive badArchive = new FileArchive(new File(badPath));
        });

    }

}
