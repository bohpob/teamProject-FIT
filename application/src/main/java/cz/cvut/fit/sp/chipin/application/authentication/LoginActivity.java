package cz.cvut.fit.sp.chipin.application.authentication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import cz.cvut.fit.sp.chipin.application.R;

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fb, google, twitter;
    private boolean isBackPressed = false;
    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        fb = findViewById(R.id.fab_facebook);
        google = findViewById(R.id.fab_google);
        twitter = findViewById(R.id.fab_twitter);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception ignored) {}
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fb.setTranslationY(300);
        google.setTranslationY(300);
        twitter.setTranslationY(300);
        tabLayout.setTranslationY(300);

        fb.setAlpha(v);
        google.setAlpha(v);
        twitter.setAlpha(v);
        tabLayout.setAlpha(v);

        tabLayout.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(100).start();
        fb.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(600).start();
        twitter.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(800).start();


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Facebook";
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Google";
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Twitter";
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed();
            return;
        }

        Toast.makeText(LoginActivity.this, "Click again to exit", Toast.LENGTH_SHORT).show();
        isBackPressed = true;

        new Handler().postDelayed(() -> isBackPressed = false, 2000);

    }
}