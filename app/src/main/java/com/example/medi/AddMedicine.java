package com.example.medi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;


import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddMedicine extends AppCompatActivity {

    TextView edtNameEmpty, edtNicknameEmpty;
    @SuppressLint("StaticFieldLeak")
    private static TextView txtTime, txtDate;
    private static EditText edtName, edtNickname, edtHeight, edtNotes;
    private static Spinner notifsFrequency, metric;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private static Switch swtchNotifs;
    private static Button insertData, btnPickTime, btnPickDate;
    private static ImageButton imageButton;
    private static boolean notify, displayEmpty;
    private static ScrollView parent;
    private static int notifYear, notifMonth, notifDay, notifHour, notifMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();

        // notify
        swtchNotifs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!swtchNotifs.isChecked()) {
                    notify = false;
                    btnPickDate.setVisibility(View.GONE);
                    btnPickTime.setVisibility(View.GONE);
                    txtDate.setVisibility(View.GONE);
                    txtTime.setVisibility(View.GONE);
                    notifsFrequency.setVisibility(View.GONE);
                } else {
                    notify = true;
                    btnPickDate.setVisibility(View.VISIBLE);
                    btnPickTime.setVisibility(View.VISIBLE);
                    txtDate.setVisibility(View.VISIBLE);
                    txtTime.setVisibility(View.VISIBLE);
                    notifsFrequency.setVisibility(View.VISIBLE);
                }
            }
        });


        // submit button
        insertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNickname.getText().toString().equals("") && edtName.getText().toString().equals("")) {
                    Snackbar.make(parent, "Please give the medicine at least 1 name.", Snackbar.LENGTH_LONG)
                            .show();
                    displayEmpty = true;
                    emptyField();
                } else if (notify && txtDate.getText().equals("dd / mm / yyyy")) {
                    Snackbar.make(parent, "Please set a notification date.", Snackbar.LENGTH_LONG)
                            .show();
                } else if (notify && txtTime.getText().equals("hh : mm")) {
                    Snackbar.make(parent, "Please set a notification time.", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    insertData.setEnabled(false);
                    insertData();
                }
            }

        });

    }

    //pass value to the database
    private void insertData() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", edtName.getText().toString());
        map.put("otherNotes", edtNotes.getText().toString());
        map.put("notify", notify);
        if (notify) {
            map.put("notifYear", notifYear);
            map.put("notifMonth", notifMonth + 1); // the raw data is month number -1 (ie. april is 3)
            map.put("notifDay", notifDay);
            map.put("notifHour", notifHour);
            map.put("notifMinute", notifMinute);
            map.put("notifFrequency", notifsFrequency.getSelectedItem().toString());
        } else {
            map.put("notifYear", -1);
            map.put("notifMonth", -1); // the raw data is month number -1 (ie. april is 3)
            map.put("notifDay", -1);
            map.put("notifHour", -1);
            map.put("notifMinute", -1);
            map.put("notifFrequency", "");
        }
        map.put("nickname", edtNickname.getText().toString());
        map.put("dosage",edtHeight);
        map.put("time",txtTime);
        FirebaseDatabase.getInstance().getReference().child("medicine").push()   //pass to the medicine table and auto-generated key
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddMedicine.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run(){
                                finish();
                            }
                        }, 750);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AddMedicine.this, "Error while Insertion", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void emptyField() {
        if (displayEmpty) {
            edtNicknameEmpty.setVisibility(View.VISIBLE);
            edtNameEmpty.setVisibility(View.VISIBLE);
        }

        edtNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtNickname.getText().toString().equals("")) {
                    edtNicknameEmpty.setVisibility(View.GONE);
                    edtNameEmpty.setVisibility(View.GONE);
                    displayEmpty = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtName.getText().toString().equals("")) {
                    edtNicknameEmpty.setVisibility(View.GONE);
                    edtNameEmpty.setVisibility(View.GONE);
                    displayEmpty = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    // Allow user select time
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @NotNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @SuppressLint("SetTextI18n")
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            String ampm = "am";
            int newHour = hourOfDay;
            String newMinute = Integer.toString(minute);
            notifHour = hourOfDay;
            notifMinute = minute;

            if (minute < 10) {
                newMinute = "0" + minute;
            }else if(hourOfDay > 12) {
                newHour -= 12;
                ampm = "pm";
            } else if (hourOfDay == 12) {
                ampm = "pm";
            } if (hourOfDay == 0) {
                newHour = 12;
            }
            txtTime.setText(newHour + " : " + newMinute + " " + ampm);
        }
    }

    public void showTimePickerDialog (View v){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    //Using Datepicker
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NotNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @SuppressLint("SetTextI18n")
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String newDay = Integer.toString(day);
            String newMonth = Integer.toString(month + 1);
            notifDay = day;
            notifMonth = month;
            notifYear = year;

            if (day < 10) {
                newDay = "0" + newDay;
            }
            if (month++ < 10) {
                newMonth = "0" + newMonth;
            }

            txtDate.setText(newDay + " / " + newMonth + " / " + year);
        }
    }

    public void showDatePickerDialog (View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    private void init(){
        edtNameEmpty = findViewById(R.id.edtNameEmpty);
        edtNicknameEmpty = findViewById(R.id.edtNameEmpty);
        edtName = findViewById(R.id.edtMedicineID);
        edtNickname = findViewById(R.id.edtNickname);
        edtHeight = findViewById(R.id.edtHeight);
        edtNotes = findViewById(R.id.edtNotes);
        txtTime = findViewById(R.id.txtTime);
        txtDate = findViewById(R.id.txtDate);
        notifsFrequency = findViewById(R.id.notifsFrequency);
        swtchNotifs = findViewById(R.id.swtchNotifs);
        insertData = findViewById(R.id.btnAdd);
        imageButton = findViewById(R.id.imageButton);
        btnPickDate = findViewById(R.id.btnDate);
        btnPickTime = findViewById(R.id.btnTime);
        displayEmpty = false;

    }

}

