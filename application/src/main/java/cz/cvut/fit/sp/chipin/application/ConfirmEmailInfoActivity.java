package cz.cvut.fit.sp.chipin.application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmEmailInfoActivity extends AppCompatActivity {

    Button button;

    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_email_info);

        button = findViewById(R.id.go_to_login_btn);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmEmailInfoActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed();
            return;
        }

        Toast.makeText(ConfirmEmailInfoActivity.this, "Click again to exit", Toast.LENGTH_SHORT).show();
        isBackPressed = true;

        new Handler().postDelayed(() -> isBackPressed = false, 2000);

    }

}
