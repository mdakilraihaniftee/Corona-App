package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class othersProfileActivity extends AppCompatActivity {

    private TextView gmail,phone,gender,username2,logout,askpost;
    private ImageView img;
    ProgressBar progressBar;

    private FirebaseUser mUser;
    private String onlineUserId;
    private TextView messageBtn;

    private DatabaseReference databaseReference2;
    String userUid;

    boolean meblockStatus,himblockStatus;


    private RecyclerView recyclerView;


    private  postAdapter postAdapter;
    private List<post> postList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

        Intent intent=getIntent();
        userUid=intent.getStringExtra("userid");



        databaseReference2= FirebaseDatabase.getInstance().getReference("userDetails").child(userUid);


        mUser=FirebaseAuth.getInstance().getCurrentUser();

        onlineUserId=mUser.getUid();

        username2=findViewById(R.id.fullnameId);
        gmail=findViewById(R.id.profileGmailId);
        gender=findViewById(R.id.profileGenderId);
        phone=findViewById(R.id.profilePhoneId);
        img=findViewById(R.id.profilePicId);

        progressBar=findViewById(R.id.progressBar4);

        //Toast.makeText(getApplicationContext(),"meblockstatus "+meblockStatus ,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"himblockstatus "+himblockStatus ,Toast.LENGTH_SHORT).show();
        //send message
        logout=findViewById(R.id.logoutId);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(meblockStatus)
                {
                    Toast.makeText(getApplicationContext(),"This person blocked you.You can't send messages",Toast.LENGTH_LONG).show();
                }
                else {

                    if(himblockStatus){
                        Toast.makeText(getApplicationContext(),"Please unblock to send messages",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Intent i = new Intent(getApplicationContext(), chatActivity.class);
                        i.putExtra("userid", userUid);
                        startActivity(i);
                    }

                }

            }
        });



        //block or unblock
        askpost=findViewById(R.id.askpost);

        askpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(himblockStatus){
                    AlertDialog.Builder logoutDialog = new AlertDialog.Builder(othersProfileActivity.this);

                    logoutDialog.setMessage("Are you want to unblock this person?");


                    logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UnblockUser();

                        }

                    });
                    logoutDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    logoutDialog.show();
                }

                else{
                    AlertDialog.Builder logoutDialog = new AlertDialog.Builder(othersProfileActivity.this);

                    logoutDialog.setMessage("Are you sure want to Block this person?");


                    logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            blockUser();
                            FirebaseDatabase.getInstance().getReference().child("friends").child(onlineUserId).child(userUid).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("friends").child(userUid).child(onlineUserId).removeValue();

                        }

                    });
                    logoutDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    logoutDialog.show();
                }

            }
        });


        recyclerView=findViewById(R.id.recyclerViewQuesAns);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        postList=new ArrayList<>();
        postAdapter =new postAdapter(othersProfileActivity.this,postList);
        recyclerView.setAdapter(postAdapter);

        readQuesPosts();

        //check if user is blocked or not by me
        checkblock();

        //check if i am blocked by the user

        imblockedornot();

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                username2.setText(snapshot.child("name").getValue(String.class));

                boolean profilelock=snapshot.child("profileLock").getValue(boolean.class);
                if(profilelock)
                {
                    gmail.setText("Locked");

                    phone.setText("Locked");

                }
                else
                {
                    gmail.setText(snapshot.child("email").getValue(String.class));

                    phone.setText(snapshot.child("mobileNo").getValue(String.class));
                }

                gender.setText(snapshot.child("gender").getValue(String.class));

                Picasso.with(othersProfileActivity.this)
                        .load(snapshot.child("imgUrl").getValue(String.class))
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(img);

                progressBar.setVisibility(View.GONE);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }


    private void imblockedornot(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("blocks");
        ref.child(userUid).orderByChild("blockId").equalTo(onlineUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren())
                        {
                            if(ds.exists()){

                                meblockStatus=true;

                            }
                            else
                            {

                                meblockStatus=false;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkblock() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("blocks");
        ref.child(onlineUserId).orderByChild("blockId").equalTo(userUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren())
                        {
                            if(ds.exists()){
                                askpost.setText("Unblock Message");
                                himblockStatus=true;
                            }
                            else
                            {
                                askpost.setText("Block Message");
                                himblockStatus=false;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




    }

    private void blockUser() {
        HashMap<String,String>hashMap=new HashMap<>();
        hashMap.put("blockId",userUid);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("blocks");
        reference.child(onlineUserId).child(userUid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            Toast.makeText(getApplicationContext(),"Blocked Successfully.You can't receive any messages from that person",Toast.LENGTH_LONG).show();

                askpost.setText("Unblock Message");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void UnblockUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("blocks");

        reference.child(onlineUserId).orderByChild("blockId").equalTo(userUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()){
                            if(ds.exists()){
                                ds.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //unblocked
                                        Toast.makeText(getApplicationContext(),"Unblocked Successfully.This person can again send you messages",Toast.LENGTH_LONG).show();

                                        askpost.setText("Block Message");

                                        Intent i = new Intent(othersProfileActivity.this, othersProfileActivity.class);
                                        i.putExtra("userid",userUid);
                                        //finish();
                                        //overridePendingTransition(0, 0);
                                        startActivity(i);
                                        //overridePendingTransition(0, 0);


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //unblocked
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void readQuesPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("questions post");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("publisher").getValue(String.class).equals(userUid)) {
                        post post = dataSnapshot.getValue(post.class);
                        postList.add(post);
                    }

                }
                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(othersProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}