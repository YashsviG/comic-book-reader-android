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
    private ComicLibrary comicLibrary;
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
        comicLibrary = new ComicLibrary();

        EditText nameEdit = binding.editName;
        EditText authorEdit = binding.editAuthor;
        EditText seriesEdit = binding.editSeries;
        EditText yearEdit = binding.editYear;

        Bundle bundle = getArguments();

        int comicID = bundle.getInt(getString(R.string.comic_id));

        comic = comicLibrary.getLibrary().stream().filter(c -> c.getId() == comicID).findFirst().get();

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

        EditText nameEditText = binding.editName;
        EditText authorEditText = binding.editAuthor;
        EditText seriesEditText = binding.editSeries;
        EditText yearEditText = binding.editYear;

        comic.setAuthor(authorEditText.getText().toString());
        comic.setName(nameEditText.getText().toString());
        comic.setSeries(seriesEditText.getText().toString());
        comic.setReleaseYear(Integer.parseInt(yearEditText.getText().toString()));

        disposable.add(comicDao.UpdateComics(comic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> onComicEdited()));
    }

    private void onComicEdited() {
        Button editComicButton = binding.editComicButton;
        editComicButton.setEnabled(true);
        Toast.makeText(getContext(), "Comic Edited!", Toast.LENGTH_SHORT).show();

        NavHostFragment.findNavController(this).navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}