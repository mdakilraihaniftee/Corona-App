  package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class messagingActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);

        VpAdapter vpAdapter=new VpAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new fragment1(),"CHATS");
        vpAdapter.addFragment(new fragment2(),"USERS");
        viewPager.setAdapter(vpAdapter);



        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationId);

        //when home is selected
        bottomNavigationView.setSelectedItemId(R.id.chatId);

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

                    case R.id.chatId:
                        return true;

                    case R.id.notificationId:
                        Intent iiii=new Intent(getApplicationContext(),notificationActivity.class);
                        startActivity(iiii);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.homeId:

                        Intent i4=new Intent(getApplicationContext(),firsthomeActivity.class);
                        startActivity(i4);
                        overridePendingTransition(0,0);
                        return true;

                }


                return false;
            }
        });

    }
}