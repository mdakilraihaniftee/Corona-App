package com.hackcovid.covidhack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class commentAdapter  extends  RecyclerView.Adapter<commentAdapter.ViewHolder> {

    private Context mContext;
    private List<comment> mCommentList;
    private String postid;
    private FirebaseUser firebaseUser;

    public commentAdapter(Context mContext, List<comment> mCommentList, String postid) {
        this.mContext = mContext;
        this.mCommentList = mCommentList;
        this.postid = postid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.comments_layout,parent,false);
        return new commentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        comment comment=mCommentList.get(position);

        holder.commentor_comment.setText(comment.getComment());


        String timestamp=comment.getDate();
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime= android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        holder.commentDate.setText(pTime);



        getInformation(holder.commentor_profile_image,holder.commentorUserName,comment.getPublisher());

        holder.commentorUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onlineUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(!comment.getPublisher().equals(onlineUserId)){
                    Intent intent=new Intent(mContext,othersProfileActivity.class);

                    intent.putExtra("userid",comment.getPublisher());
                    mContext.startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(mContext,profileActivity.class);

                    //intent.putExtra("userid",comment.getPublisher());
                    mContext.startActivity(intent);
                }



            }
        });


    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView commentor_profile_image;
        public TextView commentorUserName,commentor_comment,commentDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            commentor_profile_image=itemView.findViewById(R.id.commentor_profile_img);
            commentorUserName=itemView.findViewById(R.id.commentorUserName);
            commentor_comment=itemView.findViewById(R.id.commentorComment);
            commentDate=itemView.findViewById(R.id.commentDate);




        }
    }

    private void getInformation(CircleImageView circleImageView,TextView username, String publisherid)
    {
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
