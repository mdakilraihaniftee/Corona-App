package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class balanceLoadActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private EditText amountEt;
    Button loadBtn;
    String senderUid;
    int prev_balance,new_load_amount,current_balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_load);

        mAuth= FirebaseAuth.getInstance();
        senderUid=mAuth.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("userDetails").child(senderUid);


        loadBtn=findViewById(R.id.loadAmountId);
        amountEt=findViewById(R.id.amountId);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                prev_balance=snapshot.child("balance").getValue(Integer.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_load=amountEt.getText().toString();
                new_load_amount=Integer.parseInt(new_load);
                current_balance=new_load_amount+prev_balance;

                amountEt.setText("");

                databaseReference.child("balance").setValue(current_balance).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Recharged", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(balanceLoadActivity.this,profileActivity.class);
                        startActivity(i);

                    }
                });



            }
        });

    }
}