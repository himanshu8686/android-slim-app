package com.example.userwithslim.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.userwithslim.R;
import com.example.userwithslim.domain.User;
import com.example.userwithslim.fragments.HomeFragment;
import com.example.userwithslim.fragments.SettingsFragment;
import com.example.userwithslim.fragments.UserFragment;
import com.example.userwithslim.storage.SharedPreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        displayFragment(new HomeFragment());
    }

    private void displayFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!SharedPreferenceManager.getInstance(this).isLoggedIn())
        {
            Intent intent=new Intent(this,RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Fragment fragment=null;
        switch (item.getItemId())
        {
            case R.id.menu_home:
                fragment=new HomeFragment();
                break;
            case R.id.menu_users:
                fragment=new UserFragment();
                break;
            case R.id.menu_settings:
                fragment=new SettingsFragment();
                break;
        }

        if (fragment!=null)
        {
            displayFragment(fragment);
        }
        return false;
    }
}
