package com.example.comicshack;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.comicshack.model.dao.ComicDao;
import com.comicshack.model.database.ComicShackDatabase;
import com.comicshack.model.entities.Comic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private ComicDao comicDao;
    private ComicShackDatabase db;
    private CompositeDisposable disposable;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, ComicShackDatabase.class).build();
        comicDao = db.comicDao();
        disposable = new CompositeDisposable();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
        disposable.clear();
    }

    @Test
    public void writeComicAndReadInList() throws Exception {
        Comic comic = new Comic("My comic", "Joe", "Batman", 2010, "", false);
        comicDao.InsertComics(comic).blockingAwait();

        List<Comic> myComic = comicDao.getAllComics().blockingGet();
        assertEquals(myComic.get(0).getName(), "My comic");
    }
}
