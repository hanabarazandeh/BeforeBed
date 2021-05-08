package ca.sfu.beforebed;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ReminderActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener, CompoundButton.OnCheckedChangeListener {


    private boolean switchState;
    SwitchCompat simpleSwitch;
    int hourPicked, minutePicked;
    TimePicker timePicker;
    TextView reminderMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_BeforeBed);
        setContentView(R.layout.activity_reminder);

        // initiate a Switch
        simpleSwitch = (SwitchCompat) findViewById(R.id.simpleSwitch);
        retrievePrefs();
        simpleSwitch.setOnCheckedChangeListener(this);
        simpleSwitch.setChecked(switchState);
        reminderMsg= findViewById(R.id.reminderMsg);
        reminderMsg.setText("you have a reminder set for "+hourPicked+" : "+minutePicked+" you can also pick a new one");

        // check current state of a Switch (true or false).
//        switchState = simpleSwitch.isChecked();

        timePicker = (TimePicker) findViewById(R.id.timePicker); // initiate a time picker
//        AttributeSet attrs=getAttr(R.id.timePicker);
//        timePicker = new TimePicker(getApplicationContext(), attrs,R.style.myTimePickerStyle);
        Calendar mcurrentTime = Calendar.getInstance();
        //set the hour and minute of the timepicker to the current time
        //works
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(this);
        //set the value for current hours


    }

    private AttributeSet getAttr(int id) {
        //https://stackoverflow.com/questions/4410152/how-can-i-create-an-attributeset-from-a-style-xml
        Resources res = getApplicationContext().getResources();
        @SuppressLint("ResourceType") XmlPullParser parser = res.getXml(id);

// Seek to the first tag.
        int type = 0;
        while (type != XmlPullParser.END_DOCUMENT && type != XmlPullParser.START_TAG) {
            try {
                type = parser.next();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }
        AttributeSet attrs = Xml.asAttributeSet(parser);

// Wrap as an attribute set.
        return attrs;

    }


    private void addReminder() {

        Calendar customCalendar = new GregorianCalendar();
        customCalendar.setTimeInMillis(System.currentTimeMillis());
        customCalendar.set(Calendar.HOUR_OF_DAY, hourPicked);
        customCalendar.set(Calendar.MINUTE, minutePicked);
        customCalendar.set(Calendar.AM_PM, Calendar.AM); // Important Part
        customCalendar.set(Calendar.SECOND, 0);
        customCalendar.set(Calendar.MILLISECOND, 0);


        Intent intent = new Intent(this,
                ReminderBroadcastReceiver.class);

        PendingIntent action = PendingIntent.getBroadcast(this,
                4002, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, customCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, action);
        // new ReminderBroadcastReceiver(this).onReceive(this,new Intent());

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.simpleSwitch) {
//            Log.d("SWITCH", "onCheckedChanged: " + simpleSwitch.isChecked());
            if (simpleSwitch.isChecked()) {
                switchState = true;
            }
        }

    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        if (view.getId() == R.id.timePicker) {
            hourPicked = timePicker.getCurrentHour();
            minutePicked = timePicker.getCurrentMinute();
            if (switchState) {
                addReminder();
            }
        }


    }

    private void saveToSharedPrefs() {
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putBoolean("switch_state", switchState);
        editor.putInt("hour_picked",hourPicked);
        editor.putInt("minute_picked",minutePicked);
        //and finally commit method commits all the changes we just made
        editor.commit();
    }

    private void retrievePrefs() {
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        switchState = sharedPrefs.getBoolean("switch_state", false);
        hourPicked=sharedPrefs.getInt("hour_picked",0);
        minutePicked=sharedPrefs.getInt("minute_picked",0);
//        Toast.makeText(this, "retrieved" + switchState+hourPicked+minutePicked, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        saveToSharedPrefs();
        super.onBackPressed();
    }

    //    public void addReminder(View view) {
//        addingReminder();
//    }
}
