package com.example.myplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button add;
    CalendarView date;
    Button view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.button);
        add.setOnClickListener(view -> add());

        date = findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        date.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            calendar.set(i, i1, i2);
            date.setDate(calendar.getTimeInMillis());
        });

        view = findViewById(R.id.button2);
        view.setOnClickListener(view1 -> view());
    }

    private void view() {
        Intent intent = new Intent(MainActivity.this, ViewActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(intent);
    }


    private void add() {
        Intent intent = new Intent(MainActivity.this, AddActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra("date", date.getDate());
        startActivity(intent);
    }
}