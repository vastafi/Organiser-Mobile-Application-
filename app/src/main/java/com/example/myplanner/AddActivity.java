package com.example.myplanner;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    Button add;
    String storeDate;
    String time;
    String title;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        createNotificationChannel();

        long date = getIntent().getExtras().getLong("date");
        TextView text = findViewById(R.id.datev);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        storeDate = simpleDateFormat.format(date);
        text.append(storeDate);

        TimePicker timePicker = findViewById(R.id.timePicker);
        time = timePicker.getHour() + ":" + timePicker.getMinute();
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            TextView text2 = findViewById(R.id.timev);
            time = hourOfDay + ":" + minute;
            text2.append(time);
        });

        add = findViewById(R.id.addB);
        add.setOnClickListener(view -> {
            try {
                writeToXml();
                Toast.makeText(this, "Reminder added to calendar", Toast.LENGTH_SHORT).show();

                String stringDate = storeDate + " " + time;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime dateNotif = LocalDateTime.parse(stringDate, formatter);
                long timeInMillis = dateNotif.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                Intent intent = new Intent(AddActivity.this, NotificationReceiver.class);
                intent.putExtra("Title", title);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this, 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Log.println(Log.ASSERT, "time", String.valueOf(timeInMillis));
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        timeInMillis,
                        pendingIntent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void writeToXml() throws IOException {
        try {
            EditText editText = findViewById(R.id.editText);
            title = editText.getText().toString();
            FileOutputStream fileOutputStream;
            File file = getBaseContext().getFileStreamPath("data.xml");
            boolean exists = false;
            if(file.exists()) {
                exists = true;
            }
            fileOutputStream = openFileOutput("data.xml", MODE_APPEND);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            xmlSerializer.setOutput(writer);
            if(!exists) {
                xmlSerializer.startDocument("UTF-8", true);
            }
            xmlSerializer.startTag("", "doc");
            xmlSerializer.startTag("", "title");
            xmlSerializer.text(title);
            xmlSerializer.endTag("", "title");
            xmlSerializer.startTag("", "date");
            xmlSerializer.text(storeDate);
            xmlSerializer.endTag("", "date");
            xmlSerializer.startTag("", "time");
            xmlSerializer.text(time);
            xmlSerializer.endTag("", "time");
            xmlSerializer.endTag("", "doc");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileOutputStream.write(dataWrite.getBytes());
            fileOutputStream.close();

        } catch (IllegalArgumentException | IOException | IllegalStateException e) {
            e.printStackTrace();

        }
    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PlannerReminderChannel";
            String description = "Channel for planner";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("plannerNotification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}