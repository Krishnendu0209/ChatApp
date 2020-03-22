package com.example.chatap.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatap.Model.User;
import com.example.chatap.R;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>
{
    private ArrayList<User> usersList;
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName, userPhoneNumber, userStatus, lastMessage;
        public LinearLayout listElement;
        public ViewHolder(View itemView)
        {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userPhoneNumber = itemView.findViewById(R.id.userPhoneNumber);
            userStatus = itemView.findViewById(R.id.userStatus);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            listElement = itemView.findViewById(R.id.listElement);
        }
    }
    public ChatListAdapter(ArrayList<User> usersList)
    {
        this.usersList = usersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.user_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final User userListData = usersList.get(position);
        holder.userName.setText(userListData.getUserName());
        holder.userPhoneNumber.setText(userListData.getUserPhoneNumber());
        holder.userStatus.setText(userListData.getStatus());
        if(userListData.getStatus().equals("Offline"))
        {
            holder.listElement.setClickable(false);
            holder.userStatus.setTextColor(Color.parseColor("#D3DCD7"));
        }
        else
        {
            holder.userStatus.setTextColor(Color.parseColor("#0C9549"));
            holder.listElement.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(view.getContext(), "click on item: " + userListData.getUserName(), Toast.LENGTH_LONG).show();
                }
            });
        }
        holder.lastMessage.setText(userListData.getLastMessage());


    }
    @Override
    public int getItemCount()
    {
        return usersList.size();
    }
}
