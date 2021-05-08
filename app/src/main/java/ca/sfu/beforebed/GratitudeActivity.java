package ca.sfu.beforebed;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class GratitudeActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    //this is just a simple activity with an EditText and button
    //when button is clicked, it retrieves the user input into string, and sends it back to
    //MainActivity
    EditText entryEditText;
    String gratitudeText;
    Button gratitudeButton;
    public static final String RESULT_KEY = "GRATITUDE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_BeforeBed);
        setContentView(R.layout.activity_gratitude);
        entryEditText = (EditText) findViewById(R.id.entryEditText);
        gratitudeButton = (Button) findViewById(R.id.addGratitudeButton);
        gratitudeButton.setOnClickListener(this);
        entryEditText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (entryEditText.isFocused()) {
            gratitudeText = entryEditText.getText().toString();
//            Log.d("TAG", "onTextChanged: " + gratitudeText);
            //it does go here

        }


    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onClick(View v) {
//        Log.d("TAG", "onClick: " + gratitudeText);

        Intent i = new Intent();
        i.putExtra(RESULT_KEY, gratitudeText);

        //in main activity, we need to check if RESULT_OK
        //sometimes, the user puts nothing, and just hits the look up button
        if (gratitudeText.equals("")) {
            setResult(RESULT_CANCELED, i);
        } else {
            setResult(RESULT_OK, i);
        }
        finish();
    }
}
