package com.comicshack.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.comicshack.model.dao.ComicDao;
import com.comicshack.model.database.ComicShackDatabase;
import com.comicshack.model.ComicLibrary;
import com.example.comicshack.R;
import com.example.comicshack.databinding.ActivityMainBinding;
import com.comicshack.model.entities.Comic;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private ComicDao comicDao;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavHostFragment fragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = fragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        ComicShackDatabase db = ComicLibrary.getDb(this.getApplicationContext());
        comicDao = db.comicDao();
        disposable = new CompositeDisposable();
        disposable.add(comicDao.getAllComics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Comic>>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(List<Comic> comics) {
                        List<Comic> comicList = ComicLibrary.getLibrary();
                        List<String> directoryList = ComicLibrary.getDirectories();
                        for (Comic comic : comics) {
                            comicList.add(comic);
                            directoryList.add(comic.getFileLocation());
                        }
                    }
                }));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                //todo when permission is granted
            } else {
                //request for the permission
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // add menu items
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
    }

    // menu item select listener
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ViewPager2 viewPager2 = findViewById(R.id.viewPagerImageSlider);
        SliderAdapter adapter = (SliderAdapter) viewPager2.getAdapter();
        List<SliderItem> sliderItems = adapter.getSliderItems();
        SliderItem sliderItem = sliderItems.get(viewPager2.getCurrentItem());
        int comicID = sliderItem.getComicID();

        if (item.getTitle() == "Edit") {

            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.comic_id), comicID);

            navController.navigate(R.id.navigation_edit, bundle);
        } else if (item.getTitle() == "Delete") {
            Comic comicToDelete = ComicLibrary.getLibrary().stream().filter(c -> c.getId() == comicID).findFirst().get();
            disposable.add(comicDao.DeleteComics(comicToDelete)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe());

            ComicLibrary.getLibrary().removeIf(c -> c.getId() == comicID);

            sliderItems.removeIf(c -> c.getComicID() == comicID);
            viewPager2.setAdapter(adapter);
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment_activity_main).navigateUp()
                || super.onSupportNavigateUp();
    }
}