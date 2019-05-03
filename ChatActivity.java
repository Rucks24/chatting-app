package com.example.dell.tt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String recName,recId,recPlace;

    private TextView userName, userPlace;
    private ImageButton sendMsgBtn;
    private EditText MessageInput;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private String currUserId;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager ;
    private MessagesAdapter messagesAdapter;
    private RecyclerView userMessagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recId = getIntent().getExtras().get("visit_user_id").toString();
        recName = getIntent().getExtras().get("visit_user_name").toString();
        recPlace = getIntent().getExtras().get("visit_user_place").toString();
        RootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currUserId = mAuth.getCurrentUser().getUid();


        Toast.makeText(ChatActivity.this,recName,Toast.LENGTH_SHORT).show();

        InitializeControlizers();

        userName.setText(recName);
        userPlace.setText(recPlace);

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SendMessage();
            }
        });

    }

    private void InitializeControlizers()
    {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custom_chat_bar,null);
        actionBar.setCustomView(actionBarView);

        userName = (TextView) findViewById(R.id.cust_user_name);
        userPlace = (TextView) findViewById(R.id.cust_place);
        MessageInput = (EditText) findViewById(R.id.input_msg);

        sendMsgBtn = (ImageButton) findViewById(R.id.send_msg_btn);

        messagesAdapter = new MessagesAdapter(messagesList);
        userMessagesList = (RecyclerView) findViewById(R.id.mesg_list);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messagesAdapter);
    }


    protected void onStart()
    {
        super.onStart();

        RootRef.child("Message").child(currUserId).child(recId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Messages messages = dataSnapshot.getValue(Messages.class);

                        messagesList.add(messages);

                        messagesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


    private void SendMessage()
    {
        String messageText = MessageInput.getText().toString();
        if(TextUtils.isEmpty(messageText))
        {
            Toast.makeText(ChatActivity.this,"Please enter you message",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef = "Message/"+currUserId+"/"+recId;
            String messageReceiverRef = "Message/"+recId+"/"+currUserId;

            DatabaseReference UserMessageKeyRef = RootRef.child("Message").child(currUserId).child(recId).push();
            String messagePushId = UserMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message",messageText);
            messageTextBody.put("type","text");
            messageTextBody.put("from",currUserId);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef+"/"+messagePushId,messageTextBody);
            messageBodyDetails.put(messageReceiverRef+"/"+messagePushId,messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ChatActivity.this,"message sent succesfully",Toast.LENGTH_SHORT);
                    }
                    else
                    {
                        Toast.makeText(ChatActivity.this,"resend the message",Toast.LENGTH_SHORT);

                    }
                    MessageInput.setText("");
                }
            });




        }
    }
}
