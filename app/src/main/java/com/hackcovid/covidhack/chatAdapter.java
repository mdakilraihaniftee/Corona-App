package com.hackcovid.covidhack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hackcovid.covidhack.chatActivity.rImage;

public class chatAdapter  extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messeges>messegesArrayList;
    int item_send=1,item_receive=2;
    String imgUrl;

    public chatAdapter(Context context, ArrayList<Messeges> messegesArrayList) {
        this.context = context;
        this.messegesArrayList = messegesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==item_send)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new SenderViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return new ReceiverViewHolder(view);

        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messeges messeges=messegesArrayList.get(position);
        if(holder.getClass()==SenderViewHolder.class)
        {
            SenderViewHolder viewHolder=(SenderViewHolder)holder;
            viewHolder.message.setText(messeges.getMessege());

            if(position==messegesArrayList.size()-1){
                if(messeges.isIsseen()){

                    viewHolder.seen.setText("seen");
                }
                else {
                    viewHolder.seen.setText("Delivered");
                }
            }
            else
            {
                viewHolder.seen.setVisibility(View.GONE);
            }




            /*

            DatabaseReference lastchatreference=FirebaseDatabase.getInstance().getReference("friends").child(messeges.getSenderId()).child(messeges.getReceiverId());
            lastchatreference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lastMessage lastMessage=snapshot.getValue(lastMessage.class);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

             */

        }
        else
        {
            ReceiverViewHolder viewHolder=(ReceiverViewHolder)holder;
            //viewHolder.name.setText(messeges.getSenderId());
            viewHolder.message.setText(messeges.getMessege());

            Picasso.with(context).load(rImage).into(viewHolder.circleImageView);
        }



    }

    @Override
    public int getItemCount() {
        return messegesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messeges messeges=messegesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messeges.getSenderId()))
        {
            return item_send;
        }
        else
        {
            return item_receive;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;

        TextView name,message,seen;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            //name=itemView.findViewById(R.id.senderNameLayoutId);
            message=itemView.findViewById(R.id.senderMessegeLayoutId);
            seen=itemView.findViewById(R.id.seenStatusId);

        }
    }
    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView name,message;
        CircleImageView circleImageView;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            //name=itemView.findViewById(R.id.rcvrNameLayoutId);
            message=itemView.findViewById(R.id.rcvrMessegeLayoutId);

            circleImageView=itemView.findViewById(R.id.receiver_profile_image_id);
        }
    }


}
