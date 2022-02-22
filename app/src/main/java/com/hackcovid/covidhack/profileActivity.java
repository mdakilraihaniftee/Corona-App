package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hackcovid.Notification.Token;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class profileActivity extends AppCompatActivity {


    private TextView username,gmail,phone,gender,UId,current_balance,username2,usertype,logout,askpost;
    private ImageView img,notification;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserId;
    private TextView death_no,affected_no,date,messageBtn;

    private DatabaseReference databaseReference,databaseReference2;
    String email,userUid,currentDate;
    int flag;
    ImageView icon;

    TextView yesBtn,noBtn;

    private Button message;


    private RecyclerView recyclerView;


    private  postAdapter postAdapter;
    private List<post> postList;

    SwitchCompat profileLockSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        userUid=mAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("userDetails");
        databaseReference2= FirebaseDatabase.getInstance().getReference("userDetails").child(userUid);


        mUser=mAuth.getCurrentUser();

        onlineUserId=mUser.getUid();


        //username=findViewById(R.id.profileNameId);
        username2=findViewById(R.id.fullnameId);
        profileLockSwitch=findViewById(R.id.switchProfileLock);
        gmail=findViewById(R.id.profileGmailId);
        gender=findViewById(R.id.profileGenderId);
        phone=findViewById(R.id.profilePhoneId);
        img=findViewById(R.id.profilePicId);

        progressBar=findViewById(R.id.progressBar4);

        /*
        death_no=(TextView)findViewById(R.id.textView6);
        affected_no=(TextView)findViewById(R.id.textView7);


        date = findViewById(R.id.dateId);
        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        date.setText(currentDate);


        /*
        if(flag==1)
        {
            usertype.setText("Premium");
            Picasso.with(profileActivity.this).load(R.drawable.premium_icon).into(icon);


        }

         */

        checkUserStatus();

        logout=findViewById(R.id.logoutId);

        /*
        Dialog dialog=new Dialog(profileActivity.this,R.style.Dialoge);
                dialog.setContentView(R.layout.dialoge_layout);


                yesBtn=dialog.findViewById(R.id.yesBtn);
                noBtn=dialog.findViewById(R.id.noBtn);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
         */


        profileLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    FirebaseDatabase.getInstance().getReference("userDetails").child(onlineUserId).child("profileLock").setValue(true);
                    Toast.makeText(getApplicationContext(),"Your profile has locked.None can see your information",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    FirebaseDatabase.getInstance().getReference("userDetails").child(onlineUserId).child("profileLock").setValue(false);
                    Toast.makeText(getApplicationContext(),"Your profile has unlocked.Everyone can see your information",Toast.LENGTH_SHORT).show();

                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder logoutDialog = new AlertDialog.Builder(profileActivity.this);
                //View dialogView= LayoutInflater.from(profileActivity.this).inflate(R.layout.dialoge_layout,null);


                //logoutDialog.setTitle("Reset Password ?");
                logoutDialog.setMessage("Are you sure want to Log Out?");
                //passwordResetDialog.setView(dialogView);

                logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);


                    }

                });
                logoutDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                logoutDialog.show();



            }
        });



        askpost=findViewById(R.id.askpost);

        askpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),askActivity.class);
                startActivity(i);
            }
        });

        /*
        messageBtn=findViewById(R.id.messagePage);

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),messagingActivity.class);
                startActivity(i);
            }
        });


         */







        recyclerView=findViewById(R.id.recyclerViewQuesAns);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);






        postList=new ArrayList<>();
        postAdapter =new postAdapter(profileActivity.this,postList);
        recyclerView.setAdapter(postAdapter);

        readQuesPosts();










        //firebase Data fatch

        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

            for(DataSnapshot dataSnapshot1:snapshot.getChildren())
                {
                    if(dataSnapshot1.child("email").getValue().equals(email))
                    {
                        username.setText(dataSnapshot1.child("name").getValue(String.class));
                        gmail.setText(dataSnapshot1.child("email").getValue(String.class));
                        gender.setText(dataSnapshot1.child("gender").getValue(String.class));
                        phone.setText(dataSnapshot1.child("mobileNo").getValue(String.class));
                        UId.setText(userUid);

                        Picasso.with(profileActivity.this)
                                .load(dataSnapshot1.child("imgUrl").getValue(String.class))
                                .placeholder(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(img);




                    }
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */

        /*    username.setText(snapshot.child(userUid).child("name").getValue(String.class));
                username.setText(snapshot.child("name").getValue(String.class));
                gmail.setText(snapshot.child(userUid).child("email").getValue(String.class));
                gender.setText(snapshot.child(userUid).child("gender").getValue(String.class));
                phone.setText(snapshot.child(userUid).child("mobileNo").getValue(String.class));


                Picasso.with(profileActivity.this)
                        .load(snapshot.child(userUid).child("imgUrl").getValue(String.class))
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(img);


               */


        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //username.setText(snapshot.child(userUid).child("name").getValue(String.class));
                //username.setText(snapshot.child("name").getValue(String.class));
                username2.setText(snapshot.child("name").getValue(String.class));
                gmail.setText(snapshot.child("email").getValue(String.class));
                gender.setText(snapshot.child("gender").getValue(String.class));
                phone.setText(snapshot.child("mobileNo").getValue(String.class));

                //current_balance.setText("BDT "+snapshot.child("balance").getValue(Integer.class));

                Picasso.with(profileActivity.this)
                        .load(snapshot.child("imgUrl").getValue(String.class))
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(img);


                //fetchData();




                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //fetchData();
                progressBar.setVisibility(View.GONE);
            }
        });





        //initialize
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationId);

        //when premium is selected
        bottomNavigationView.setSelectedItemId(R.id.profileId);

        //listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {


                    case R.id.chatId:

                        Intent i4=new Intent(getApplicationContext(),messagingActivity.class);
                        startActivity(i4);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profileId:
                        return true;


                    case R.id.homeId:

                        Intent ii=new Intent(getApplicationContext(),firsthomeActivity.class);
                        startActivity(ii);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.newsfeedId:
                        Intent iii=new Intent(getApplicationContext(),quesAnsActivity.class);
                        startActivity(iii);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.notificationId:
                        Intent iiii=new Intent(getApplicationContext(),notificationActivity.class);
                        startActivity(iiii);
                        overridePendingTransition(0,0);
                        return true;

                }


                return false;
            }
        });


        //checkUserStatus();

        updateToken(FirebaseInstanceId.getInstance().getToken());

    }



    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null)
        {
            String mUID=user.getUid();
            SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();
        }
        else
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();

        }

    }

    private void fetchData() {
        String url="https://coronavirus-19-api.herokuapp.com/countries/bangladesh";
        JsonObjectRequest jor=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try{

                            death_no.setText("Today Died: "+response.getString("todayDeaths"));
                            affected_no.setText("Today Affected : "+response.getString("todayCases"));

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(profileActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        );

        int retry_time=80000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(retry_time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        jor.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue= Volley.newRequestQueue(profileActivity.this);
        requestQueue.add(jor);






    }

    private void readQuesPosts() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("questions post");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(dataSnapshot.child("publisher").getValue(String.class).equals(onlineUserId)){
                        post post=dataSnapshot.getValue(post.class);
                        postList.add(post);
                    }

                }
                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(profileActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    public  void updateToken(String token)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken=new Token(token);

        ref.child(onlineUserId).setValue(mToken);

    }


}