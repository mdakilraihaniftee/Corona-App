package com.hackcovid.covidhack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class notificationAdapter extends  RecyclerView.Adapter<notificationAdapter.ViewHolder>{

    private Context mContext;
    private List<notification> mCommentList;
    //private String postid;
    private FirebaseUser firebaseUser;

    public notificationAdapter(Context mContext, List<notification> mCommentList) {
        this.mContext = mContext;
        this.mCommentList = mCommentList;
        //this.postid = postid;
    }


    @NonNull
    @Override
    public notificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.notification_layout,parent,false);
        return new notificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull notificationAdapter.ViewHolder holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        notification notification=mCommentList.get(position);

        holder.commentor_comment.setText(notification.getComment());

        String timestamp=notification.getDate();
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime= android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        holder.commentDate.setText(pTime);


        getInformation(holder.commentor_profile_image,holder.commentorUserName,notification.getPublisherid());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,commentsActivity.class);
                intent.putExtra("postid",notification.getPostid());
                intent.putExtra("publisher",notification.getReceiverId());
                mContext.startActivity(intent);
            }
        });


        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String onlineUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                //child(notification.getNotificationId()).

                DatabaseReference deleteRef=FirebaseDatabase.getInstance().getReference("userDetails");
                deleteRef.child(onlineUserId).child("Notification").child(timestamp).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(mContext,"Notification Deleted",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        //done

        public CircleImageView commentor_profile_image;
        public TextView commentorUserName,commentor_comment,commentDate;
        public CardView cardView;
        public ImageView deleteIcon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            commentor_profile_image=itemView.findViewById(R.id.commentor_profile_img);
            commentorUserName=itemView.findViewById(R.id.commentorUserName);
            commentor_comment=itemView.findViewById(R.id.commentorComment);
            commentDate=itemView.findViewById(R.id.commentDate);
            cardView=itemView.findViewById(R.id.c11);
            deleteIcon=itemView.findViewById(R.id.deleteIcon);




        }
    }

    private void getInformation(CircleImageView circleImageView,TextView username, String publisherid)
    {
        //done
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("userDetails").child(publisherid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails user=snapshot.getValue(userDetails.class);
                Picasso.with(mContext).load(user.getImgUrl()).into(circleImageView);
                username.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}
