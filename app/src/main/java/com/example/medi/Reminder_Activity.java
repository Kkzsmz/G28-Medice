package com.example.medi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Reminder_Activity extends AppCompatActivity {
    private LinearLayout remindersLayout;
    private ScrollView remindersScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        // Get references to layout views
        remindersLayout = findViewById(R.id.reminders_layout);
        remindersScrollView = findViewById(R.id.reminders_scroll_view);

        // Show calendar to select date
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(R.string.select_date);
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<Long> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // Display reminders for the selected date
                displayRemindersForDate(selectedDate);
            }
        });
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    private void displayRemindersForDate(long selectedDate) {
        // Clear any previous reminders
        remindersLayout.removeAllViews();

        // Get reminders for the selected date (dummy data for testing)
        List<Reminder> reminders = getRemindersForDate(selectedDate);

        // Add reminder views to the layout
        for (Reminder reminder : reminders) {
            View reminderView = createReminderView(reminder);
            remindersLayout.addView(reminderView);
        }

        // Show up to 3 reminders below the calendar with scroll
        if (reminders.size() > 0) {
            remindersScrollView.setVisibility(View.VISIBLE);
            if (reminders.size() > 3) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.height = convertDpToPx(100) * 3; // Set maximum height to 3 items
                remindersScrollView.setLayoutParams(params);
            }
        } else {
            remindersScrollView.setVisibility(View.GONE);
        }
    }

    private List<Reminder> getRemindersForDate(long selectedDate) {
        // Dummy data for testing
        List<Reminder> reminders = new ArrayList<>();
        reminders.add(new Reminder("Medication 1", selectedDate + 9 * 60 * 60 * 1000, true));
        reminders.add(new Reminder("Medication 2", selectedDate + 12 * 60 * 60 * 1000, false));
        reminders.add(new Reminder("Medication 3", selectedDate + 15 * 60 * 60 * 1000, true));
        reminders.add(new Reminder("Medication 4", selectedDate + 18 * 60 * 60 * 1000, false));
        reminders.add(new Reminder("Medication 5", selectedDate + 21 * 60 * 60 * 1000, true));
        return reminders;
    }

    private View createReminderView(Reminder reminder) {
        // Create reminder view from layout file
        View reminderView = LayoutInflater.from(this).inflate(R.layout.reminder_layout, null);
        // Set reminder details
        TextView nameTextView = reminderView.findViewById(R.id.reminder_name_text_view);
        nameTextView.setText(reminder.getName());
        TextView timeTextView = reminderView.findViewById(R.id.reminder_time_text_view);
        timeTextView.setText(getFormattedTime(reminder.getTime()));
        Switch repeatSwitch = reminderView.findViewById(R.id.repeat_switch);
        repeatSwitch.setChecked(reminder.isRepeatWeekly());

        // Set switch listener to update reminder in database
        repeatSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update reminder in database with new repeat value
            // TODO: Implement update in database

        });

        return reminderView;
    }

    private String getFormattedTime(long timeInMillis) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date = new Date(timeInMillis);
        return dateFormat.format(date);
    }

    private int convertDpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


}
