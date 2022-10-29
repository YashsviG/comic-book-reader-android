package com.example.comicshack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ReadComic extends AppCompatActivity {
    private ViewPager2 comicViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comic);

        comicViewPager = findViewById(R.id.viewPagerReadComic);

        List<SliderItem> comicPages = new ArrayList<>();
    }
}