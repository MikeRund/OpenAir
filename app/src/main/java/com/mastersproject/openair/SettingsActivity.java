package com.mastersproject.openair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mastersproject.openair.ui.BaseActivity;

public class SettingsActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Nav draw
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_settings_navDraw);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        Intent i;

        if (itemID == R.id.action_home_navDraw){

            i = new Intent(SettingsActivity.this, HomePostListActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_add_navDraw){

            i = new Intent(SettingsActivity.this, NewPostActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_profile_navDraw){

            i = new Intent(SettingsActivity.this, MyStuffActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_settings_navDraw){

//            i = new Intent(SettingsActivity.this, SettingsActivity.class);
//            startActivity(i);
            Toast.makeText(this, "Nav Bar Working", Toast.LENGTH_SHORT).show();
            return true;

        } else {
            return false;
        }
    }
}