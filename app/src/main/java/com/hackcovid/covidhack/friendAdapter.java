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
import androidx.recyclerview.widget.RecyclerView;

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

public class friendAdapter extends RecyclerView.Adapter<friendAdapter.ViewHolder>{
    public Context mContext;
    public List<lastMessage> mUsersList;

    public friendAdapter(Context mContext, List<lastMessage> mUsersList) {
        this.mContext = mContext;
        this.mUsersList = mUsersList;
    }

    @NonNull
    @Override
    public friendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.chats_user_layout,parent,false);
        return new friendAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull friendAdapter.ViewHolder holder, int position) {
        //Toast.makeText(mContext,"inside adapter",Toast.LENGTH_SHORT).show();

        final lastMessage lmsg = mUsersList.get(position);


        holder.commentor_comment.setText(lmsg.last_message);


        String timestamp=lmsg.getTimestamp();
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime= android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        holder.commentDate.setText(pTime);



        getInformation(holder.commentor_profile_image,holder.commentorUserName,lmsg.getMainId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,chatActivity.class);

                intent.putExtra("userid",lmsg.getMainId());
                mContext.startActivity(intent);


            }
        });

    }



    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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




