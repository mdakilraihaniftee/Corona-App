package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class firsthomeActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView backButton;
    private CardView card1,card2,card3,card4,card5,card11,card22,card44,card55;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firsthome);
        checkUserStatus();



        //back Button
        /*

        backButton=(ImageView)findViewById(R.id.backfromhomeId);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firsthomeActivity.super.onBackPressed();

            }
        });



         */
        //Back Button


        //Catagory Button

        card1 =  findViewById(R.id.c1);
        card2 =  findViewById(R.id.c2);
        card3=  findViewById(R.id.c3);
        card4 =  findViewById(R.id.c4);
        card5 =  findViewById(R.id.c5);


        card1.setOnClickListener(this);

        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);

        card11 =  findViewById(R.id.c11);
        card22 =  findViewById(R.id.c22);
        //card3=  findViewById(R.id.c33);
        card44 =  findViewById(R.id.c44);
        card55 =  findViewById(R.id.c55);


        card11.setOnClickListener(this);

        card22.setOnClickListener(this);
        //card3.setOnClickListener(this);
        card44.setOnClickListener(this);
        card55.setOnClickListener(this);




        //Catagory Button

        //Bottom navigation

        //initialize
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationId);

        //when home is selected
        bottomNavigationView.setSelectedItemId(R.id.homeId);

        //listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {



                    case R.id.newsfeedId:
                        Intent iii=new Intent(getApplicationContext(),quesAnsActivity.class);
                        startActivity(iii);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profileId:

                        Intent ii=new Intent(getApplicationContext(),profileActivity.class);
                        startActivity(ii);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.homeId:
                        return true;

                    case R.id.notificationId:
                        Intent iiii=new Intent(getApplicationContext(),notificationActivity.class);
                        startActivity(iiii);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.chatId:

                        Intent i4=new Intent(getApplicationContext(),messagingActivity.class);
                        startActivity(i4);
                        overridePendingTransition(0,0);
                        return true;

                }


                return false;
            }
        });


        //bottom navigation




    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null)
        {
            String mUID=user.getUid();
            SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();

        }
        else
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();

        }

    }

    //button onClick
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.c1:

                startActivity(new Intent(getApplicationContext(),homeCatagory1Activity.class));
                break;

            case R.id.c2:

                startActivity(new Intent(getApplicationContext(), statisticsActivity.class));
                break;

            case R.id.c4:

                startActivity(new Intent(getApplicationContext(), worldStatusActivity.class));
                break;

            case R.id.c3:

                startActivity(new Intent(getApplicationContext(), premiumCatagory1Activity.class));




                //startActivity(new Intent(getApplicationContext(), premiumCatagory2Activity.class));
                break;

            case R.id.c5:
                Uri uri=Uri.parse("https://corona.gov.bd/corona-news/");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
                //startActivity(new Intent(getApplicationContext(), dailyNewsActivity.class));
                break;

            /*case R.id.homeCatagory6Id:
                startActivity(new Intent(getApplicationContext(), faqActivity.class));
                break;


             */


            case R.id.c11:
                Uri uri6=Uri.parse("https://surokkha.gov.bd/enroll");
                startActivity(new Intent(Intent.ACTION_VIEW,uri6));
                break;



            case R.id.c22:
                Uri uri2=Uri.parse("https://corona.gov.bd/oxygen-services-info");
                startActivity(new Intent(Intent.ACTION_VIEW,uri2));

                //startActivity(new Intent(getApplicationContext(), EmergencyActivity.class));
                break;

            /*case R.id.c33:

                    startActivity(new Intent(getApplicationContext(), quesAnsActivity.class));
                break;
             */
            case R.id.c44:

                Uri uri3=Uri.parse("https://livecoronatest.com/chat.php?city=Unnamed%20Road&lat=23.820264&lng=90.417367&addr_dist=Unknown&addr_div=Unknown");
                startActivity(new Intent(Intent.ACTION_VIEW,uri3));
                //startActivity(new Intent(getApplicationContext(), liveCoronaTestActivity.class));
                break;

            case R.id.c55:

                Uri uri5=Uri.parse("https://corona.gov.bd/telemedicine");
                startActivity(new Intent(Intent.ACTION_VIEW,uri5));
                //startActivity(new Intent(getApplicationContext(), telemedicineActivity.class));

                break;



        }

    }
    //button OnClick
}