package com.example.chatap.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.chatap.Model.User;
import com.example.chatap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ChatFragment extends Fragment
{
    private LinearLayout layout;
    private RelativeLayout layout_2;
    private ImageView sendButton;
    private EditText messageArea;
    private ScrollView scrollView;
    private DatabaseReference reference1, reference2;
    private String userPhoneNumber, chatWithUserName, chatWithUserNumber;
    private static final String CHAT_USER_NAME = "chat_user_name";
    private static final String CHAT_USER_NUMBER = "chat_user_number";
    private DatabaseReference userDataBase;
    private User userObject;
    private int checkerFlag = 0;

    public ChatFragment()
    {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String chatWithName, String chatWithNumber)
    {
        ChatFragment chatFragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(CHAT_USER_NAME, chatWithName);
        args.putString(CHAT_USER_NUMBER, chatWithNumber);
        chatFragment.setArguments(args);
        return chatFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initViews(view);
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fetchUserDetails("NA",2);
            }
        });

        reference1.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s)
            {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(userPhoneNumber))
                {
                    addMessageBox("You :\n" + " " + message, 1);
                }
                else
                {
                    addMessageBox(chatWithUserName + " :\n" + " " + message, 2);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        fetchUserDetails("Online",1);
    }
    private void initViews(View view)
    {
        layout = view.findViewById(R.id.layout1);
        layout_2 = view.findViewById(R.id.layout2);
        sendButton = view.findViewById(R.id.sendButton);
        messageArea = view.findViewById(R.id.messageArea);
        scrollView = view.findViewById(R.id.scrollView);
        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("User Registration Status", MODE_PRIVATE);
        userPhoneNumber = sharedPreferences.getString("User Phone Number", "");

        if(getArguments() != null)
        {
            chatWithUserName = getArguments().getString(CHAT_USER_NAME);
            chatWithUserNumber = getArguments().getString(CHAT_USER_NUMBER);
        }
        reference1 = FirebaseDatabase.getInstance().getReference().child("Messages").child(userPhoneNumber + "_" + chatWithUserNumber);
        reference2 = FirebaseDatabase.getInstance().getReference().child("Messages").child(chatWithUserNumber + "_" + userPhoneNumber);
        messageArea.setEnabled(true);
    }

    private void addMessageBox(String message, int type)
    {
        TextView textView;
        if(getContext() != null)
        {
            textView = new TextView(getContext());
            textView.setText(message);

            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.weight = 1.0f;

            if(type == 1) //User itself
            {
                lp2.gravity = Gravity.LEFT;
                textView.setBackgroundResource(R.drawable.bubble_in);
            }
            else // the other user
            {
                lp2.gravity = Gravity.RIGHT;
                textView.setBackgroundResource(R.drawable.bubble_out);
            }
            textView.setLayoutParams(lp2);
            layout.addView(textView);
            scrollView.fullScroll(View.FOCUS_DOWN);
        }
    }

    private void sendMessage()
    {
        String messageText = messageArea.getText().toString();
        if(!messageText.equals("") && userObject.status.equals("Online"))
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("message", messageText);
            map.put("user", userPhoneNumber);
            reference1.push().setValue(map);
            reference2.push().setValue(map);
            messageArea.setText("");
        }
        else
        {
            Toast.makeText(getContext(),"Enter Text",Toast.LENGTH_LONG).show();
        }
    }

    private void fetchUserDetails(final String userStatus, final int choice)
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
                        if(choice == 1) // for making the main user user status as online or fofline
                        {
                            if(userList.getKey().equals(userPhoneNumber))
                            {
                                userObject = userList.getValue(User.class);// Assigning the database data to the model object
                                if(userStatus.equals("Online"))
                                {
                                    updateUserStatus(userObject, "Online"); //Update user status to online
                                }
                                else
                                {
                                    updateUserStatus(userObject, "Offline"); //Update user status as offline
                                }
                            }
                            else
                            {
                                continue;
                            }
                        }
                        else //for checking if the chatting user is online or not
                        {
                            if(userList.getKey().equals(chatWithUserNumber))
                            {
                                userObject = userList.getValue(User.class);// Assigning the database data to the model object
                                if(userObject.status.equals("Online")) //User is online on other side hence message can be sent
                                {
                                    sendMessage();
                                }
                                else //User is offline
                                {
                                    messageArea.setEnabled(false);
                                    Toast.makeText(getContext(),"User Is Offline",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                continue;
                            }
                        }

                    }
                } catch(Exception e)
                {
                    Log.e("FetchUserDetails", "Data interchange failed. Exception: <<< " + e.getMessage() + " >>>.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.w("FetchUserDetailsFailed", "Database error : " + databaseError.toException() + " >>>");
            }
        });
    }

    private void updateUserStatus(User userObject, String status)
    {
        User user = new User(userObject.userName, userObject.userPhoneNumber, status, "No Message");
        userDataBase = FirebaseDatabase.getInstance().getReference(); // Add the reference
        userDataBase.child("Users").child(userObject.userPhoneNumber).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            public void onSuccess(Void aVoid) // If the task is successful i. e registration successful
            {
                //Toast.makeText(getActivity(), "Status Updated", Toast.LENGTH_SHORT).show(); // Updating Last message
            }
        }).addOnFailureListener(new OnFailureListener() // If after the task fails after initiation then either connectivity issue or FireBase down or node not found
        {
            public void onFailure(@NonNull Exception e)
            {
                //Toast.makeText(getContext(), "Modification Failed", Toast.LENGTH_SHORT).show(); // Last message not updated
            }
        });
    }
    @Override
    public void onPause()
    {
        checkerFlag = 1;
        fetchUserDetails("Offline",1);
        super.onPause();
    }

    @Override
    public void onStop()
    {
        checkerFlag = 1;
        fetchUserDetails("Offline",1);
        super.onStop();
    }
}
