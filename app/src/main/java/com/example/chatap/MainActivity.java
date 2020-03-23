package com.example.chatap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatap.Fragments.UserListFragment;
import com.example.chatap.Fragments.UserRegisterFragment;
import com.example.chatap.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
    private DatabaseReference userDataBase;
    private User userObject;
    private String userPhoneNumber;
    private int checkerFlag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("User Registration Status", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        userPhoneNumber = sharedPreferences.getString("User Phone Number","");
        openFragment(sharedPreferences);
    }
    @Override
    protected void onPause()
    {
        updateUserStatus("Offline");
        checkerFlag = 1;
        super.onPause();
    }
    public void onResume()
    {
        super.onResume();
        if(checkerFlag == 1)
        {
            updateUserStatus("Online");
        }
    }
    @Override
    protected void onStop()
    {
        updateUserStatus("Offline");
        checkerFlag = 1;
        super.onStop();
    }

    @Override
    public void onBackPressed()
    {
        if(getSupportFragmentManager().getBackStackEntryCount() == 0)
        {
            exitApp();
        }
        else
        {
            super.onBackPressed();
        }
    }
    private void exitApp()
    {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
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
                    .commit();
        }
    }
    private void fetchUserDetails(final String userPhoneNumber) // Function is responsible for fetching details corresponding to employee code form FireBase
    {
        userDataBase = FirebaseDatabase.getInstance().getReference().child("Users");
        userDataBase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild(userPhoneNumber))
                {
                    try
                    {
                        for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                        {
                            if(userSnapshot.getKey().equals(userPhoneNumber))
                            {
                                userObject = userSnapshot.getValue(User.class);// Assigning the database data to the model object
                            }
                        }
                    } catch(Exception e)
                    {
                        Log.e("fetchUserDetails", "Data interchange failed. Exception: <<< " + e.getMessage() + " >>>.");
                    }
                }
                else
                {
                    //Employee Code not found
                    Toast.makeText(MainActivity.this, "Number Not Found!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.w("fetDisEmpAttenData", "Database error : " + databaseError.toException() + " >>>");
            }
        });
    }
    private void updateUserStatus(String status)
    {
        fetchUserDetails(userPhoneNumber);
        User user = new User(userObject.userName, userObject.userPhoneNumber, status, userObject.lastMessage);
        userDataBase = FirebaseDatabase.getInstance().getReference(); // Add the reference
        userDataBase.child("Users").child(userPhoneNumber).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            public void onSuccess(Void aVoid) // If the task is successful i. e registration successful
            {
                Toast.makeText(MainActivity.this, "Status Changed", Toast.LENGTH_SHORT).show(); // If registration fails
            }
        }).addOnFailureListener(new OnFailureListener() // If after the task fails after initiation then either connectivity issue or FireBase down or node not found
        {
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(MainActivity.this, "Modification Failed", Toast.LENGTH_SHORT).show(); // If registration fails
            }
        });
    }

}
