package com.example.comicshack;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comicshack.dao.ComicDao;
import com.example.comicshack.database.ComicShackDatabase;
import com.example.comicshack.entities.Comic;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
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
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            EditText filePathEditText = (EditText)findViewById(R.id.editTextFilePath);
                            filePathEditText.setText((data.getData().getPath()));
                        }
                    }
                });

        db = Room.inMemoryDatabaseBuilder(getApplicationContext(), ComicShackDatabase.class).build();
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

        EditText nameEditText = (EditText)findViewById(R.id.editTextName);
        EditText authorEditText = (EditText)findViewById(R.id.editTextAuthor);
        EditText seriesEditText = (EditText)findViewById(R.id.editTextSeries);
        EditText yearEditText = (EditText)findViewById(R.id.editTextYear);
        EditText filePathEditText = (EditText)findViewById(R.id.editTextFilePath);

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
    }

    private void onComicSaved()
    {
        EditText nameEditText = (EditText)findViewById(R.id.editTextName);
        EditText authorEditText = (EditText)findViewById(R.id.editTextAuthor);
        EditText seriesEditText = (EditText)findViewById(R.id.editTextSeries);
        EditText yearEditText = (EditText)findViewById(R.id.editTextYear);
        EditText filePathEditText = (EditText)findViewById(R.id.editTextFilePath);

        nameEditText.setText("");
        authorEditText.setText("");
        seriesEditText.setText("");
        yearEditText.setText("");
        filePathEditText.setText("");

        Button saveComicButton = (Button) findViewById(R.id.add_comic_to_db);
        saveComicButton.setEnabled(true);
        Toast.makeText(this, "Comic Saved!", Toast.LENGTH_SHORT).show();
    }
}