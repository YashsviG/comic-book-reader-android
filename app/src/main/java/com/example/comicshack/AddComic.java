package com.example.comicshack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.comicshack.dao.ComicDao;
import com.example.comicshack.database.ComicShackDatabase;
import com.example.comicshack.entities.Comic;
import com.example.comicshack.tools.FileUtil;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddComic extends AppCompatActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ComicDao comicDao;
    private ComicShackDatabase db;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comic);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        EditText filePathEditText = findViewById(R.id.editTextFilePath);
                        filePathEditText.setText((FileUtil.getPath(data.getData(), getApplicationContext())));
                    }
                });

        db = ComicLibrary.getDb(getApplicationContext());
        comicDao = db.comicDao();
        disposable = new CompositeDisposable();
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
        Button saveComicButton = (Button) findViewById(R.id.add_comic_to_db);
        saveComicButton.setEnabled(false);

        EditText nameEditText = findViewById(R.id.editTextName);
        EditText authorEditText = findViewById(R.id.editTextAuthor);
        EditText seriesEditText = findViewById(R.id.editTextSeries);
        EditText yearEditText = findViewById(R.id.editTextYear);
        EditText filePathEditText = findViewById(R.id.editTextFilePath);

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
        EditText nameEditText = findViewById(R.id.editTextName);
        EditText authorEditText = findViewById(R.id.editTextAuthor);
        EditText seriesEditText = findViewById(R.id.editTextSeries);
        EditText yearEditText = findViewById(R.id.editTextYear);
        EditText filePathEditText = findViewById(R.id.editTextFilePath);

        nameEditText.setText("");
        authorEditText.setText("");
        seriesEditText.setText("");
        yearEditText.setText("");
        filePathEditText.setText("");

        Button saveComicButton = findViewById(R.id.add_comic_to_db);
        saveComicButton.setEnabled(true);
        Toast.makeText(this, "Comic Saved!", Toast.LENGTH_SHORT).show();

        finish();
    }
}