package com.example.chatap.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatap.Adapter.ChatListAdapter;
import com.example.chatap.MainActivity;
import com.example.chatap.Model.User;
import com.example.chatap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class UserListFragment extends Fragment
{
    private RecyclerView userListView;
    private DatabaseReference userDataBase;
    private User userObject;
    private ArrayList<User> usersList = new ArrayList<>();
    private ProgressBar progressBar;
    private String userPhoneNumber;
    MainActivity mainActivity;
    public UserListFragment()
    {
        // Required empty public constructor
    }

    public static UserListFragment newInstance()
    {
        return new UserListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view)
    {
        userListView = view.findViewById(R.id.userList);
        progressBar = view.findViewById(R.id.progress);
        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("User Registration Status", MODE_PRIVATE);
        userPhoneNumber = sharedPreferences.getString("User Phone Number","");
        mainActivity = new MainActivity();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        usersList.clear();
        fetchUserDetails();
    }

    private void fetchUserDetails()
    {
        userDataBase = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        userDataBase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                try
                {
                    for(DataSnapshot userList : dataSnapshot.getChildren())
                    {
                        userObject = userList.getValue(User.class);// Assigning the database data to the model object
                        if(userList.getKey().equals(userPhoneNumber))
                        {
                            updateUserStatus(userObject, "Online");
                            continue;
                        }
                        usersList.add(userObject);//Will contain the phone number wise details
                    }
                } catch(Exception e)
                {
                    Log.e("FetchUserlist", "Data interchange failed. Exception: <<< " + e.getMessage() + " >>>.");
                }
                if(!usersList.isEmpty())
                {
                    ChatListAdapter chatListAdapter = new ChatListAdapter(usersList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    userListView.setLayoutManager(mLayoutManager);
                    userListView.setItemAnimator(new DefaultItemAnimator());
                    userListView.setAdapter(chatListAdapter);
                    chatListAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(getContext(),"Check Network Connection", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.w("FetchUserlist", "Database error : " + databaseError.toException() + " >>>");
            }
        });
    }
    public void updateUserStatus(User userObject, String status)
    {
        User user = new User(userObject.userName, userObject.userPhoneNumber, status, userObject.lastMessage);
        userDataBase = FirebaseDatabase.getInstance().getReference(); // Add the reference
        userDataBase.child("Users").child(userPhoneNumber).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            public void onSuccess(Void aVoid) // If the task is successful i. e registration successful
            {
                Toast.makeText(getContext(), "Status Changed", Toast.LENGTH_SHORT).show(); // User Status Changed
            }
        }).addOnFailureListener(new OnFailureListener() // If after the task fails after initiation then either connectivity issue or FireBase down or node not found
        {
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(getContext(), "Modification Failed", Toast.LENGTH_SHORT).show(); // User Status failed to change
            }
        });
    }
}
