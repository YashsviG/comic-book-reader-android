package com.example.comicshack;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import com.example.comicshack.dao.ComicDao;
import com.example.comicshack.database.ComicShackDatabase;
import com.example.comicshack.entities.Comic;
import com.example.comicshack.tools.FileArchive;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private ComicDao comicDao;
    private ComicShackDatabase db;
    private CompositeDisposable disposable;
    private ComicLibrary comicLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = ComicLibrary.getDb(getApplicationContext());
        comicDao = db.comicDao();
        disposable = new CompositeDisposable();
        comicLibrary = new ComicLibrary();

        disposable.add(comicDao.getAllComics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith( new DisposableSingleObserver<List<Comic>>(){
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(List<Comic> comics) {
                        List<Comic> comicList = ComicLibrary.getLibrary();
                        List<String> directoryList = comicLibrary.getDirectories();
                        for (Comic comic : comics)
                        {
                            comicList.add(comic);
                            directoryList.add(comic.getFileLocation());
                        }

                        viewPager2 = findViewById(R.id.viewPagerImageSlider);

                        List<SliderItem> sliderItems = new ArrayList<>();
                        FileArchive archive;
                        for (Comic comic : comicList)
                        {
                            archive = new FileArchive(new File(comic.getFileLocation()));
                            ComicPage cover = null;
                            try {
                                cover = archive.getCurrentZipEntry();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            
                            Bitmap bmp = BitmapFactory.decodeByteArray(cover.getData(), 0, (int)cover.getDataSize());
                            sliderItems.add(new SliderItem(bmp, comic.getId()));
                        }

                        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

                        viewPager2.setClipToPadding(false);
                        viewPager2.setClipChildren(false);
                        viewPager2.setOffscreenPageLimit(3);
                        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                            @Override
                            public void transformPage(@NonNull View page, float position) {
                                float r = 1 - Math.abs(position);
                                page.setScaleY(0.35f + r + 0.15f);
                            }
                        });

                        viewPager2.setPageTransformer(compositePageTransformer);
                    }
                }));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                //todo when permission is granted
            } else {
                //request for the permission
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }

    public void goToAddComic(View view)
    {
        Intent intent = new Intent(MainActivity.this, AddComic.class);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        updateLibrary();
    }

    private void updateLibrary()
    {
        if (viewPager2 != null)
        {
            SliderAdapter adapter = (SliderAdapter) viewPager2.getAdapter();

            List<Comic> comics = ComicLibrary.getLibrary();
            int comicCount = comics.size();

            List<SliderItem> items = adapter.getSliderItems();
            int adapterCount = items.size();

            int addedComics =  comicCount - adapterCount;
            if (addedComics > 0)
            {
                for (int i = adapterCount; i < comicCount; i ++)
                {
                    Comic comic = comics.get(i);
                    FileArchive archive = new FileArchive(new File(comic.getFileLocation()));
                    ComicPage cover = null;
                    try {
                        cover = archive.getCurrentZipEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bitmap bmp = BitmapFactory.decodeByteArray(cover.getData(), 0, (int)cover.getDataSize());
                    items.add(new SliderItem(bmp, comic.getId()));
                }

                viewPager2.setAdapter(adapter);
            }
        }
    }
}