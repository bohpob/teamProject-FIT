package cz.cvut.fit.sp.chipin.application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcomeText = findViewById(R.id.welcome_text);
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            welcomeText.setText("Welcome, " + intent.getStringExtra("name"));
        }

    }

}
