package com.example.medi;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportPDF_Activity extends AppCompatActivity {

    private TextView medicationNameTextView;
    private TextView dosageTextView;
    private TextView frequencyTextView;

    private Button exportButton;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_pdf);

        medicationNameTextView = findViewById(R.id.medication_name);
        dosageTextView = findViewById(R.id.dosage);
        frequencyTextView = findViewById(R.id.frequency);

        String medicationName = "Ibuprofen";
        String dosage = "400mg";
        String frequency = "Every 6 hours";

        medicationNameTextView.setText("Medication Name: " + medicationName);
        dosageTextView.setText("Dosage: " + dosage);
        frequencyTextView.setText("Frequency: " + frequency);

        exportButton = findViewById(R.id.button_exportPDF);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generatePDF() {
        // Check for storage permission
        int permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            try {
                // Get external storage directory
                File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyApp");
                if (!pdfDir.exists()) {
                    pdfDir.mkdirs();
                }

                // Create PDF file
                File pdfFile = new File(pdfDir, "history.pdf");
                if (!pdfFile.exists()) {
                    try {
                        if (pdfFile.createNewFile()) {
                            Log.d("ExportPDF_Activity", "PDF file created successfully");
                        } else {
                            Log.d("ExportPDF_Activity", "Failed to create PDF file");
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("ExportPDF_Activity", "Failed to create PDF file");
                        return;
                    }
                }

                // Create document and writer
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

                // Open document and add content
                document.open();

                // Add title
                Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD);
                Paragraph title = new Paragraph("Medication History", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                // Add medication info
                Font infoFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
                Paragraph medicationInfo = new Paragraph("Medication Name: " + medicationNameTextView.getText().toString(), infoFont);
                medicationInfo.setAlignment(Element.ALIGN_LEFT);
                document.add(medicationInfo);

                Paragraph dosageInfo = new Paragraph("Dosage: " + dosageTextView.getText().toString(), infoFont);
                dosageInfo.setAlignment(Element.ALIGN_LEFT);
                document.add(dosageInfo);

                Paragraph frequencyInfo = new Paragraph("Frequency: " + frequencyTextView.getText().toString(), infoFont);
                frequencyInfo.setAlignment(Element.ALIGN_LEFT);
                document.add(frequencyInfo);

                // Close the document
                document.close();

                // Show a message to the user indicating that the PDF was saved successfully
                Toast.makeText(ExportPDF_Activity.this, "PDF saved to Documents directory", Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}