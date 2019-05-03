package com.example.dell.tt;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {


    EditText name;
    CheckBox isGuide;
    Spinner dist,city;
    Button add,logOut;
    private FirebaseAuth mAuth;


    private DatabaseReference mDatabase;

    public Profile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


// ...
        final String currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        name = (EditText) v.findViewById(R.id.uName);
        isGuide = (CheckBox) v.findViewById(R.id.isguide);
        dist = (Spinner) v.findViewById(R.id.districtSpinner);
        city = (Spinner) v.findViewById(R.id.citySpinner);
        add = (Button) v.findViewById(R.id.add_btn);
        logOut = (Button) v.findViewById(R.id.log_out_btn);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().equals("") && isGuide.isChecked()) {
                    writeNewUser(currentuserid, name.getText().toString(), true, dist.getSelectedItem().toString(), city.getSelectedItem().toString());
                }
                else
                {
                    Toast.makeText(getContext(),"Please fill information..",Toast.LENGTH_SHORT);
                }
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return v;
    }

    private void writeNewUser(String userId, String name, Boolean isGuide,String dist,String city) {
        User user = new User(name, isGuide,dist,city,userId);

        mDatabase.child("my_app_user").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    System.out.println("It was successful");
                }
            }
        });
    }


}
