package ca.sfu.beforebed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanks.passcodeview.PasscodeView;

public class PasscodeActivity extends AppCompatActivity {
    PasscodeView passcodeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        passcodeView=findViewById(R.id.passcodeView);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String password = sharedPrefs.getString("password", MeActivity.DEFAULT);
        String username = sharedPrefs.getString("username", MeActivity.DEFAULT);


        if (password!=MeActivity.DEFAULT)
            passcodeView.setLocalPasscode(password);
        else {
            passcodeView.setLocalPasscode("1234");
        }
        if (username!=MeActivity.DEFAULT) {
            passcodeView.setFirstInputTip("Hi, " + username + " please enter your passcode");
        }

        passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {
                Toast.makeText(getApplicationContext(), "wrong passcode", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String number) {

                Intent intent= new Intent (getApplicationContext(),EntryActivity.class);
                startActivity(intent);
            }
        });
    }
}