package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class commentsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CircleImageView circleImageView;
    private TextView textView;
    private EditText editText;
    private ProgressDialog loader;
    final String[] publisherName = new String[1];
    final String[] receiverId = new String[1];
    private String onlineUserId;

    public CircleImageView publisher_profile_image;
    public TextView  asked_by_Tv;
    public ImageView questionImage;
    public TextView topicTv,askedOnTv;
    public ExpandableTextView expandableTextView;


    DatabaseReference publisherRef,receiverRef;

    String postid,postPublisherId;

    private commentAdapter commentAdapter;
    private List<comment> commentList;

    APIService apiService;
    boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        postPublisherId = intent.getStringExtra("publisher");

        apiService= Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        circleImageView = findViewById(R.id.commentProfileImg);
        textView = findViewById(R.id.commentingPostTextview);
        editText = findViewById(R.id.addingComment);
        loader = new ProgressDialog(this);


        publisher_profile_image=findViewById(R.id.publisher_img);
        asked_by_Tv=findViewById(R.id.asked_by_Tv);
        topicTv=findViewById(R.id.topic_Tv);
        askedOnTv=findViewById(R.id.dateTv);
        questionImage=findViewById(R.id.questionImage);
        expandableTextView=findViewById(R.id.expand_Tv);





        onlineUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();

        //publisherRef= FirebaseDatabase.getInstance().getReference("userDetails").child(onlineUserId);
        receiverRef= FirebaseDatabase.getInstance().getReference("questions post").child(postid);

        receiverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiverId[0] =snapshot.child("publisher").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = editText.getText().toString();
                if (TextUtils.isEmpty(commentText)) {
                    editText.setError("Please type Something");
                }
                else{
                    addComment();
                }

            }


        });

        recyclerView = findViewById(R.id.recyclerViewComments);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        commentList=new ArrayList<>();
        commentAdapter=new commentAdapter(commentsActivity.this,commentList,postid);


        recyclerView.setAdapter(commentAdapter);
        readQuesPosts();

        /*
        questionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(commentsActivity.this,fullScreenActivity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisher",post.getPublisher());
                startActivity(intent);
            }
        });

         */


        DatabaseReference userreference= FirebaseDatabase.getInstance().getReference("userDetails").child(postPublisherId);

        userreference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    userDetails user=snapshot.getValue(userDetails.class);
                    //Glide.with(mContext).load(user.getImgUrl()).into(publisherImage);
                    Picasso.with(commentsActivity.this).load(user.getImgUrl()).into(publisher_profile_image);

                    asked_by_Tv.setText(user.getName());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(commentsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });




        getImage();

        readComments();

    }

    private void readComments() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("comments").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    comment comment=dataSnapshot.getValue(comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void addComment() {
        loader.setMessage("Adding a comment");
        loader.setCanceledOnTouchOutside(false);
        loader.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("comments").child(postid);
        //DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Notification").child(receiverId[0]);
        String commentid = reference.push().getKey();
        String date = ""+System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", editText.getText().toString());
        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("commentid", commentid);
        hashMap.put("postid", postid);
        hashMap.put("date", date);

        notify=true;

        reference.child(commentid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(commentsActivity.this, "Comment Added Successfully", Toast.LENGTH_SHORT).show();
                    final DatabaseReference databasee = FirebaseDatabase.getInstance().getReference("userDetails").child(onlineUserId);

                    databasee.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userDetails user=snapshot.getValue(userDetails.class);

                            if(notify && !postPublisherId.equals(onlineUserId))
                            {
                                //Toast.makeText(commentsActivity.this, "send notification" , Toast.LENGTH_SHORT).show();
                                sendNotification(postPublisherId, user.getName(),"Commented On Your Post");
                            }
                            notify=false;

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Toast.makeText(commentsActivity.this, "i am here", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    Toast.makeText(commentsActivity.this, "Error Adding Message", Toast.LENGTH_SHORT).show();

                }

                editText.setText("");

            }
        });

        if(!receiverId[0].equals(onlineUserId))
        {
            addNotification();
        }

        loader.dismiss();


    }

    private void addNotification()
    {

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("userDetails");

        String date = ""+System.currentTimeMillis();
        String notificationid = reference2.push().getKey();

        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("notificationid", notificationid);

        hashMap2.put("comment", "commented on your post.");
        hashMap2.put("date", date);

        hashMap2.put("publisherid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap2.put("receiverId",receiverId[0]);
        hashMap2.put("postid", postid);


        reference2.child(receiverId[0]).child("Notification").child(date).setValue(hashMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(commentsActivity.this, "Notification Added Successfully", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(commentsActivity.this, "Error Adding notification", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }


    private void getImage() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("userDetails").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails user = snapshot.getValue(userDetails.class);
                //Glide.with(commentsActivity.this).load(user.getImgUrl()).into(circleImageView);
                Picasso.with(commentsActivity.this).load(user.getImgUrl()).into(circleImageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(commentsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void readQuesPosts() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("questions post").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                post post=snapshot.getValue(post.class);

                if ((post.getQuestionImage() == null)) {
                    questionImage.setVisibility(View.GONE);
                    expandableTextView.setText(post.getQuestion());
                    topicTv.setText(post.getTopic());
                    //askedOnTv.setText(post.getDate());
                }
                else {
                    questionImage.setVisibility(View.VISIBLE);
                    //Glide.with(mContext).load(post.getQuestionImage()).into(holder.questionImage);
                    //Picasso.with(mContext).load(post.getQuestionImage()).into(holder.questionImage);
                    Picasso.with(commentsActivity.this)
                            .load(post.getQuestionImage())
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .centerCrop()
                            .into(questionImage);

                    expandableTextView.setText(post.getQuestion());
                    topicTv.setText(post.getTopic());

                }
                String timestamp=post.getDate();
                Calendar calendar=Calendar.getInstance(Locale.getDefault());
                calendar.setTimeInMillis(Long.parseLong(timestamp));
                String pTime= android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                askedOnTv.setText(pTime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(commentsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void sendNotification(String receiverid, String name, String message) {
        DatabaseReference allTokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query =allTokens.orderByKey().equalTo(receiverid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Token token=ds.getValue(Token.class);
                    Data data=new Data(onlineUserId,""+message,name,receiverid,"commentNotification",R.drawable.app_icon);

                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotifcation(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(commentsActivity.this,""+response.message(),Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                    Toast.makeText(commentsActivity.this,"error sending notification",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

