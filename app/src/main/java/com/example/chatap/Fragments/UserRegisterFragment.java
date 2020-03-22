package com.example.chatap.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatap.Model.User;
import com.example.chatap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegisterFragment extends Fragment
{

    private EditText userName, userPhoneNumber;
    private Button buttonRegister;
    private DatabaseReference userDataBase, employeeAttendance;
    public UserRegisterFragment()
    {
        // Required empty public constructor
    }

    public static UserRegisterFragment newInstance() {
        return new UserRegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_register, container, false);
        initViews(view);
        buttonRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerNumber(userName.getText().toString(),userPhoneNumber.getText().toString());
            }
        });
        return view;
    }

    private void registerNumber(String name, String phoneNumber)
    {
        //FireBase register to be done
        User userDetails = new User(name, phoneNumber);
        userDataBase = FirebaseDatabase.getInstance().getReference(); // Add the reference
        userDataBase.child("Users").child(phoneNumber).setValue(userDetails).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            public void onSuccess(Void aVoid) // If the task is successful i. e registration successful
            {
                //employeeAttendance.child("Employee Attendance").child(employeeCode).setValue(true);
                Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() // If after the task fails after initiation then either connectivity issue or FireBase down or node not found
        {
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(getContext(), "Registration Failed", Toast.LENGTH_SHORT).show(); // If registration fails
            }
        });
    }

    private void initViews(View view)
    {
        userName = view.findViewById(R.id.userName);
        userPhoneNumber = view.findViewById(R.id.userPhoneNumber);
        buttonRegister = view.findViewById(R.id.buttonRegister);
    }
}
