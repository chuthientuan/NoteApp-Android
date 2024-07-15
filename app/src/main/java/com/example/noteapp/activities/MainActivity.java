package com.example.noteapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.noteapp.R;
import com.example.noteapp.database.NotesDatabase;
import com.example.noteapp.entities.Note;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CREATE_NOTE = 1;
    private long pressBacktime;
    ImageView imgAddNote, imgAddNoteMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgAddNote = findViewById(R.id.imgAddNote);
        imgAddNoteMain = findViewById(R.id.imgAddNoteMain);
        imgAddNote.setOnClickListener(v -> startActivityForResult(
                new Intent(getApplicationContext(), CreateNoteActivity.class),
                REQUEST_CREATE_NOTE)
        );
        imgAddNoteMain.setOnClickListener(v -> startActivityForResult(new Intent(getApplicationContext(), CreateNoteActivity.class),
                REQUEST_CREATE_NOTE)
        );
        getNote();
    }

    private void getNote() {
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase.getDatabase(getApplicationContext()).noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                Log.d("MY_NOTES", notes.toString());
            }
        }
        new GetNotesTask().execute();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - pressBacktime < 2000) {
            super.onBackPressed();
            return;
        }
        else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressBacktime = System.currentTimeMillis();
    }
}