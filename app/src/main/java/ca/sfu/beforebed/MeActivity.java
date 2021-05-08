package ca.sfu.beforebed;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private float xCurrent, yCurrent, zCurrent, xPrevious, yPrevious, zPrevious, xDiffernce, yDiffernce, zDiffernce;
    private Sensor accel;
    boolean itIsNotFirstTime = false;
    private float shakeThreshold = 40.0f;
    private SensorManager sensorManager;

    private String msgForUser;

    public static final String DEFAULT = "not available";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_BeforeBed);
        setContentView(R.layout.activity_me);
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        //set entry selected
        bottomNavigationView.setSelectedItemId(R.id.me);
        //itemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null) {
            accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,accel,SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.entry:
                startActivity(new Intent(getApplicationContext(),EntryActivity.class));
                overridePendingTransition(0,0);
                return true;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(),RecyclerActivity.class));
                overridePendingTransition(0,0);
                return true;
            case R.id.me:
                return true;
        }
        return false;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        xCurrent=event.values[0];
        yCurrent=event.values[1];
        zCurrent=event.values[2];

        if(itIsNotFirstTime){
            xDiffernce=Math.abs(xPrevious-xCurrent);
            yDiffernce=Math.abs(yPrevious-yCurrent);
            zDiffernce=Math.abs(zPrevious-zCurrent);
//            Log.d("Vibration Checked", String.valueOf(xDiffernce)+String.valueOf(yDiffernce)+String.valueOf(zDiffernce));
            if((xDiffernce>shakeThreshold && yDiffernce>shakeThreshold) || (xDiffernce>shakeThreshold
                    && zDiffernce>shakeThreshold)
                    || (yDiffernce>shakeThreshold && zDiffernce>shakeThreshold)){

//                Log.d("Vibration Checked","SuccessFull");
                //after shaking move from this activity to MainActivity
                Intent intent =new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }


        //if it is the first time that if block will be skipped,
        //the curr values of x, y and z would be stored in prev x, y and z.
        //and itIsNotFirstTime is set to true
        xPrevious=xCurrent;
        yPrevious=yCurrent;
        zPrevious=zCurrent;
        itIsNotFirstTime=true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void goToAccount(View view) {
        Intent i= new Intent(this, AccountActivity.class);
        startActivity(i);
    }
    public void goToReminder(View view) {
        Intent i= new Intent(this, ReminderActivity.class);
        startActivity(i);
    }
}