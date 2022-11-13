package com.example.comicshack.ui.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.comicshack.ComicLibrary;
import com.example.comicshack.ComicPage;
import com.example.comicshack.SliderAdapter;
import com.example.comicshack.SliderItem;
import com.example.comicshack.dao.ComicDao;
import com.example.comicshack.database.ComicShackDatabase;
import com.example.comicshack.databinding.FragmentDashboardBinding;
import com.example.comicshack.entities.Comic;
import com.example.comicshack.tools.FileArchive;
import com.example.comicshack.ui.add.AddFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ViewPager2 viewPager2;
    private ComicDao comicDao;
    private ComicShackDatabase db;
    private CompositeDisposable disposable;
    private ComicLibrary comicLibrary;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewPager2 = binding.viewPagerImageSlider;


        db = ComicLibrary.getDb(getActivity().getApplicationContext());
        comicDao = db.comicDao();
        disposable = new CompositeDisposable();
        comicLibrary = new ComicLibrary();

        List<Comic> comicList = ComicLibrary.getLibrary();
        viewPager2 = binding.viewPagerImageSlider;

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

        return root;
    }

    public void goToAddComic(View view)
    {
        Intent intent = new Intent(getActivity(), AddFragment.class);
        startActivity(intent);
    }

    @Override
    public void onResume()
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}