package ca.sfu.beforebed;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MyDayActivity extends AppCompatActivity implements View.OnClickListener{
    public ImageView memoryImgView, doodleImgView, moodImgView;
    public TextView gratitudeTextView, dateTextView;
    public CardView memoryCardView, doodleCardView;
    String id;
    Button deleteDayBttn;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_BeforeBed);
        setContentView(R.layout.activity_my_day);
        memoryImgView= findViewById(R.id.selectedMemoryImgView);
        doodleImgView=findViewById(R.id.selectedDoodleImgView);
        gratitudeTextView=findViewById(R.id.selectedGratitudeTextView);
        dateTextView=findViewById(R.id.selectedDateTextView);
        moodImgView=findViewById(R.id.selectedMoodImgView);

        memoryCardView= findViewById(R.id.selectedMemoryCardView);
        doodleCardView=findViewById(R.id.selectedDoodleCardView);

        deleteDayBttn=findViewById(R.id.deleteDayBttn);
        deleteDayBttn.setOnClickListener(this);

        Intent intent= getIntent();
        id=intent.getStringExtra("UID_VAL");

        db = new MyDatabase(this);

        Cursor cursor = db.getData(id);
        int index0 = cursor.getColumnIndex(Constants.UID);
        int index1 = cursor.getColumnIndex(Constants.DATE);
        int index2 = cursor.getColumnIndex(Constants.MEMORY);
        int index3 = cursor.getColumnIndex(Constants.DOODLE);
        int index4 = cursor.getColumnIndex(Constants.GRATITUDE);
        int index5 = cursor.getColumnIndex(Constants.MOOD);

        ArrayList<MyDay> myDays = new ArrayList<MyDay>();
        //first we need to put the cursor at first
        cursor.moveToFirst();
        //checks if theres another data after your last data
        while (!cursor.isAfterLast()) {
            Bitmap memoryBitmap=null;
            Bitmap doodleBitmap=null;
            String UID = cursor.getString(index0);
            String date = cursor.getString(index1);

            //if I dont have this, I will get error in the case that memory or doodle is null
            //because I cannot getImage byte array from null bitmap

            //and this is where I create the arrayList

            if (cursor.getBlob(index2)!=null)
                memoryBitmap = DbBitmapUtility.getImage(cursor.getBlob(index2));
            if (cursor.getBlob(index3)!=null)
                doodleBitmap = DbBitmapUtility.getImage(cursor.getBlob(index3));
            String gratitude = cursor.getString(index4);
            int mood = cursor.getInt(index5);
            //we create a row in each iteration and add it to the arraylist
            MyDay myDay= new MyDay(UID, date,memoryBitmap, doodleBitmap, gratitude, mood);
            myDays.add(myDay);
            cursor.moveToNext();
        }
        memoryImgView.setImageBitmap(myDays.get(0).memoryBitmap);
        doodleImgView.setImageBitmap(myDays.get(0).doodleBitmap);
        gratitudeTextView.setText(myDays.get(0).gratitude);
        dateTextView.setText(myDays.get(0).date);
        if (myDays.get(0).mood==MoodActivity.SAD) {
            //https://stackoverflow.com/questions/8642823/using-setimagedrawable-dynamically-to-set-image-in-an-imageview
            moodImgView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sad_normal));
        } else if (myDays.get(0).mood==MoodActivity.NEUTRAL)
            moodImgView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.neutral_normal));
        else if (myDays.get(0).mood==MoodActivity.HAPPY)
           moodImgView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.happy_normal));

        if (myDays.get(0).mood==-1){
            moodImgView.setVisibility(View.GONE);
        }
        if (myDays.get(0).memoryBitmap==null){
            memoryCardView.setVisibility(View.GONE);
        }
        if (myDays.get(0).doodleBitmap==null){
            doodleCardView.setVisibility(View.GONE);
        }


    }

    //when we click an item, and we see that item only,
    //there will be a delete button,
    //when we click that, this method is called,
    //type is a field, which can be
    public void removeDay() {
        String[] selection= new String[]{id};
        int count = db.deleteRow(selection);
        Toast.makeText(this, ""+ count, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.deleteDayBttn){
            removeDay();
//            Log.d("DELETE_BUTTON", "onClick: ");
            startActivity(new Intent(this, RecyclerActivity.class));
        }
    }
}