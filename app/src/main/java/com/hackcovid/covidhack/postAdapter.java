package com.hackcovid.covidhack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder>{

    public Context mContext;
    public List<post> mPostList;
    private FirebaseUser firebaseUser;


    public postAdapter(Context mContext, List<post> mPostList) {
        this.mContext = mContext;
        this.mPostList = mPostList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.ques_retrieved_layout,parent,false);
        return new postAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        final post post=mPostList.get(position);

        if ((post.getQuestionImage()==null))
        {
            holder.questionImage.setVisibility(View.GONE);
            holder.tapImageText.setVisibility(View.GONE);
            holder.expandableTextView.setText(post.getQuestion());
            holder.topicTv.setText(post.getTopic());



        }
        else{
            holder.tapImageText.setVisibility(View.VISIBLE);
            holder.questionImage.setVisibility(View.VISIBLE);
            //Glide.with(mContext).load(post.getQuestionImage()).into(holder.questionImage);
            //Picasso.with(mContext).load(post.getQuestionImage()).into(holder.questionImage);
            Picasso.with(mContext)
                    .load(post.getQuestionImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.questionImage);

            holder.expandableTextView.setText(post.getQuestion());
            holder.topicTv.setText(post.getTopic());
            //holder.askedOnTv.setText(post.getDate());
        }

        String timestamp=post.getDate();
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime= android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        holder.askedOnTv.setText(pTime);



        publisherInformation(holder.publisher_profile_image,holder.asked_by_Tv,post.getPublisher());

        isliked(post.getPostid(),holder.like);
        isDisliked(post.getPostid(),holder.dislike);

        getLikes(holder.likes,post.getPostid());
        getdisLikes(holder.dislikes,post.getPostid());

        holder.asked_by_Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String onlineUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(!post.getPublisher().equals(onlineUserId)){
                    Intent intent=new Intent(mContext,othersProfileActivity.class);

                    intent.putExtra("userid",post.getPublisher());
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

        holder.publisher_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onlineUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(!post.getPublisher().equals(onlineUserId)){
                    Intent intent=new Intent(mContext,othersProfileActivity.class);

                    intent.putExtra("userid",post.getPublisher());
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



        holder.questionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mContext,fullScreenActivity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisher",post.getPublisher());
                mContext.startActivity(intent);

            }
        });



        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("like") && holder.dislike.getTag().equals("dislike")){
                    FirebaseDatabase.getInstance().getReference().child("likes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                    //addNotifications(post.getPublisher(), post.getPostid());
                }
                else if (holder.like.getTag().equals("like") && holder.dislike.getTag().equals("disliked")){
                    FirebaseDatabase.getInstance().getReference().child("dislikes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("likes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                    //addNotifications(post.getPublisher(), post.getPostid());

                }else {
                    FirebaseDatabase.getInstance().getReference().child("likes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.dislike.getTag().equals("dislike") && holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("dislikes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                }else if (holder.dislike.getTag().equals("dislike") && holder.like.getTag().equals("liked")){
                    FirebaseDatabase.getInstance().getReference().child("likes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("dislikes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("dislikes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,commentsActivity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisher",post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,commentsActivity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisher",post.getPublisher());
                mContext.startActivity(intent);
            }
        });


        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.inflate(R.menu.more_menu);
                if ((!post.getPublisher().equals(firebaseUser.getUid()))) {
                    popupMenu.getMenu().findItem(R.id.deletePostId).setVisible(false);

                }
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.deletePostId:
                                FirebaseDatabase.getInstance().getReference().child("dislikes").child(post.getPostid()).removeValue();
                                FirebaseDatabase.getInstance().getReference().child("likes").child(post.getPostid()).removeValue();
                                FirebaseDatabase.getInstance().getReference().child("comments").child(post.getPostid()).removeValue();
                                FirebaseDatabase.getInstance().getReference().child("questions post").child(post.getPostid()).removeValue();

                                return true;

                            case R.id.reportPostId:
                                Toast.makeText(mContext,"Report Done",Toast.LENGTH_SHORT).show();

                                return true;



                        }




                        return false;
                    }
                });


            }

        });






    }



    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView publisher_profile_image;
        public TextView  asked_by_Tv,likes,comments,dislikes,tapImageText;
        public ImageView more,questionImage,like,comment,dislike;
        public TextView topicTv,askedOnTv;
        public ExpandableTextView expandableTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            publisher_profile_image=itemView.findViewById(R.id.publisher_img);
            asked_by_Tv=itemView.findViewById(R.id.asked_by_Tv);
            tapImageText=itemView.findViewById(R.id.tapImageText);
            dislikes=itemView.findViewById(R.id.dislikes);
            likes=itemView.findViewById(R.id.likes);
            dislike=itemView.findViewById(R.id.dislike);
            like=itemView.findViewById(R.id.like);
            comments=itemView.findViewById(R.id.comments);
            comment=itemView.findViewById(R.id.comment);
            more=itemView.findViewById(R.id.more);
            topicTv=itemView.findViewById(R.id.topic_Tv);
            askedOnTv=itemView.findViewById(R.id.dateTv);
            questionImage=itemView.findViewById(R.id.questionImage);
            expandableTextView=itemView.findViewById(R.id.expand_Tv);


        }
    }

    private void  publisherInformation(ImageView publisherImage,TextView askedBy,String userid)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("userDetails").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userDetails user=snapshot.getValue(userDetails.class);
                //Glide.with(mContext).load(user.getImgUrl()).into(publisherImage);
                Picasso.with(mContext).load(user.getImgUrl()).into(publisherImage);

                askedBy.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void  isliked(String postid,ImageView imageView)
    {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists())
                {

                    imageView.setImageResource(R.drawable.liked);
                    imageView.setTag("liked");
                }
                else
                {
                    imageView.setImageResource(R.drawable.like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {




            }
        });
    }


    private void  isDisliked(String postid,ImageView imageView)
    {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("dislikes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists())
                {

                    imageView.setImageResource(R.drawable.disliked);
                    imageView.setTag("disliked");
                }
                else
                {
                    imageView.setImageResource(R.drawable.dislike);
                    imageView.setTag("dislike");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {




            }
        });
    }

    private void getLikes(final TextView likes,String postid)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long numberofLikes=snapshot.getChildrenCount();
                int NOL=(int) numberofLikes;
                if(NOL>1)
                {
                    likes.setText(snapshot.getChildrenCount()+ " likes");
                }
                else if(NOL==0)
                {
                    likes.setText("0 likes");
                }
                else {
                    likes.setText(snapshot.getChildrenCount() + " like");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getdisLikes(final TextView dislikes,String postid)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("dislikes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long numberofdisLikes=snapshot.getChildrenCount();
                int NOD=(int) numberofdisLikes;
                if(NOD>1)
                {
                    dislikes.setText(snapshot.getChildrenCount()+ " dislikes");
                }
                else if(NOD==0)
                {
                    dislikes.setText("0 dislikes");
                }
                else {
                    dislikes.setText(snapshot.getChildrenCount() + " dislike");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }





}
