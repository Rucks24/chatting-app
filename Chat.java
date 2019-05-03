package com.example.dell.tt;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment {


    private RecyclerView chatList;
    private DatabaseReference ChatsRef,UsersRef;
    private FirebaseAuth myAuth;
    public String currUserid;



    public Chat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        chatList= (RecyclerView) v.findViewById(R.id.chat_list);
        myAuth = FirebaseAuth.getInstance();
        currUserid = myAuth.getCurrentUser().getUid();
        ChatsRef = FirebaseDatabase.getInstance().getReference().child("my_app_user");
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }


    public void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(ChatsRef,User.class).build();

        FirebaseRecyclerAdapter<User,chatsViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, chatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final chatsViewHolder holder, int position, @NonNull User model) {
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
                                        Intent chatIntent = new Intent(getContext(),ChatActivity.class);
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
                    public chatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                        return new chatsViewHolder(view);
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
