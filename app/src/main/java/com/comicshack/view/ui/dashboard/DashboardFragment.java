package com.comicshack.view.ui.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.comicshack.view.ui.add.AddFragment;
import com.comicshack.model.ComicLibrary;
import com.comicshack.model.ComicPage;
import com.comicshack.view.SliderAdapter;
import com.comicshack.view.SliderItem;
import com.comicshack.model.dao.ComicDao;
import com.comicshack.model.database.ComicShackDatabase;
import com.example.comicshack.databinding.FragmentDashboardBinding;
import com.comicshack.model.entities.Comic;
import com.comicshack.presenter.tools.FileArchive;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ViewPager2 viewPager2;
    private ComicDao comicDao;
    private ComicShackDatabase db;
    private CompositeDisposable disposable;

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

        List<Comic> comicList = ComicLibrary.getLibrary();

        List<SliderItem> sliderItems = new ArrayList<>();
        FileArchive archive;
        for (Comic comic : comicList) {
            archive = new FileArchive(new File(comic.getFileLocation()));
            ComicPage cover = null;
            try {
                cover = archive.getCurrentZipEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap bmp = BitmapFactory.decodeByteArray(cover.getData(), 0, (int) cover.getDataSize());
            sliderItems.add(new SliderItem(bmp, comic.getId()));
        }

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2, getActivity()));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        viewPager2.setPageTransformer(compositePageTransformer);

        return root;
    }

    public void goToAddComic(View view) {
        Intent intent = new Intent(getActivity(), AddFragment.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLibrary();
    }

    private void updateLibrary() {
        if (viewPager2 != null) {
            SliderAdapter adapter = (SliderAdapter) viewPager2.getAdapter();

            List<Comic> comics = ComicLibrary.getLibrary();
            int comicCount = comics.size();

            List<SliderItem> items = adapter.getSliderItems();
            int adapterCount = items.size();

            int addedComics = comicCount - adapterCount;
            if (addedComics > 0) {
                for (int i = adapterCount; i < comicCount; i++) {
                    Comic comic = comics.get(i);
                    FileArchive archive = new FileArchive(new File(comic.getFileLocation()));
                    ComicPage cover = null;
                    try {
                        cover = archive.getCurrentZipEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bitmap bmp = BitmapFactory.decodeByteArray(cover.getData(), 0, (int) cover.getDataSize());
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