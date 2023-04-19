package com.example.medi;


import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DrugInteractionssActivity extends AppCompatActivity {

    private EditText drug1EditText, drug2EditText;
    private TextView resultTextView;
    private Button checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drug1EditText = findViewById(R.id.drug1EditText);
        drug2EditText = findViewById(R.id.drug2EditText);
        resultTextView = findViewById(R.id.resultTextView);
        checkButton = findViewById(R.id.checkButton);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String drug1 = drug1EditText.getText().toString();
                String drug2 = drug2EditText.getText().toString();

                checkDrugInteractions(drug1, drug2);
            }
        });
    }

    private void checkDrugInteractions(String drug1, String drug2) {
        String url = "https://api.drugbank.com/v1/drug_names?region=ca,us&q=abacavir" + drug1 + "+" + drug2;

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL urlObject = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    return stringBuilder.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if (result == null) {
                    resultTextView.setText("Error occurred while checking drug interactions.");
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        JSONArray interactions = jsonObject.getJSONArray("fullInteractionTypeGroup")
                                .getJSONObject(0)
                                .getJSONArray("fullInteractionType");

                        if (interactions.length() > 0) {
                            String resultText = "These drugs have the following interactions:\n";
                            for (int i = 0; i < interactions.length(); i++) {
                                JSONObject interaction = interactions.getJSONObject(i);
                                String description = interaction.getString("interactionPair")
                                        .replace("[", "")
                                        .replace("]", "")
                                        .replace("{", "")
                                        .replace("}", "")
                                        .replace(",", "\n");
                                resultText += "- " + description + "\n";
                            }
                            resultTextView.setText(resultText);
                        } else {
                            resultTextView.setText("These drugs do not have any interactions.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        resultTextView.setText("Error occurred while parsing drug interaction data.");
                    }
                }
            }
        }.execute();
    }
}




