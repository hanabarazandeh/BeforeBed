package ca.sfu.beforebed;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class RecyclerActivity extends Activity implements AdapterView.OnItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener{
    //this activity corresponds to Home in the app UI
    //deleteDayButton is only visible when showing only 1 item

    //TODO
    //https://www.geeksforgeeks.org/recyclerview-as-staggered-grid-in-android-with-example/

    RecyclerView myRecycler;
    MyDatabase db;
    MyAdapter myAdapter;
    RecyclerView.LayoutManager myLayoutManager;

    TextView emptyTextView;
    ImageView emptyImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_BeforeBed);
        setContentView(R.layout.activity_recycler);
        emptyImgView= findViewById(R.id.emptyImgView);
        emptyTextView= findViewById(R.id.emptyTextView);
        emptyImgView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);

        myRecycler = (RecyclerView) findViewById(R.id.recycler);

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.me);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


//        Log.d("TAG", "onCreate: "+type);

        db = new MyDatabase(this);

        Cursor cursor = db.getData(null);
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

        myLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(myLayoutManager);
        myAdapter = new MyAdapter(this,myDays);
        myRecycler.setAdapter(myAdapter);

        //if myDays.size()==1, it is equivalent to MyDayActivity
        //it means its showing the selected item
        //only if myDays.size() is 1, add the button for delete

        if (myDays.size()==0){
            emptyImgView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
//        Log.d("myDaysSize", "onCreate: "+ myDays.size());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
   }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.entry:
                startActivity(new Intent(getApplicationContext(),EntryActivity.class));
                overridePendingTransition(0,0);
                return true;
            case R.id.home:
                return true;
            case R.id.me:
                startActivity(new Intent(getApplicationContext(),MeActivity.class));
                overridePendingTransition(0,0);
                return true;
        }
        return false;
    }
}