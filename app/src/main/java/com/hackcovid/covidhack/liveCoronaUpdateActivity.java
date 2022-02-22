package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class liveCoronaUpdateActivity extends AppCompatActivity {



    FirebaseAuth mAuth;
    private DatabaseReference databaseReference,databaseReference2;
    EditText messegeEt;
    CardView sendBtn;
    String senderUid;
    RecyclerView massageAdapter;
    ArrayList<Messeges> messegesArrayList;
    chatAdapter chatAdapter;

    public  static String senderName,receiverName;
    public String sImage,rImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_corona_update);
        mAuth= FirebaseAuth.getInstance();
        senderUid=mAuth.getUid();

        databaseReference= FirebaseDatabase.getInstance().getReference("userDetails").child(senderUid);
        databaseReference2= FirebaseDatabase.getInstance().getReference("Chatroom");
        messegeEt=findViewById(R.id.messegeId);
        sendBtn=findViewById(R.id.sendMassegeId);
        messegesArrayList=new ArrayList<>();


        massageAdapter=findViewById(R.id.massageAdapterId);//recycler View

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        massageAdapter.setLayoutManager(linearLayoutManager);

        chatAdapter=new chatAdapter(liveCoronaUpdateActivity.this,messegesArrayList);

        massageAdapter.setAdapter(chatAdapter);









        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messege=messegeEt.getText().toString();

                if(messege.isEmpty())
                {
                    Toast.makeText(liveCoronaUpdateActivity.this,"Please Enter Valid Message",Toast.LENGTH_SHORT).show();
                    return;
                }



                messegeEt.setText("");
                Date date=new Date();

            }
        });



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                sImage= snapshot.child("imgUrl").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });







        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messegesArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Messeges prev_messeges=dataSnapshot.getValue(Messeges.class);
                    messegesArrayList.add(prev_messeges);
                }
                chatAdapter.notifyDataSetChanged();
                massageAdapter.smoothScrollToPosition(massageAdapter.getAdapter().getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}