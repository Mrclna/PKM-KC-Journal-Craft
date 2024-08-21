package com.example.jurnalkelompok2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewJournalActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> journalTitles;
    private ArrayList<String> fileNames;
    private JournalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journal);

        listView = findViewById(R.id.listView);
        journalTitles = new ArrayList<>();
        fileNames = new ArrayList<>();
        loadJournalFiles();

        adapter = new JournalAdapter(this, journalTitles, fileNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = fileNames.get(position);
                openJournal(fileName);
            }
        });
    }

    private void loadJournalFiles() {
        File directory = getFilesDir();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.contains("_")) {
                    String[] parts = fileName.split("_");
                    if (parts.length >= 2) {
                        String title = parts[0];
                        String datePart = parts[1];

                        try {
                            // Parse the date in YYYYMMDD format
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                            Date date = inputFormat.parse(datePart);

                            // Format the date in DD-MM-YYYY format
                            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            String formattedDate = outputFormat.format(date);

                            journalTitles.add(title + "\n" + formattedDate);
                            fileNames.add(fileName);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void openJournal(String fileName) {
        Intent intent = new Intent(this, EditJournalActivity.class);
        intent.putExtra("fileName", fileName);
        startActivity(intent);
    }
}
