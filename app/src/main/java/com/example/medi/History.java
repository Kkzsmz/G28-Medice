package com.example.medi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class History extends AppCompatActivity {

    private TextView medicationNameTextView;
    private TextView dosageTextView;
    private TextView frequencyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);

        medicationNameTextView = findViewById(R.id.medication_name);
        dosageTextView = findViewById(R.id.dosage);
        frequencyTextView = findViewById(R.id.frequency);

        String medicationName = "Ibuprofen";
        String dosage = "400mg";
        String frequency = "Every 6 hours";

        medicationNameTextView.setText("Medication Name: " + medicationName);
        dosageTextView.setText("Dosage: " + dosage);
        frequencyTextView.setText("Frequency: " + frequency);

        Button shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Medication Name: " + medicationName + "\nDosage: " + dosage + "\nFrequency: " + frequency);
                startActivity(Intent.createChooser(intent, "Share via"));
            }
        });
    }
}