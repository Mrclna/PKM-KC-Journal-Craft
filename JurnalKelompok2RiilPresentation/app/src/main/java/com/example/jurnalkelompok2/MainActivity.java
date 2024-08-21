package com.example.jurnalkelompok2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button addJournalButton;
    private long selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView2);
        addJournalButton = findViewById(R.id.jurnal);

        // Set initial selected date to current date
        selectedDate = calendarView.getDate();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Adjust month value (0-11 to 1-12)
                month++;
                // Store selected date as milliseconds since epoch
                selectedDate = new java.util.GregorianCalendar(year, month - 1, dayOfMonth).getTimeInMillis();
                Toast.makeText(MainActivity.this, "Selected date: " + dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });

        addJournalButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WritingJournal.class);
            intent.putExtra("selectedDate", selectedDate);
            startActivity(intent);
        });

        // Handle "View Journals" button click
        Button viewJournalsButton = findViewById(R.id.view_journals);
        viewJournalsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewJournalActivity.class);
            startActivity(intent);
        });
    }
}
