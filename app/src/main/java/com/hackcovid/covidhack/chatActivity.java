package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hackcovid.Notification.APIService;
import com.hackcovid.Notification.Client;
import com.hackcovid.Notification.Data;
import com.hackcovid.Notification.Response;
import com.hackcovid.Notification.Sender;
import com.hackcovid.Notification.Token;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class chatActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username,status;
    ImageView backButton;

    Intent intent;

    FirebaseUser onlineUser;
    DatabaseReference reference,chatReference;

    public static  String rImage;

    String senderid,receiverid,senderroom,receiverroom;

    CardView sendBtn;
    EditText edtMessage;

    ValueEventListener seenListener;

    RecyclerView messageRecyclerview;
    ArrayList<Messeges>messegesArrayList;

    chatAdapter chatAdapter;
    boolean meblockStatus,himblockStatus;

    APIService apiService;
    boolean notify=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messegesArrayList=new ArrayList<>();

        profile_image=findViewById(R.id.profileImage);
        username=findViewById(R.id.username);
        status=findViewById(R.id.onlineStatusId);
        backButton=findViewById(R.id.backButton);

        messageRecyclerview=findViewById(R.id.messageRecyclerView);
        messageRecyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        apiService= Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);


        messageRecyclerview.setLayoutManager(linearLayoutManager);






        onlineUser= FirebaseAuth.getInstance().getCurrentUser();
        senderid=onlineUser.getUid();

        intent=getIntent();
        receiverid=intent.getStringExtra("userid");

        senderroom=senderid+receiverid;
        receiverroom=receiverid+senderid;




        sendBtn=findViewById(R.id.sendBtn);
        edtMessage=findViewById(R.id.edtMessage);

        imblockedornot();


        reference= FirebaseDatabase.getInstance().getReference("userDetails").child(receiverid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails user=snapshot.getValue(userDetails.class);
                username.setText(user.getName());

                String timestamp=user.getStatus();
                if(timestamp.equals("online")){
                    status.setText("online");
                }
                else {
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(timestamp));
                    String pTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                    status.setText("Last seen at: "+pTime);
                }

                Picasso.with(chatActivity.this)
                        .load(user.getImgUrl())
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(profile_image);
                rImage=user.getImgUrl();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        chatReference= FirebaseDatabase.getInstance().getReference("chats").child(senderroom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messegesArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Messeges messeges=dataSnapshot.getValue(Messeges.class);
                    messegesArrayList.add(messeges);
                }
                chatAdapter=new chatAdapter(chatActivity.this,messegesArrayList);
                messageRecyclerview.setAdapter(chatAdapter);

                chatAdapter.notifyDataSetChanged();
                messageRecyclerview.smoothScrollToPosition(messageRecyclerview.getAdapter().getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),othersProfileActivity.class);

                intent.putExtra("userid",receiverid);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),messagingActivity.class);
                startActivity(intent);
            }
        });



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(meblockStatus)
                {
                    AlertDialog.Builder logoutDialog = new AlertDialog.Builder(chatActivity.this);

                    logoutDialog.setMessage("You are blocked by that person and can't Reply");


                    logoutDialog.setPositiveButton("back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(getApplicationContext(),messagingActivity.class);
                            startActivity(intent);

                        }

                    });

                    logoutDialog.show();

                }
                else
                {
                    notify=true;
                    String message=edtMessage.getText().toString();

                    if(message.isEmpty()){
                        Toast.makeText(chatActivity.this,"Please enter a valid Message",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    edtMessage.setText("");



                    String date=""+System.currentTimeMillis();
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("chats").child(senderroom).child("messages");


                    String msg =message;
                    final DatabaseReference databasee = FirebaseDatabase.getInstance().getReference("userDetails").child(onlineUser.getUid());

                    databasee.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userDetails user=snapshot.getValue(userDetails.class);

                            if(notify)
                            {
                                sendNotification(receiverid,user.getName(),message);
                            }
                            notify=false;

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





                    String msgKey=databaseReference.push().getKey();
                    Messeges messeges=new Messeges(message,senderid,date,receiverid,msgKey,false);
                    lastMessage lastMessagesender=new lastMessage(senderid,message,date,receiverid,receiverid,false);
                    lastMessage lastMessagereceiver=new lastMessage(receiverid,message,date,senderid,senderid,false);

                    databaseReference.child(msgKey).setValue(messeges).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            FirebaseDatabase.getInstance().getReference("chats").child(receiverroom).child("messages").child(msgKey)
                                    .setValue(messeges).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseDatabase.getInstance().getReference("friends").child(senderid).child(receiverid).setValue(lastMessagesender);
                                    FirebaseDatabase.getInstance().getReference("friends").child(receiverid).child(senderid).setValue(lastMessagereceiver);
                                    //Toast.makeText(chatActivity.this,"i am here",Toast.LENGTH_SHORT).show();
                                }
                            });



                            //Toast.makeText(chatActivity.this,"i am here",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }


        });

        //seenMessage2();

        seenMessage();



    }

    String mDAte=""+System.currentTimeMillis();

    private void sendNotification(String receiverid, String name, String message) {
        DatabaseReference allTokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query =allTokens.orderByKey().equalTo(receiverid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Token token=ds.getValue(Token.class);
                    Data data=new Data(onlineUser.getUid(),name+":\n"+message,"New Message",receiverid,"ChatNotification",R.drawable.app_icon);

                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotifcation(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    //Toast.makeText(chatActivity.this,""+response.message(),Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                    Toast.makeText(chatActivity.this,"error sending notification",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //Toolbar toolbar=findViewById()


    private void seenMessage()
    {
        reference=FirebaseDatabase.getInstance().getReference("chats").child(receiverroom).child("messages");
        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Messeges messeges=ds.getValue(Messeges.class);
                    if(senderid.equals(messeges.getReceiverId()) && receiverid.equals(messeges.getSenderId()))
                    {
                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        ds.getRef().updateChildren(hashMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*
    private void seenMessage2()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("friends").child(senderid).child(receiverid);
        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                lastMessage messages=snapshot.getValue(lastMessage.class);
                    if(senderid.equals(messages.getReceiverId()) && receiverid.equals(messages.getSenderId()))
                    {
                        FirebaseDatabase.getInstance().getReference("friends").child(senderid).child(receiverid).child("isseen").setValue(true);
                        FirebaseDatabase.getInstance().getReference("friends").child(receiverid).child(senderid).child("isseen").setValue(true);

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

     */


    private  void status(String status)
    {
        FirebaseDatabase.getInstance().getReference("userDetails").child(onlineUser.getUid())
                .child("status").setValue(status);

    }

    @Override
    protected void onStart() {
        status("online");
        super.onStart();
    }

    @Override
    protected void onResume() {
        status("online");
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        status(mDAte);
        if (seenListener != null && reference!=null) {
            //DatabaseReference reference=FirebaseDatabase.getInstance().getReference("userDetails").child(receiverid);
            reference.removeEventListener(seenListener);
        }


    }


    private void imblockedornot(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("blocks");
        ref.child(receiverid).orderByChild("blockId").equalTo(senderid)
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
}