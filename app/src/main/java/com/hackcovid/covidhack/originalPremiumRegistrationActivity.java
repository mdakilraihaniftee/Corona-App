package com.hackcovid.covidhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class originalPremiumRegistrationActivity extends AppCompatActivity {

    private EditText p_emailEt,nameEt,password_et;
    private Button premiumSignUpButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    DatabaseReference premium_database_user;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_premium_registration);

        Intent intent=getIntent();
        email=intent.getStringExtra("email");

        mAuth = FirebaseAuth.getInstance();
        premium_database_user= FirebaseDatabase.getInstance().getReference("premiumUserDetails");

        nameEt=(EditText)findViewById(R.id.premiumNameId);
        p_emailEt=(EditText)findViewById(R.id.premiumEmailId);

        progressBar=(ProgressBar)findViewById(R.id.premiumSignUpProgressBarId);

        premiumSignUpButton=(Button)findViewById(R.id.saveInfoPremium);

        premiumSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                premiumUserRegister();

            }
        });



    }

    private void premiumUserRegister(){

        String name=nameEt.getText().toString().trim();
        String premium_email=p_emailEt.getText().toString().trim();

        if(name.isEmpty())
        {
            nameEt.setText("Required");
            nameEt.requestFocus();
            return;
        }

        if(premium_email.isEmpty())
        {
            p_emailEt.setError("Enter an email address");
            p_emailEt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(premium_email).matches())
        {
            p_emailEt.setError("Enter a valid email address");
            p_emailEt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        PremiumUser premiumUser=new PremiumUser(name,premium_email);

        String key=mAuth.getCurrentUser().getUid();
        premium_database_user.child(key).setValue(premiumUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getApplicationContext(),"New Premium User Added",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"Registration is successful", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);


                Intent i=new Intent(originalPremiumRegistrationActivity.this,premiumActivity.class);
                i.putExtra("email",email);
                startActivity(i);
            }
        });

    }

}