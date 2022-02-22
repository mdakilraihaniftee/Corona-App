package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class  notificationActivity extends AppCompatActivity {

    SwitchCompat notificationSwitch;


    //use shared preference to save the state of switch
    SharedPreferences sp;
    SharedPreferences.Editor editor; //to edit value of shared pref


    // constant for topic
    private static final String TOPIC_POST_NOTIFICATION = "POST";


    private RecyclerView recyclerView;
    private CircleImageView circleImageView;
    private TextView textView;
    private EditText editText;
    private ProgressDialog loader;
    String onlineUserId;

    private notificationAdapter notificationAdapter;
    private List<notification> notificationList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationSwitch=findViewById(R.id.switchNotification);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationId);

        //when premium is selected
        bottomNavigationView.setSelectedItemId(R.id.notificationId);

        //listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {



                    case R.id.profileId:

                        Intent i = new Intent(getApplicationContext(), profileActivity.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.homeId:
                        Intent ii = new Intent(getApplicationContext(), firsthomeActivity.class);
                        startActivity(ii);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.newsfeedId:
                        Intent iii = new Intent(getApplicationContext(), quesAnsActivity.class);
                        startActivity(iii);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.notificationId:

                        return true;

                    case R.id.chatId:

                        Intent i4=new Intent(getApplicationContext(),messagingActivity.class);
                        startActivity(i4);
                        overridePendingTransition(0,0);
                        return true;

                }


                return false;
            }
        });

        //initialize shared Preference

        sp=getSharedPreferences("Notification_SP",MODE_PRIVATE);
        boolean isPostEnabled=sp.getBoolean(""+TOPIC_POST_NOTIFICATION,false);

        //if enabled check switch ,otherwise uncheck switch- by default unchecked/false

        if(isPostEnabled){
            notificationSwitch.setChecked(true);
        }
        else {
            notificationSwitch.setChecked(false);
        }




        //implement switch change listener
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //edit switch state

                editor=sp.edit();
                editor.putBoolean(""+TOPIC_POST_NOTIFICATION,isChecked);
                editor.apply();

                if(isChecked)
                {
                    subscribeNotification();

                }
                else
                {
                    unsubscribeNotifiacation();
                }
            }
        });

        //retrive notification
        onlineUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        loader = new ProgressDialog(this);


        recyclerView = findViewById(R.id.recyclerViewNotifications);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        notificationList=new ArrayList<>();
        notificationAdapter=new notificationAdapter(notificationActivity.this,notificationList);
        recyclerView.setAdapter(notificationAdapter);



        readComments();

    }

    //fetch notification
    private void readComments() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("userDetails").child(onlineUserId).child("Notification");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    notification notification=dataSnapshot.getValue(notification.class);
                    notificationList.add(notification);
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }






    private void unsubscribeNotifiacation() {
    //unsubscribe to a topic(POST) to disable it's notification
        FirebaseMessaging.getInstance().unsubscribeFromTopic(""+TOPIC_POST_NOTIFICATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg= "You will not receive post notifications";
                        if(!task.isSuccessful())
                        {
                            msg="Unsubscription failed";
                        }
                        Toast.makeText(notificationActivity.this,msg,Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void subscribeNotification() {

        //subscribe to a topic(POST) to enable it's notification
        FirebaseMessaging.getInstance().subscribeToTopic(""+TOPIC_POST_NOTIFICATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg= "You will receive post notifications";
                        if(!task.isSuccessful())
                        {
                            msg="Subscription failed";
                        }
                        Toast.makeText(notificationActivity.this,msg,Toast.LENGTH_SHORT).show();

                    }
                });




    }
}