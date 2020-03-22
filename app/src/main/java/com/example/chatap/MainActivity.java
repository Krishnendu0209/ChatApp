package com.example.chatap;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chatap.Fragments.UserRegisterFragment;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, UserRegisterFragment.newInstance()) // opening the login fragment
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .addToBackStack("register").commit();
    }
}
