package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;

public class signUp extends AppCompatActivity implements View.OnClickListener{

    private EditText emailEt,passwordEt,confirmPassEt,nameEt,phoneEt,genderEt;
    private TextView signInTextView1,signInTextView2;
    private Button signUpButton,buttonchoosepicture;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private ImageView imageView,gosignIn;
    String gender;

    private Uri imageUri;

    private static final int Image_Request=1;

    private FirebaseAuth mAuth;
    DatabaseReference database_user;
    StorageReference storageReference;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set title
        this.setTitle("Sign Up");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        database_user= FirebaseDatabase.getInstance().getReference("userDetails");
        storageReference= FirebaseStorage.getInstance().getReference("userDetails");

        imageView=(ImageView)findViewById(R.id.profilePictureId);
        gosignIn=(ImageView)findViewById(R.id.goSignIn);
        emailEt=(EditText)findViewById(R.id.userEmailId);
        passwordEt=(EditText)findViewById(R.id.password1Id);
        confirmPassEt=(EditText)findViewById(R.id.password2Id);
        nameEt=(EditText)findViewById(R.id.userNameId);
        phoneEt=(EditText)findViewById(R.id.userPhnId);
        radioGroup=(RadioGroup)findViewById(R.id.genderId);
        buttonchoosepicture=(Button)findViewById(R.id.chooseImageId);
        signUpButton=(Button)findViewById(R.id.userRegisterId);
        signInTextView1=(TextView)findViewById(R.id.signInTextId1);
        signInTextView2=(TextView)findViewById(R.id.signInTextId2);
        progressBar=(ProgressBar)findViewById(R.id.signUpProgressBarId);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.maleId)
                {
                    gender="Male";
                }
                else
                {
                    gender="Female";
                }
            }
        });

        buttonchoosepicture.setOnClickListener(this);


        signUpButton.setOnClickListener(this);
        signInTextView1.setOnClickListener(this);
        signInTextView2.setOnClickListener(this);
        gosignIn.setOnClickListener(this);



    }







    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.chooseImageId:
                chooseFile();
                break;

            case R.id.userRegisterId:
                userRegister();
                break;

            case R.id.signInTextId1:
                Intent intent=new Intent(signUp.this,MainActivity.class);
                startActivity(intent);
                break;

            case R.id.signInTextId2:
                Intent intent2=new Intent(signUp.this,MainActivity.class);
                startActivity(intent2);
                break;

            case R.id.goSignIn:
                Intent intent3=new Intent(signUp.this,MainActivity.class);
                startActivity(intent3);
                break;
        }

    }

    //image extention
    public String getExtension(Uri imageUri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void userRegister() {

        if (imageView.getDrawable()==null){

            Toast.makeText(getApplicationContext(),"Select a profile iamge",Toast.LENGTH_LONG).show();
            imageView.requestFocus();
            return;
        }

        String name=nameEt.getText().toString().trim();
        String phoneNo=phoneEt.getText().toString().trim();

        String email=emailEt.getText().toString().trim();
        String password=passwordEt.getText().toString().trim();
        String confirmPassword=confirmPassEt.getText().toString().trim();

        StorageReference ref=storageReference.child(System.currentTimeMillis()+"."+getExtension(imageUri));


        if(name.isEmpty())
        {
            nameEt.setText("Required");
            nameEt.requestFocus();
            return;
        }

        if(phoneNo.isEmpty())
        {
            phoneEt.setText("Required");
            phoneEt.requestFocus();
            return;
        }


        if(gender.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Select a gender",Toast.LENGTH_LONG).show();
            return;
        }




        if(email.isEmpty())
        {
            emailEt.setError("Enter an email address");
            emailEt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEt.setError("Enter a valid email address");
            emailEt.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            passwordEt.setError("Enter a password");
            passwordEt.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty())
        {
            confirmPassEt.setError("Required");
            confirmPassEt.requestFocus();
            return;
        }

        if(!password.equals(confirmPassword))
        {
            confirmPassEt.setError("Password didn't match");
            confirmPassEt.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            passwordEt.setError("Minimum length of a password should be 6");
            passwordEt.requestFocus();
            return;
        }

        String numRegex   = ".*[0-9].*";
        String salphaRegex = ".*[A-Z].*";
        String calphaRegex = ".*[a-z].*";
        if(!(password.matches(numRegex) && (password.matches(calphaRegex) || password.matches(salphaRegex)))){
            //|| !password.contains("[a-zA-Z]+")
            passwordEt.setError("Create a password including both alphabet and number");
            passwordEt.requestFocus();
            return;
        }

        //progressBar.setVisibility(View.VISIBLE);

        //gender="male";

        final ProgressDialog progressDialog = new ProgressDialog(signUp.this);
        progressDialog.setTitle("Creating Your Account");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                FirebaseMessaging.getInstance().subscribeToTopic("allUser")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg ="successful";
                                if (!task.isSuccessful()) {
                                    msg = "failed";
                                }

                                Toast.makeText(signUp.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });


                ref.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful());
                                Uri downloadUrl=uriTask.getResult();


                                //upload up=new upload(name,email,downloadUrl.toString());
                                Calendar calendar=Calendar.getInstance();
                                String currentDate= DateFormat.getDateInstance().format(calendar.getTime());


                                String uploadId =mAuth.getCurrentUser().getUid();

                                userDetails user =new userDetails(name, phoneNo, gender, email, downloadUrl.toString(),uploadId,name.toLowerCase(),"online",false);

                                //String uploadId =database_user.push().getKey();

                                database_user.child(uploadId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Account Created",Toast.LENGTH_SHORT).show();

                                        //progressBar.setVisibility(View.GONE);

                                        Intent i=new Intent(signUp.this,profileActivity.class);

                                        startActivity(i);

                                    }
                                });


                            }

                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Failed to Create Account",Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Processing " + (int) progress + "%");
                        progressDialog.setCanceledOnTouchOutside(false);
                    }
                });






                //});
                        /*.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"couldn't save",Toast.LENGTH_SHORT).show();
                    }
                });*/




            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                String str= "Error! "+e.getMessage();
                Toast.makeText(getApplicationContext(),str , Toast.LENGTH_SHORT).show();

            }
        });



    }


    private void chooseFile() {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,Image_Request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Image_Request && resultCode==RESULT_OK && data!=null && data.getData()!= null)
        {
            imageUri=data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }

    }





}


