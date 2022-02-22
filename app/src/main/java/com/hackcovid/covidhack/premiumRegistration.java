package com.hackcovid.covidhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class premiumRegistration extends AppCompatActivity implements View.OnClickListener {
    private Button premiumReg,load,signIn,profile;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_registration);

        Intent intent=getIntent();
        email=intent.getStringExtra("email");

        //premiumReg=(Button)findViewById(R.id.premiumRegistrationId);
        //premiumReg.setOnClickListener(this);

        load=(Button)findViewById(R.id.loadBalaceId);
        load.setOnClickListener(this);

        //signIn=(Button)findViewById(R.id.signInId);
        //signIn.setOnClickListener(this);

        profile=(Button)findViewById(R.id.gotoProfileId);
        profile.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gotoProfileId:
                Intent intent=new Intent(premiumRegistration.this,profileActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
                break;

            case R.id.loadBalaceId:
                Intent i=new Intent(premiumRegistration.this,balanceLoadActivity.class);
                startActivity(i);
                break;

            /*case R.id.signInId:
                Intent ii=new Intent(premiumRegistration.this,MainActivity.class);
                startActivity(ii);
                break;

            case R.id.premiumRegistrationprofileId:
                Intent iii=new Intent(getApplicationContext(),profileActivity.class);
                iii.putExtra("email",email);
                startActivity(iii);
                break;


             */

        }
    }
}