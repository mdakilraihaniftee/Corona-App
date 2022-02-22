package com.hackcovid.covidhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class first_screen extends AppCompatActivity {

    private ProgressBar progressBar;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //remove the notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN

                );
        setContentView(R.layout.activity_first_screen);

        //progressBar
        progressBar=(ProgressBar)findViewById(R.id.progressBarId);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                loginpage();
            }
        });
        thread.start();





    }

    public void doWork()
    {

        for(time=25;time<=100;time=time+25)
        {
            try{
                Thread.sleep(1000);
                progressBar.setProgress(time);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void loginpage()
    {
        Intent intent=new Intent(first_screen.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}