package com.example.comicshack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.comicshack.entities.Comic;
import com.example.comicshack.tools.FileArchive;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadComicActivity extends AppCompatActivity {
    FileArchive archive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comic);

        List<Comic> comicList = ComicLibrary.getLibrary();

        Intent intent = getIntent();
        Comic comic = comicList.get(intent.getIntExtra("index", 0));

        archive = new FileArchive(new File(comic.getFileLocation()));
        setComicPage();
    }

    public void onRightClick(View view) {
        int currentEntry = archive.getCurrentEntryIndex();
        //if (currentEntry < ar)
        archive.setCurrentEntryIndex(currentEntry + 1);
        setComicPage();
    }

    public void onLeftClick(View view) {
        int currentEntry = archive.getCurrentEntryIndex();
        if (currentEntry > 0) {
            archive.setCurrentEntryIndex(currentEntry - 1);
            setComicPage();
        }
    }

    private void setComicPage()
    {
        try {
            ComicPage page = archive.getCurrentEntry();

            Bitmap bmp = BitmapFactory.decodeByteArray(page.getData(), 0, (int) page.getDataSize());
            PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
            photoView.setImageBitmap(bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
