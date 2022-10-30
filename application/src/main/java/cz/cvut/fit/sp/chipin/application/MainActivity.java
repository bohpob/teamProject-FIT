package cz.cvut.fit.sp.chipin.application;

import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false);

        Intent intent;
        if (isLoggedIn) {
            intent = new Intent(MainActivity.this, MenuActivity.class)
                    .putExtra("name", sharedPrefs.getString("name", null))
                    .putExtra("email", sharedPrefs.getString("email", null))
                    .putExtra("id", sharedPrefs.getString("id", null));
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();


    }

}