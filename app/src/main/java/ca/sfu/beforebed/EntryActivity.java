package ca.sfu.beforebed;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import utils.CameraUtils;

public class EntryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    ImageButton memoryButton, gratitudeButton, doodleButton, moodButton;
    Button doneButton;
    String formattedDate, doodlePath, gratitude;
    Bitmap memoryBitmap;
    int mood=-1;
    MyDatabase db;

    //this is used to request for permission to access the camera of the device
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;

    //these are the requests for 3 user inputs, because the intents that start activity for adding memory, gratitude, and doodle all are
    //startActivityForResult
    private static final int REQUEST_GRATITUDE_ENTRY = 0;
    private static final int REQUEST_MOOD_ENTRY = 1;
    private static final int REQUEST_DOODLE_PATH = 2;
    private static final int CAMERA_REQUEST = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //before setContentView
        setTheme(R.style.Theme_BeforeBed);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set entry selected
        bottomNavigationView.setSelectedItemId(R.id.entry);
        //itemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        memoryButton = findViewById(R.id.memoryButton);
        gratitudeButton = findViewById(R.id.gratitudeButton);
        doodleButton = findViewById(R.id.doodleButton);
        doneButton = findViewById(R.id.doneDBBttn);
        moodButton = findViewById(R.id.moodButton);

        memoryButton.setOnClickListener(this);
        gratitudeButton.setOnClickListener(this);
        doodleButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        moodButton.setOnClickListener(this);

        db = new MyDatabase(this);
        //lets check the value for the variables


        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(date);


    }


    //this method is called when user changes the item on navigation bar
    public void addToDatabase(String date, Bitmap memoryBitmap, String doodlePath, String gratitude, int mood) {
        //I create the doodleBitmap here, because what the doodle activity sends back (in onActivityResult)
        //is the path of the file, so in here, I create the bitmap using the path
        //and pass the bitmap to insertData() method of database
        Bitmap doodleBitmap = null;

        //cannot create bitmap of a null object or empty path
        if (doodlePath != "" && doodlePath != null) {
            doodleBitmap = CameraUtils.scaleDownAndRotatePic(doodlePath);
        }
        //if everything is null, then there is nothing to be added to database
        //but even if one thing is not null(the else block), then it can be added to the database
        //because the user cannot be forced to add 4 inputs
        if ( mood == -1 && memoryBitmap == null && doodleBitmap == null && (gratitude == null || gratitude == "")) {
            Toast.makeText(this, "nothing to be added to database", Toast.LENGTH_SHORT).show();
        } else {

            long id = db.insertData(date, memoryBitmap, doodleBitmap, gratitude, mood);
            if (id < 0) {
                Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.entry:
                return true;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(), RecyclerActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.me:
                startActivity(new Intent(getApplicationContext(), MeActivity.class));
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        //there are 3 buttons for 3 types of user entry
        //memoryButton opens camera to take a picture
        //the result is the picture taken with camera
        if (v.getId() == R.id.memoryButton) {
            if (hasCameraPermission()) {
//                Log.d("PERMISSION", "onClick: has permission");
                enableCamera();
            } else {
                requestPermission();
            }
        }
        //gratitude button starts activity for result
        //and the result is the string user input
        if (v.getId() == R.id.gratitudeButton) {
            Intent i = new Intent(this, GratitudeActivity.class);
            startActivityForResult(i, REQUEST_GRATITUDE_ENTRY);

        }
        //doodle button starts activity for result
        //and the result is the path of image of the doodle by user
        if (v.getId() == R.id.doodleButton) {
            Intent i = new Intent(this, DoodleActivity.class);
            startActivityForResult(i, REQUEST_DOODLE_PATH);
        }
        if (v.getId() == R.id.moodButton) {
            Intent i = new Intent(this, MoodActivity.class);
            startActivityForResult(i, REQUEST_MOOD_ENTRY);
        }

        if (v.getId() == R.id.doneDBBttn) {

            addToDatabase(formattedDate, memoryBitmap, doodlePath, gratitude, mood);
            resetValues();
        }

    }


    private void resetValues() {
        formattedDate = null;
        memoryBitmap = null;
        doodlePath = null;
        gratitude = null;
        mood = -1;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //requestCode is used to differentiate between 3 different results that are coming from 3 different activities
        //then inside each if block checking request code, I check to make sure the resultCode is ok,
        //then I get the extras using the key-value model
        //I store those values inside the fields, that were initially null
        //these fields will later on be passed to addToDatabase()

        if (requestCode == REQUEST_GRATITUDE_ENTRY) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(GratitudeActivity.RESULT_KEY)) {
                    gratitude = data.getStringExtra(GratitudeActivity.RESULT_KEY);
                }
            }
        }
        if (requestCode == CAMERA_REQUEST) {
            if (data != null)
                memoryBitmap = (Bitmap) data.getExtras().get("data");
        } else if (requestCode == REQUEST_DOODLE_PATH) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(DoodleActivity.RESULT_KEY)) {
                    doodlePath = data.getStringExtra(DoodleActivity.RESULT_KEY);
                }
            }

        }
        else if (requestCode == REQUEST_MOOD_ENTRY) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(MoodActivity.RESULT_KEY)) {
                    mood=data.getIntExtra(MoodActivity.RESULT_KEY,-1);
//                    Log.d("MOOD_ENTRY_ACTIVITY", String.valueOf(mood));
                }
            }

        }
//        Log.d("DOODLE", "onActivityResult: " + doodlePath);
//        Log.d("GRAT", "onActivityResult: " + gratitude);
        super.onActivityResult(requestCode, resultCode, data);

    }



    //checks if app has camera permission
    private boolean hasCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else
            return true;
    }

    //requests permission, used in the case where permission is not granted
    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    //using an implicit intent to access the camera of the device
    private void enableCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }
}