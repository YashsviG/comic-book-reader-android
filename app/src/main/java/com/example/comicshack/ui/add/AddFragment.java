package com.example.comicshack.ui.add;

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

import com.example.comicshack.ComicLibrary;
import com.example.comicshack.dao.ComicDao;
import com.example.comicshack.database.ComicShackDatabase;
import com.example.comicshack.databinding.FragmentAddBinding;
import com.example.comicshack.entities.Comic;
import com.example.comicshack.tools.FileUtil;

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


        Button chooseFile = (Button) binding.chooseFile;
        chooseFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                chooseFile(v);
            }
        });

        Button saveComicButton = (Button) binding.addComicToDb;
        saveComicButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveComic(v);
            }
        });

        return root;
    }

    public void chooseFile(View view)
    {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a comic");

        activityResultLauncher.launch(chooseFile);
    }
    public void saveComic(View view)
    {
        Button saveComicButton = (Button) binding.addComicToDb;
        saveComicButton.setEnabled(false);

        EditText nameEditText = binding.editTextName;
        EditText authorEditText = binding.editTextAuthor;
        EditText seriesEditText = binding.editTextSeries;
        EditText yearEditText = binding.editTextYear;
        EditText filePathEditText = binding.editTextFilePath;

        Comic comic = new Comic(nameEditText.getText().toString(),
                authorEditText.getText().toString(),
                seriesEditText.getText().toString(),
                Integer.parseInt(yearEditText.getText().toString()),
                filePathEditText.getText().toString(),
                false);

        disposable.add(comicDao.InsertComics(comic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> onComicSaved()));


        ComicLibrary.getLibrary().add(comic);
    }

    private void onComicSaved()
    {
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
