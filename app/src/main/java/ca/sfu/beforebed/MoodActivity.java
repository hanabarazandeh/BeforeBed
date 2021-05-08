package ca.sfu.beforebed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MoodActivity extends AppCompatActivity implements View.OnClickListener{

    int mood=-1;
    public static final int SAD=0;
    public static final int NEUTRAL=1;
    public static final int HAPPY=2;
    ImageButton sadBttn, neutralBttn, happyBttn;
    public static final String RESULT_KEY = "MOOD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_BeforeBed);
        setContentView(R.layout.activity_mood);
        sadBttn=findViewById(R.id.sadBttn);
        neutralBttn=findViewById(R.id.neutralBttn);
        happyBttn=findViewById(R.id.happyBttn);
        sadBttn.setOnClickListener(this);
        neutralBttn.setOnClickListener(this);
        happyBttn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sadBttn){
            mood=SAD;
        }
        else if (v.getId() == R.id.neutralBttn){
            mood=NEUTRAL;
        }
        else if (v.getId()==R.id.happyBttn){
            mood=HAPPY;
        }
        Intent i = new Intent();
        i.putExtra(RESULT_KEY, mood);

        //in main activity, we need to check if RESULT_OK
        //sometimes, the user puts nothing, and just hits the look up button
        if (mood==-1) {
            setResult(RESULT_CANCELED, i);
        } else {
            setResult(RESULT_OK, i);
        }
        finish();

    }
}