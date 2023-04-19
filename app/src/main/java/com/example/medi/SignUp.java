package com.example.medi;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private EditText signName, signPhone, signEmail, signPassword;
    private Button signButton;
    private TextView signRedirectText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        dbHelper = new DatabaseHelper(this);

        signName = findViewById(R.id.signup_name);
        signPhone = findViewById(R.id.signup_phone);
        signEmail = findViewById(R.id.signup_email);
        signPassword = findViewById(R.id.signup_password);
        signRedirectText = findViewById(R.id.signup_RedirectText);
        signButton = findViewById(R.id.signup_button);

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = signName.getText().toString().trim();
                String phone = signPhone.getText().toString().trim();
                String email = signEmail.getText().toString().trim();
                String password = signPassword.getText().toString().trim();

                ContentValues values = new ContentValues();

                // Validate input
                if (name.isEmpty()) {
                    signName.setError("Name cannot be empty!");
                    return;
                }
                if (phone.isEmpty()) {
                    signPhone.setError("Phone cannot be empty!");
                    return;
                }
                if (email.isEmpty()) {
                    signEmail.setError("Email cannot be empty!");
                    return;
                }
                if (password.isEmpty()) {
                    signPassword.setError("Password cannot be empty!");
                    return;
                }

                values.put(DatabaseHelper.ROW_NAME, name);
                values.put(DatabaseHelper.ROW_PHONE, phone);
                values.put(DatabaseHelper.ROW_EMAIL, email);
                values.put(DatabaseHelper.ROW_PASSWORD, password);
                dbHelper.insertData(values);


                Toast.makeText(SignUp.this, "SignUp Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });

        signRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }
}
