package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class quesAnsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private  postAdapter postAdapter;
    private List<post>postList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques_ans);



        fab=findViewById(R.id.fab);

        progressBar=findViewById(R.id.progressBarQuesAns);
        recyclerView=findViewById(R.id.recyclerViewQuesAns);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(quesAnsActivity.this,askActivity.class);
                startActivity(intent);
            }
        });


        postList=new ArrayList<>();
        postAdapter =new postAdapter(quesAnsActivity.this,postList);
        recyclerView.setAdapter(postAdapter);
        
        readQuesPosts();

        //initialize
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationId);

        //when premium is selected
        bottomNavigationView.setSelectedItemId(R.id.newsfeedId);

        //listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {



                    case R.id.profileId:
                        Intent iii=new Intent(getApplicationContext(),profileActivity.class);
                        startActivity(iii);
                        overridePendingTransition(0,0);
                        return true;



                    case R.id.homeId:
                        Intent ii=new Intent(getApplicationContext(),firsthomeActivity.class);
                        startActivity(ii);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.newsfeedId:
                        return true;

                    case R.id.notificationId:
                        Intent iiii=new Intent(getApplicationContext(),notificationActivity.class);
                        startActivity(iiii);
                        overridePendingTransition(0,0);
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



    }

    private void readQuesPosts() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("questions post");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    post post=dataSnapshot.getValue(post.class);
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(quesAnsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}