package com.mastersproject.openair.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mastersproject.openair.HomePostListActivity;
import com.mastersproject.openair.MyStuffActivity;
import com.mastersproject.openair.NewPostActivity;
import com.mastersproject.openair.R;
import com.mastersproject.openair.SettingsActivity;

public abstract class BaseActivity extends AppCompatActivity
    implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == R.id.action_home_navDraw){

            i = new Intent(this, HomePostListActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_add_navDraw){

            i = new Intent(this, NewPostActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_profile_navDraw){

            i = new Intent(this, MyStuffActivity.class);
            startActivity(i);
            return true;

        } else if (itemID == R.id.action_settings_navDraw){

            i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;

        } else {
            return false;
        }
    }

}
