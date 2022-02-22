package com.hackcovid.covidhack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class fragment1 extends Fragment {


    private RecyclerView recyclerView;
    private  friendAdapter friendAdapter;
    private List<lastMessage>mUser;


    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private List<String> userList;
    String senderId;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fragment1,container,false);

        recyclerView=view.findViewById(R.id.chats_recycler_viewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        senderId=firebaseUser.getUid();

        //Toast.makeText(getContext(),""+mUser.size(),Toast.LENGTH_SHORT).show();

        mUser=new ArrayList<>();


        databaseReference= FirebaseDatabase.getInstance().getReference("friends").child(senderId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    lastMessage lMessage=dataSnapshot.getValue(lastMessage.class);
                    mUser.add(lMessage);


                }


                friendAdapter =new friendAdapter(getContext(),mUser);
                recyclerView.setAdapter(friendAdapter);



                //Collections.sort(mUser);
                friendAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;

    }
}