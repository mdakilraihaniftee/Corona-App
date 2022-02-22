package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class premiumActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backButton;
    private CardView card1,card2,card3,card4,card5;
    String email;
    public int flag;
    private String senderUid, currentDate;
    int current_balance, autorenewal;
    private String last_date;


    TextView date;

    private FirebaseAuth mAuth;

    private DatabaseReference databaseReference, databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        //gmail

        //email=intent.getStringExtra("email");


        //firebase connect
        mAuth = FirebaseAuth.getInstance();

        senderUid = mAuth.getUid();
        //databaseReference= FirebaseDatabase.getInstance().getReference("premiumUserDetails");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("userDetails").child(mAuth.getCurrentUser().getUid());


        /*
        //back Button

        backButton = (ImageView) findViewById(R.id.backfrompremiumId);
        date = findViewById(R.id.dateId);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                premiumActivity.super.onBackPressed();

            }
        });


         */

        //Back Button


        //Catagory Button

        card1 =  findViewById(R.id.c11);
        card2 =  findViewById(R.id.c22);
         //card3=  findViewById(R.id.c33);
        card4 =  findViewById(R.id.c44);
        card5 =  findViewById(R.id.c55);


        card1.setOnClickListener(this);

        card2.setOnClickListener(this);
        //card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);

        //new


        /*
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // last_upadate_date=snapshot.child("last_updated_date").getValue(String.class);
                userDetails user = snapshot.getValue(userDetails.class);
                last_date = user.getLast_updated_date();
                date.setText(last_date);


                current_balance = snapshot.child("balance").getValue(Integer.class);
                autorenewal = snapshot.child("autoRenewal").getValue(Integer.class);

                conditionCheck(last_date, current_balance, autorenewal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */

        /*databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                last_upadate_date=snapshot.child("last_updated_date").getValue(String.class);
                userDetails user=snapshot.getValue(userDetails.class);

                current_balance=snapshot.child("balance").getValue(Integer.class);
                autorenewal=snapshot.child("autoRenewal").getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


         */


        /*databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               last_upadated_date=snapshot.child("last_updated_date").getValue().toString();

               current_balance=snapshot.child("balance").getValue(Integer.class);
               autorenewal=snapshot.child("autoRenewal").getValue(Integer.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */





        //new condition




        /*

        flag=0;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot1:snapshot.getChildren())
                {
                    if(dataSnapshot1.child("email").getValue().equals(email))
                    {
                        flag=1;
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */


        //Catagory Button

        //Bottom navigation

        //initialize
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationId);

        //when premium is selected
        //bottomNavigationView.setSelectedItemId(R.id.premiumId);

        //listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    //case R.id.premiumId:

                      //  return true;

                    case R.id.profileId:

                        Intent i = new Intent(getApplicationContext(), profileActivity.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.homeId:
                        Intent ii = new Intent(getApplicationContext(), firsthomeActivity.class);
                        startActivity(ii);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.newsfeedId:
                        Intent iii = new Intent(getApplicationContext(), quesAnsActivity.class);
                        startActivity(iii);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.notificationId:
                        Intent iiii=new Intent(getApplicationContext(),notificationActivity.class);
                        startActivity(iiii);
                        overridePendingTransition(0,0);
                        return true;

                }


                return false;
            }
        });


        //bottom navigation

    }

    private void conditionCheck(String last_date, int current_balance, int autorenewal) {
        flag = 0;
        if (currentDate.equals(last_date)) {
            flag = 1;
        } else {
            if (autorenewal == 1) {
                Toast.makeText(getApplicationContext(), "check", Toast.LENGTH_LONG).show();
                if (current_balance >= 2) {
                    flag = 1;
                    current_balance = current_balance - 2;
                    databaseReference2.child("balance").setValue(current_balance);
                    databaseReference2.child("last_updated_date").setValue(currentDate);
                } else {
                    Toast.makeText(getApplicationContext(), "Insufficient Balance", Toast.LENGTH_LONG).show();
                }
            }


        }

    }

    //button onClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.c11:
                    Uri uri=Uri.parse("https://surokkha.gov.bd/enroll");
                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
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

   /* private void conditionCheck2() {
        if (current_balance >= 2) {
            premiumAsking();


        } else {
            balanceLoadAsking();
        }

    }


    //button OnClick

    //show alert

    private void premiumAsking() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission");
        builder.setMessage("Do you want activate your premium features?")
                .setIcon(R.drawable.question)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flag = 1;
                        current_balance = current_balance - 2;
                        databaseReference2.child("balance").setValue(current_balance);
                        databaseReference2.child("last_updated_date").setValue(currentDate);
                        autoRenewalAsking();


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void balanceLoadAsking() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insufficient Balance");
        builder.setMessage("Do you want to Recharge?(Premium features will cost you 2 tk per day)")
                .setIcon(R.drawable.question)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), balanceLoadActivity.class));


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void autoRenewalAsking() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Auto Renewal");
        builder.setMessage("How do you want to use premium feature?")
                .setIcon(R.drawable.question)
                .setCancelable(false)
                .setPositiveButton("Auto Renewal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReference2.child("autoRenewal").setValue(1);
                        Toast.makeText(premiumActivity.this, "Auto Renewal Started Successfully", Toast.LENGTH_SHORT).show();

                    }
                }).setNegativeButton("only for today", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    */




}


