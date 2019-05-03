package com.example.dell.tt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSearchResultActivity extends AppCompatActivity {

    private RecyclerView chatList;
    private DatabaseReference ChatsRef,UsersRef;
    private FirebaseAuth myAuth;
    public String currUserid,dist_req;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_result);

        chatList= (RecyclerView) findViewById(R.id.chat_list);
        myAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("my_app_user");
        currUserid = myAuth.getCurrentUser().getUid();
        ChatsRef = UsersRef;
        chatList.setLayoutManager(new LinearLayoutManager(UserSearchResultActivity.this));
    }


    public void onStart(){
        super.onStart();
        dist_req = getIntent().getExtras().get("User_search_query").toString();

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(ChatsRef.orderByChild("district").equalTo(dist_req),User.class).build();

        FirebaseRecyclerAdapter<User,Chat.chatsViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, Chat.chatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final Chat.chatsViewHolder holder, int position, @NonNull User model) {
                        final String usersIDs = getRef(position).getKey();
                        ChatsRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final String UserName = dataSnapshot.child("username").getValue().toString();
                                final String dist_place = dataSnapshot.child("district").getValue().toString();

                                holder.cuserName.setText(UserName);
                                holder.cplace.setText(dist_place);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent chatIntent = new Intent(UserSearchResultActivity.this,ChatActivity.class);
                                        chatIntent.putExtra("visit_user_id",usersIDs);
                                        chatIntent.putExtra("visit_user_name",UserName);
                                        chatIntent.putExtra("visit_user_place",dist_place);
                                        startActivity(chatIntent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public Chat.chatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                        return new Chat.chatsViewHolder(view);
                    }


                };
        chatList.setAdapter(adapter);
        adapter.startListening();;

    }



    public static class chatsViewHolder extends RecyclerView.ViewHolder{

        TextView cuserName,cplace;

        public chatsViewHolder(View itemView) {
            super(itemView);

            cuserName = itemView.findViewById(R.id.gname);
            cplace = itemView.findViewById(R.id.place);
        }
    }

}
