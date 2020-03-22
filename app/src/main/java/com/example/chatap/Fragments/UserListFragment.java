package com.example.chatap.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatap.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment
{


    public UserListFragment()
    {
        // Required empty public constructor
    }
    public static UserListFragment newInstance() {
        return new UserListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

}
