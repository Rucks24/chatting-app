package com.example.dell.tt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private String receiverUserId , currentUser , CurrentState;

    TextView userProName,userProPlace;
    Button sendMsg;
    private DatabaseReference UsersRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        userProName = (TextView) findViewById(R.id.user_name);
        userProPlace = (TextView) findViewById(R.id.place);
        sendMsg = (Button) findViewById(R.id.send_msg_req_btn);
        CurrentState = "new";
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("my_app_user");


        RetriveUserInfo();
    }

    private void RetriveUserInfo() {
        UsersRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String UserName = dataSnapshot.child("username").getValue().toString();
                String dist_place = dataSnapshot.child("district").getValue().toString();

                userProName.setText(UserName);
                userProPlace.setText(dist_place);

                //ManageChatReq();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
