package com.example.dell.tt;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>
{
    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public MessagesAdapter(List<Messages> userMessagesList){
        this.userMessagesList = userMessagesList;
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder
    {

        public TextView senderMessageText,receiverMessageText;

        public MessagesViewHolder(View itemView) {
            super(itemView);



            senderMessageText = (TextView) itemView.findViewById(R.id.sender_msg_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_msg_text);

        }


    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_message_layout,parent,false);

        mAuth = FirebaseAuth.getInstance();

        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int i)
    {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(i);

        String fromUserID = messages.getFrom();
        String fromMessageType = messages.getType();

        usersRef = FirebaseDatabase.getInstance().getReference().child("User").child(fromUserID);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (fromMessageType.equals("text"))
        {
            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            //holder.receiverMessageText.setVisibility(View.INVISIBLE);

            if (fromUserID.equals(messageSenderId))
            {
                holder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                holder.senderMessageText.setTextColor(Color.BLACK);
                holder.senderMessageText.setText(messages.getMessage());
            }
            else
            {
                holder.senderMessageText.setVisibility(View.INVISIBLE);

                holder.receiverMessageText.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
                holder.receiverMessageText.setTextColor(Color.BLACK);
                holder.receiverMessageText.setText(messages.getMessage());
            }

        }

    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }
}
