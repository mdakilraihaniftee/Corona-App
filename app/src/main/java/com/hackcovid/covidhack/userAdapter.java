package com.hackcovid.covidhack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class userAdapter  extends RecyclerView.Adapter<userAdapter.ViewHolder> {
    public Context mContext;
    public List<userDetails> mUsersList;

    public userAdapter(Context mContext, List<userDetails> mUsersList) {
        this.mContext = mContext;
        this.mUsersList = mUsersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new userAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Toast.makeText(mContext,"inside adapter",Toast.LENGTH_SHORT).show();

        final userDetails user=mUsersList.get(position);
        holder.username.setText(user.getName());
        Picasso.with(mContext)
                .load(user.getImgUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.profile_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,othersProfileActivity.class);

                intent.putExtra("userid",user.getUserId());
                mContext.startActivity(intent);


            }
        });

    }



    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView profile_image;
        public TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);

        }
    }
}
