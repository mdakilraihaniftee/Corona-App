package com.hackcovid.covidhack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class fragment2 extends Fragment {

    private RecyclerView recyclerView;
    private userAdapter userAdapter;
    private List<userDetails>mUsers;

    EditText SearchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fragment2,container,false);
        recyclerView=view.findViewById(R.id.users_recycler_viewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers=new ArrayList<>();

        readUsers();

        SearchEditText=view.findViewById(R.id.searchEditTextId);

        SearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());
            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    private void searchUsers(String s) {

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference("userDetails").orderByChild("lowercaseName")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    userDetails user=dataSnapshot.getValue(userDetails.class);

                    if(!user.getUserId().equals(firebaseUser.getUid()))
                    {
                        mUsers.add(user);
                    }
                    userAdapter=new userAdapter(getContext(),mUsers);
                    recyclerView.setAdapter(userAdapter);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readUsers() {

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("userDetails");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                mUsers.clear();
                if(SearchEditText.getText().toString().equals("")) {
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {

                        userDetails user = snapshot.getValue(userDetails.class);
                        assert user != null;
                        assert firebaseUser != null;
                        if (!user.getUserId().equals(firebaseUser.getUid())) {
                            mUsers.add(user);
                        }

                    }
                }

                //Toast.makeText(getContext(),"I am here",Toast.LENGTH_SHORT).show();
                userAdapter =new userAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
                //Toast.makeText(getContext(),""+mUsers.size(),Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //Toast.makeText(getContext(),"I am here",Toast.LENGTH_SHORT).show();
            }
        });

    }
}