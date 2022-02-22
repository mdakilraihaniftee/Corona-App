package com.hackcovid.covidhack;
//signin

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText signInEmailEditText,singInPasswordEditText;
    private TextView signUpTextView,forgetPass;
    private Button signInButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private ImageView gosignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //title set
        //this.setTitle("Sign In");

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null)
        {

            Intent i=new Intent(getApplicationContext(),firsthomeActivity.class);

            startActivity(i);
            //Toast.makeText(MainActivity.this,"USer found",Toast.LENGTH_LONG).show();
        }

        signInEmailEditText=(EditText)findViewById(R.id.editTextTextPersonName2);
        singInPasswordEditText=(EditText)findViewById(R.id.editTextTextPersonName3);
        signInButton=(Button)findViewById(R.id.signInButtonId);
        signUpTextView=(TextView)findViewById(R.id.signUpTextId);
        progressBar=(ProgressBar)findViewById(R.id.progressBarId);
        forgetPass=(TextView)findViewById(R.id.forgetPassId);
        gosignUp=(ImageView)findViewById(R.id.goSignUp);
        signUpTextView.setOnClickListener(this);
        forgetPass.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        gosignUp.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signInButtonId:
                UserLogin();
                break;

            case R.id.signUpTextId:
                Intent i=new Intent(MainActivity.this,signUp.class);
                startActivity(i);
                break;

            case R.id.goSignUp:
                Intent ii=new Intent(MainActivity.this,signUp.class);
                startActivity(ii);
                break;

            case R.id.forgetPassId:

                check();
                break;

        }

    }

    public void check() {

        //EditText reset = new EditText(MainActivity.this);
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(MainActivity.this);
        View dialogView= LayoutInflater.from(MainActivity.this).inflate(R.layout.dilgbx,null);
        EditText reset=dialogView.findViewById(R.id.resetEmailId);

        passwordResetDialog.setTitle("Reset Password ?");
        passwordResetDialog.setMessage("Enter Your Email To Received Reset Link");
        passwordResetDialog.setView(dialogView);

        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String mail = reset.getText().toString().trim();
                mAuth.sendPasswordResetEmail(mail)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Sent Reset Password Link To Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Invalid Email ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        passwordResetDialog.show();
    }


    private void UserLogin() {
        String email=signInEmailEditText.getText().toString().trim();
        String password=singInPasswordEditText.getText().toString().trim();
        if(email.isEmpty())
        {
            signInEmailEditText.setError("Enter an email address");
            signInEmailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signInEmailEditText.setError("Enter a valid email address");
            signInEmailEditText.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            singInPasswordEditText.setError("Enter a password");
            singInPasswordEditText.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            singInPasswordEditText.setError("Minimum length of a password should be 6");
            singInPasswordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        //mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    Intent i=new Intent(getApplicationContext(),firsthomeActivity.class);


                    startActivity(i);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Login Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }




}