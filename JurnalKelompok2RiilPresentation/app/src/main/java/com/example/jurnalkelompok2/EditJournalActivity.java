package com.example.jurnalkelompok2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class EditJournalActivity extends AppCompatActivity {

    private EditText editTitleEditText;
    private EditText editContentEditText;
    private Button saveEditButton;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journal);

        editTitleEditText = findViewById(R.id.editTitleEditText);
        editContentEditText = findViewById(R.id.editContentEditText);
        saveEditButton = findViewById(R.id.saveEditButton);

        fileName = getIntent().getStringExtra("fileName");

        loadJournalEntry(fileName);

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditedJournalEntry();
            }
        });
    }

    private void loadJournalEntry(String fileName) {
        File file = new File(getFilesDir(), fileName);
        try (FileInputStream fis = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder contentBuilder = new StringBuilder();
            boolean isFirstLine = true;
            String line;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    titleBuilder.append(line);
                    isFirstLine = false;
                } else {
                    contentBuilder.append(line).append("\n");
                }
            }
            editTitleEditText.setText(titleBuilder.toString());
            editContentEditText.setText(contentBuilder.toString().trim());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("EditJournalActivity", "Error loading journal entry: " + e.getMessage());
            Toast.makeText(this, "Error loading journal entry", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEditedJournalEntry() {
        String newTitle = editTitleEditText.getText().toString().trim();
        String newContent = editContentEditText.getText().toString().trim();

        if (newTitle.isEmpty() || newContent.isEmpty()) {
            Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(getFilesDir(), fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {
            writer.write(newTitle + "\n" + newContent);
            Toast.makeText(this, "Journal entry updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("EditJournalActivity", "Error saving journal entry: " + e.getMessage());
            Toast.makeText(this, "Error saving journal entry", Toast.LENGTH_SHORT).show();
        }
    }
}
