package com.example.jurnalkelompok2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class JournalAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> journalTitles;
    private List<String> fileNames;

    public JournalAdapter(Context context, List<String> journalTitles, List<String> fileNames) {
        super(context, 0, journalTitles);
        this.context = context;
        this.journalTitles = journalTitles;
        this.fileNames = fileNames;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_journal, parent, false);
        }

        TextView journalTitle = convertView.findViewById(R.id.journalTitle);
        TextView journalDate = convertView.findViewById(R.id.journalDate);
        Button editButton = convertView.findViewById(R.id.editButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        String[] parts = journalTitles.get(position).split("\n");
        if (parts.length > 0) {
            journalTitle.setText(parts[0]);
            if (parts.length > 1) {
                journalDate.setText(parts[1]);
            }
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JournalAdapter", "Edit button clicked for position: " + position);
                Log.d("JournalAdapter", "File name: " + fileNames.get(position));
                Intent intent = new Intent(context, EditJournalActivity.class);
                intent.putExtra("fileName", fileNames.get(position));
                context.startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JournalAdapter", "Delete button clicked for position: " + position);
                deleteJournal(fileNames.get(position), position);
            }
        });

        return convertView;
    }

    private void deleteJournal(String fileName, int position) {
        File file = new File(context.getFilesDir(), fileName);
        if (file.exists() && file.delete()) {
            Toast.makeText(context, "Journal deleted successfully", Toast.LENGTH_SHORT).show();
            journalTitles.remove(position);
            fileNames.remove(position);
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "Error deleting journal", Toast.LENGTH_SHORT).show();
        }
    }
}
