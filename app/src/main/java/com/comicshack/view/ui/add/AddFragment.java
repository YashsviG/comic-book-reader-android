package com.comicshack.view.ui.add;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.comicshack.model.ComicLibrary;
import com.comicshack.model.dao.ComicDao;
import com.comicshack.model.database.ComicShackDatabase;
import com.example.comicshack.databinding.FragmentAddBinding;
import com.comicshack.model.entities.Comic;
import com.comicshack.presenter.tools.FileUtil;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ComicDao comicDao;
    private ComicShackDatabase db;
    private CompositeDisposable disposable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddViewModel addViewModel =
                new ViewModelProvider(this).get(AddViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        EditText filePathEditText = binding.editTextFilePath;
                        filePathEditText.setText((FileUtil.getPath(data.getData(), getActivity().getApplicationContext())));
                    }
                });
        db = ComicLibrary.getDb(getActivity().getApplicationContext());
        comicDao = db.comicDao();
        disposable = new CompositeDisposable();


        Button chooseFile = binding.chooseFile;
        chooseFile.setOnClickListener(v -> chooseFile(v));

        Button saveComicButton = binding.addComicToDb;
        saveComicButton.setOnClickListener(v -> saveComic(v));

        return root;
    }

    public void chooseFile(View view) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a comic");

        activityResultLauncher.launch(chooseFile);
    }

    public void saveComic(View view) {
        Button saveComicButton = binding.addComicToDb;
        saveComicButton.setEnabled(false);

        String name = binding.editTextName.getText().toString();
        if (!isValidString(name))
        {
            Toast.makeText(getContext(), "Please enter a valid name.", Toast.LENGTH_SHORT).show();
            saveComicButton.setEnabled(true);
            return;
        }

        String author = binding.editTextAuthor.getText().toString();
        if (!isValidString(author))
        {
            Toast.makeText(getContext(), "Please enter a valid author.", Toast.LENGTH_SHORT).show();
            saveComicButton.setEnabled(true);
            return;
        }

        String series = binding.editTextSeries.getText().toString();
        if (!isValidString(series))
        {
            Toast.makeText(getContext(), "Please enter a valid series.", Toast.LENGTH_SHORT).show();
            saveComicButton.setEnabled(true);
            return;
        }

        String year = binding.editTextYear.getText().toString();
        if (!isValidString(year))
        {
            Toast.makeText(getContext(), "Please enter a valid year.", Toast.LENGTH_SHORT).show();
            saveComicButton.setEnabled(true);
            return;

        }

        String filePath = binding.editTextFilePath.getText().toString();
        if (!isValidString(filePath))
        {
            Toast.makeText(getContext(), "Please enter a valid file.", Toast.LENGTH_SHORT).show();
            saveComicButton.setEnabled(true);
            return;
        }

        Integer yearInt = null;
        try {
            yearInt = Integer.parseInt(year);
        }
        catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid year.", Toast.LENGTH_SHORT).show();
            saveComicButton.setEnabled(true);
            return;
        }

        Comic comic = new Comic(name,
                author,
                series,
                yearInt,
                filePath,
                false);

        disposable.add(comicDao.InsertComics(comic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> onComicSaved()));


        ComicLibrary.getLibrary().add(comic);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onComicSaved() {
        EditText nameEditText = binding.editTextName;
        EditText authorEditText = binding.editTextAuthor;
        EditText seriesEditText = binding.editTextSeries;
        EditText yearEditText = binding.editTextYear;
        EditText filePathEditText = binding.editTextFilePath;

        nameEditText.setText("");
        authorEditText.setText("");
        seriesEditText.setText("");
        yearEditText.setText("");
        filePathEditText.setText("");

        Button saveComicButton = binding.addComicToDb;
        saveComicButton.setEnabled(true);
        Toast.makeText(getContext(), "Comic Saved!", Toast.LENGTH_SHORT).show();
    }


    private boolean isValidString(String input) {
        if (input == null || input.isEmpty() || input.trim().length() == 0) {
            return false;
        }

        return true;
    }

}
