package com.example.comicshack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.comicshack.model.dao.ComicDao;
import com.comicshack.model.database.ComicShackDatabase;
import com.comicshack.model.entities.Comic;
import com.comicshack.presenter.tools.FileArchive;
import com.comicshack.model.ComicPage;

import org.apache.tools.ant.types.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

@RunWith(AndroidJUnit4.class)
public class FileValidationTest {
    FileArchive fileArchive;
    String filePath = "/storage/emulated/0/Download/Seconds by Bryan Lee O'Malley (2014) (digital).cbz";
    String badPath = "badstring.txt";

    @Before
    public void createArchive(){

        fileArchive = new FileArchive(new File(filePath));
    }

    @Test
    public void archiveIllegalIndexTest() {

        try {
            fileArchive.setCurrentEntryIndex(-1);
        }
        catch(Exception e){
            assertTrue(false);
        }
        assertTrue(true);

        try {
            fileArchive.setCurrentEntryIndex(999999);
        }
        catch(Exception e){
            assertTrue(false);
        }
        assertTrue(true);

        //Negative tests to show we have no validation built in here

    }

    @Test
    public void archiveIllegalFilenameTest() throws Exception {

        try {
            new FileArchive(new File(badPath));
        }
        catch(Exception e){
            assertTrue(false);
        }
        assertTrue(true);


    }

}