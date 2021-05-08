package ca.sfu.beforebed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {


    public EditText usernameEditText, passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_BeforeBed);
        setContentView(R.layout.activity_account);

        usernameEditText = (EditText)findViewById(R.id.editTextUsername);
        passwordEditText = (EditText)findViewById(R.id.editTextPassword);
    }
    public void saveToSharedPrefs(View view){

        //we use getSharedPreferences method which belongs to the Activity class
        //to create a sharedPreferences object
        //it creates a private xml file with the given name
        //we can use edit method to write to it
        //using the Editor object we can put new data to our xml files
        //we can use putString, putFloat, ...

        String username=usernameEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        if (!username.equals("")|| !password.equals("")){
            if (!username.equals("")) {
                editor.putString("username", username);
//                Toast.makeText(this, "Username saved to Preferences", Toast.LENGTH_LONG).show();
            }
            if (!password.equals("")){
                editor.putString("password",password);
//                Toast.makeText(this, "password saved to Preferences", Toast.LENGTH_LONG).show();
            }
            //and finally commit method commits all the changes we just made
            //we will retrieve this in the 2nd activity
            editor.commit();
            startActivity(new Intent(this, MeActivity.class));
        }


    }
}