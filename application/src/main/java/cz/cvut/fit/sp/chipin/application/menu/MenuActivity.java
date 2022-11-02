package cz.cvut.fit.sp.chipin.application.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import cz.cvut.fit.sp.chipin.application.R;
import cz.cvut.fit.sp.chipin.application.ServiceGenerator;
import cz.cvut.fit.sp.chipin.application.SessionManager;
import cz.cvut.fit.sp.chipin.application.authentication.LoginActivity;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SessionManager sessionManager = new SessionManager(this);

        drawerLayout = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navView = findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(this);

        TextView email = navView.getHeaderView(0).findViewById(R.id.nav_header_email);
        TextView name = navView.getHeaderView(0).findViewById(R.id.nav_header_name);

        name.setText(sessionManager.getName());
        email.setText(sessionManager.getEmail());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileItem()).commit();
            toolbar.setTitle("Profile");
            navView.setCheckedItem(R.id.profile);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileItem()).commit();
                toolbar.setTitle("Profile");
                break;
            case R.id.groups:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GroupsItem()).commit();
                toolbar.setTitle("Groups");
                break;
            case R.id.about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutItem()).commit();
                toolbar.setTitle("About");
                break;
            case R.id.feedback:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedbackItem()).commit();
                toolbar.setTitle("Feedback");
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsItem()).commit();
                toolbar.setTitle("Settings");
                break;
            case R.id.share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                ServiceGenerator.logout();
                new Handler().postDelayed(() -> startActivity(new Intent(MenuActivity.this, LoginActivity.class)), 0);
                SessionManager sessionManager = new SessionManager(MenuActivity.this);
                sessionManager.clearSession();
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed();
            return;
        }

        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            Toast.makeText(MenuActivity.this, "Click again to exit", Toast.LENGTH_SHORT).show();
            isBackPressed = true;
        }

        new Handler().postDelayed(() -> isBackPressed = false, 2000);

    }

}