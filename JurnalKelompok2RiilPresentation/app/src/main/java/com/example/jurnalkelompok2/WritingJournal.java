package com.example.jurnalkelompok2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WritingJournal extends AppCompatActivity {

    private EditText judul, isi;
    private Button saveButton, exitButton;
    private TextView time;
    private long selectedDate;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_jurnal);

        judul = findViewById(R.id.Title);
        isi = findViewById(R.id.content);
        saveButton = findViewById(R.id.saveButton);
        exitButton = findViewById(R.id.exitButton);
        time = findViewById(R.id.time);

        // Get the selected date from the intent
        selectedDate = getIntent().getLongExtra("selectedDate", System.currentTimeMillis());
        time.setText(formatDate(selectedDate));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String journalTitle = judul.getText().toString();
                String journalContent = isi.getText().toString();

                if (journalTitle.isEmpty() || journalContent.isEmpty()) {
                    Toast.makeText(WritingJournal.this, "Please fill both title and content", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String filename = generateUniqueFilename(journalTitle);

                    File file = new File(getApplicationContext().getFilesDir(), filename);
                    FileOutputStream fOutputStream = new FileOutputStream(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fOutputStream));

                    bufferedWriter.write(journalTitle + "\n" + journalContent);
                    bufferedWriter.close();
                    Toast.makeText(WritingJournal.this, "Journal saved successfully!", Toast.LENGTH_SHORT).show();
                    judul.setText("");
                    isi.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("WritingJournal", "Error saving journal data: " + e.getMessage());
                    Toast.makeText(WritingJournal.this, "Error saving journal!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity (WritingJournal)
            }
        });
    }

    private String formatDate(long dateInMillis) {
        Date date = new Date(dateInMillis);
        return DATE_FORMAT.format(date);
    }

    private String generateUniqueFilename(String title) {
        Date date = new Date(selectedDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String formattedDate = sdf.format(date);

        return String.format("%s_%s.txt", title.replaceAll("[^a-zA-Z0-9\\s]", ""), formattedDate);
    }
}
