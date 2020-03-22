package com.example.chatap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.chatap.Fragments.UserListFragment;
import com.example.chatap.Fragments.UserRegisterFragment;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("User Registration Status", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        openFragment(sharedPreferences);
    }
    private void openFragment(SharedPreferences sharedPreferences)
    {
        if(!sharedPreferences.getBoolean("Registered User", false)) //User not registered hence initial registration required
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, UserRegisterFragment.newInstance()) // opening the login fragment
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .addToBackStack("register").commit();
        }
        else  //Initial registration not required hence display list of users
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, UserListFragment.newInstance()) // opening the login fragment
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .addToBackStack("register").commit();
        }
    }
}
