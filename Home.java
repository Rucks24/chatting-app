package com.example.dell.tt;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    private Toolbar mToolbar;
    private RecyclerView FindFriendsRecyclerlist;
    private DatabaseReference UsersRef;
    private SearchView sv;
    Query fquery;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("my_app_user");
        sv = (SearchView) v.findViewById(R.id.search);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
// do something on text submit
                fquery = UsersRef.child("my_app_user").orderByChild("district").equalTo(query);

                Intent myChatsIntent = new Intent(getContext(),UserSearchResultActivity.class);
                myChatsIntent.putExtra("User_search_query",query);
                startActivity(myChatsIntent);

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
// do something when text changes
                return false;
            }
        });


        FindFriendsRecyclerlist = (RecyclerView) v.findViewById(R.id.find_guides_recyclerview_list);
        FindFriendsRecyclerlist.setLayoutManager(new LinearLayoutManager(v.getContext()));



        return v;
    }

    public void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(UsersRef,User.class).build();

        FirebaseRecyclerAdapter<User,FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, Home.FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Home.FindFriendViewHolder holder, final int position, @NonNull User model) {
                        holder.userName.setText(model.getName());
                        holder.place.setText(model.getDistrict());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String visit_user_id = getRef(position).getKey();

                                Intent profileIntent = new Intent(v.getContext(),ProfileActivity.class);
                                profileIntent.putExtra("visit_user_id",visit_user_id);
                                startActivity(profileIntent);
                            }
                        });
                    }

                    @Override
                    public Home.FindFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                        Home.FindFriendViewHolder viewHolder = new Home.FindFriendViewHolder(view);
                        return viewHolder;
                    }
                };
        FindFriendsRecyclerlist.setAdapter(adapter);

        adapter.startListening();
    }

    public static class FindFriendViewHolder extends RecyclerView.ViewHolder{
        TextView userName,place;
        public FindFriendViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.gname);
            place = itemView.findViewById(R.id.place);

        }
    }


}
