package com.comicshack.view.ui.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.comicshack.model.ComicLibrary;
import com.example.comicshack.R;
import com.comicshack.model.dao.ComicDao;
import com.comicshack.model.database.ComicShackDatabase;
import com.example.comicshack.databinding.FragmentEditBinding;
import com.comicshack.model.entities.Comic;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditFragment extends Fragment {
    private FragmentEditBinding binding;
    private ComicDao comicDao;
    private ComicShackDatabase db;
    private CompositeDisposable disposable;
    private Comic comic;

    public static EditFragment newInstance() {
        return new EditFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = ComicLibrary.getDb(getActivity().getApplicationContext());
        comicDao = db.comicDao();
        disposable = new CompositeDisposable();

        EditText nameEdit = binding.editName;
        EditText authorEdit = binding.editAuthor;
        EditText seriesEdit = binding.editSeries;
        EditText yearEdit = binding.editYear;

        Bundle bundle = getArguments();

        int comicID = bundle.getInt(getString(R.string.comic_id));

        comic = ComicLibrary.getLibrary().stream().filter(c -> c.getId() == comicID).findFirst().get();

        nameEdit.setText(comic.getName());
        authorEdit.setText(comic.getAuthor());
        seriesEdit.setText(comic.getSeries());
        yearEdit.setText(Integer.toString(comic.getReleaseYear()));

        Button editComicButton = binding.editComicButton;
        editComicButton.setOnClickListener(v -> editComic(v));

        return root;
    }

    public void editComic(View view) {
        Button editComicButton = (Button) binding.editComicButton;
        editComicButton.setEnabled(false);

        String name = binding.editName.getText().toString();
        if (!isValidString(name))
        {
            Toast.makeText(getContext(), "Please enter a valid name.", Toast.LENGTH_SHORT).show();
            editComicButton.setEnabled(true);
            return;
        }

        String author = binding.editAuthor.getText().toString();
        if (!isValidString(author))
        {
            Toast.makeText(getContext(), "Please enter a valid author.", Toast.LENGTH_SHORT).show();
            editComicButton.setEnabled(true);
            return;
        }

        String series = binding.editSeries.getText().toString();
        if (!isValidString(series))
        {
            Toast.makeText(getContext(), "Please enter a valid series.", Toast.LENGTH_SHORT).show();
            editComicButton.setEnabled(true);
            return;
        }

        String year = binding.editYear.getText().toString();
        if (!isValidString(series))
        {
            Toast.makeText(getContext(), "Please enter a valid year.", Toast.LENGTH_SHORT).show();
            editComicButton.setEnabled(true);
            return;
        }

        Integer yearInt = null;
        try {
            yearInt = Integer.parseInt(year);
        }
        catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid year.", Toast.LENGTH_SHORT).show();
            editComicButton.setEnabled(true);
            return;
        }

        comic.setAuthor(author);
        comic.setName(name);
        comic.setSeries(series);
        comic.setReleaseYear(yearInt);

        disposable.add(comicDao.UpdateComics(comic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> onComicEdited()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onComicEdited() {
        Button editComicButton = binding.editComicButton;
        editComicButton.setEnabled(true);
        Toast.makeText(getContext(), "Comic Edited!", Toast.LENGTH_SHORT).show();

        NavHostFragment.findNavController(this).navigateUp();
    }

    private boolean isValidString(String input) {
        if (input == null || input.isEmpty() || input.trim().length() == 0) {
            return false;
        }

        return true;
    }
}