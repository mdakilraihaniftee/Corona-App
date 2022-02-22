package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class fullScreenActivity extends AppCompatActivity {

    String postid,postPublisherId;
    //public ImageView questionImage;
    public TouchImageView questionImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        postPublisherId = intent.getStringExtra("publisher");

        questionImage=findViewById(R.id.fullImageId);

        readQuesImage();


    }


    private void readQuesImage() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("questions post").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                post post=snapshot.getValue(post.class);

                if ((post.getQuestionImage() != null)) {

                    //questionImage.setVisibility(View.VISIBLE);
                    //Glide.with(mContext).load(post.getQuestionImage()).into(holder.questionImage);
                    //Picasso.with(mContext).load(post.getQuestionImage()).into(holder.questionImage);
                    Picasso.with(fullScreenActivity.this)
                            .load(post.getQuestionImage())
                            .placeholder(R.mipmap.ic_launcher)
                            .into(questionImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(fullScreenActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

}