package com.hackcovid.covidhack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class askActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinner;
    private EditText questionBox;
    private ImageView imageView;
    private TextView cancelBtn,postQuesBtn;

    private String askedByName="";
    private DatabaseReference askedByRef;
    private ProgressDialog loader;
    private  String myUrl="";
    StorageTask uploadTask;
    StorageReference storageReference;
    private Uri imageUri;
    final String[] receiverId = new String[1];
    DatabaseReference publisherRef,receiverRef;
    private String postid;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserId,timestamp;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        toolbar=(Toolbar) findViewById(R.id.quesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ask a Question");




        spinner=findViewById(R.id.spinner);
        questionBox=findViewById(R.id.quesText);
        imageView=findViewById(R.id.quesImage);
        cancelBtn=findViewById(R.id.cancelId);
        postQuesBtn=findViewById(R.id.postQuesId);

        loader=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        onlineUserId=mUser.getUid();

        askedByRef= FirebaseDatabase.getInstance().getReference("userDetails").child(onlineUserId);

        askedByRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                askedByName=snapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storageReference= FirebaseStorage.getInstance().getReference("questions");

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.topics));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(spinner.getSelectedItem().equals("select topic")){

                    Toast.makeText(askActivity.this,"Please Select a Valid topic",Toast.LENGTH_SHORT).show();

                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        postQuesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionBox.getText().toString().trim().isEmpty())
                {
                    questionBox.setError("Question Required");
                    return;

                }


                if(spinner.getSelectedItem().equals("select topic")){


                    spinner.requestFocus();

                    Toast.makeText(askActivity.this,"Please Select a Valid topic",Toast.LENGTH_SHORT).show();

                    return;
                }
                performValidations();
            }
        });


    }



    String mDAte=""+System.currentTimeMillis();
    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("questions post");



    private void performValidations() {

        String ques=questionBox.getText().toString().trim();
        String topic=spinner.getSelectedItem().toString();



        if(!ques.isEmpty() && !topic.equals("") && imageUri==null)
        {
            uploadQuesWithoutImg();
        }

        if(!ques.isEmpty() && !topic.equals("") && imageUri!=null)
        {

            uploadQuesWithImg();
        }



    }

    private void uploadQuesWithoutImg() {

        startLoader();
        postid = ref.push().getKey();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("postid",postid);
        hashMap.put("question",questionBox.getText().toString().trim());
        hashMap.put("publisher",onlineUserId);
        hashMap.put("topic",spinner.getSelectedItem().toString());
        hashMap.put("askedby",askedByName);
        hashMap.put("date",mDAte);

        String question=questionBox.getText().toString().trim();
        String topic=spinner.getSelectedItem().toString();

      /* prepareNotification(postid,
                ""+askedByName+" added new post",
                ""+topic+"\n   "+question,
                "PostNotification",
                "POST"
        );


       */


        ref.child(postid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(askActivity.this,"Question Posted Successfully",Toast.LENGTH_SHORT).show();
                    loader.dismiss();











                    //Data data=new Data(onlineUser.getUid(),name+":\n"+message,"New Message",receiverid,"ChatNotification",R.drawable.app_icon);




                    startActivity(new Intent(askActivity.this,quesAnsActivity.class));

                    finish();
                }
                else
                {
                    Toast.makeText(askActivity.this,"could not Upload Image"+task.getException().toString(),Toast.LENGTH_SHORT).show();
                    loader.dismiss();
                }

            }
        });


    }

    /*

    //addnotification of post

    private void addNotification()
    {

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("userDetails");


        String date = DateFormat.getDateInstance().format(new Date());
        String notificationid = reference2.push().getKey();

        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("notificationid", notificationid);

        hashMap2.put("comment", "added a new post.");
        hashMap2.put("date", date);

        hashMap2.put("publisherid", FirebaseAuth.getInstance().getCurrentUser().getUid());

        hashMap2.put("postid", postid);

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    userDetails user=dataSnapshot.getValue(userDetails.class);
                    if(user.getUserId().equals(onlineUserId)){
                        continue;
                    }
                    reference2.child("")
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference2.child(receiverId[0]).child("Notification").child(mDAte).setValue(hashMap2)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(askActivity.this, "Notification Added Successfully", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(askActivity.this, "Error Adding notification", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    */

    private void uploadQuesWithImg() {

        startLoader();
        final StorageReference fileReference;
        fileReference=storageReference.child(System.currentTimeMillis()+"."+ getExtension(imageUri));
        uploadTask =fileReference.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isComplete())
                {
                    throw task.getException();
                }
                return  fileReference.getDownloadUrl();

            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isSuccessful())
                {
                    Uri downloadUrl=(Uri)task.getResult();
                    myUrl=downloadUrl.toString();
                    String postid = ref.push().getKey();
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("postid",postid);
                    hashMap.put("question",questionBox.getText().toString().trim());
                    hashMap.put("publisher",onlineUserId);
                    hashMap.put("topic",spinner.getSelectedItem().toString());
                    hashMap.put("askedby",askedByName);
                    hashMap.put("date",mDAte);
                    hashMap.put("questionImage",myUrl);



                    ref.child(postid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(askActivity.this,"Question Posted Successfully",Toast.LENGTH_SHORT).show();

                                String question=questionBox.getText().toString().trim();
                                String topic=spinner.getSelectedItem().toString();




                                /*
                                prepareNotification(postid,
                                        ""+askedByName+" added new post",
                                        ""+topic+"\n"+question,
                                        "PostNotification",
                                        "POST"
                                );


                                 */
                                loader.dismiss();
                                startActivity(new Intent(askActivity.this,quesAnsActivity.class));

                                finish();
                            }
                            else
                            {
                                Toast.makeText(askActivity.this,"could not Upload Image"+task.getException().toString(),Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }




                        }
                    });



                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(askActivity.this,"Failed to upload the Question",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private  void startLoader()
    {
        loader.setMessage("Posting Your Question");
        loader.setCanceledOnTouchOutside(false);
        loader.show();

    }


    public String getExtension(Uri imageUri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!= null)
        {
            imageUri=data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }

    }

    /*
    prepareNotification(postid,
                            ""+askedByName+" added new post",
                            ""+topic+"\n  "+question,
                            "PostNotification",
                            "POST"
                            );
     */


    private void prepareNotification(String pId,String title,String description,String notificationType, String notificationTopic)
    {
        //prepare Data for notification
        String NOTIFICATION_TOPIC ="/topics/" +notificationTopic;//topic must match with what the receiver subscribed to
        String NOTIFICATION_TITLE=title; //e.g Alif prottoy added a new post
        String NOTIFICATION_MESSAGE=description; //contest of post
        String NOTIFICATION_TYPE= notificationType; //like,comment,post etc;


        //prepare json what to send
        JSONObject notificationJo=new JSONObject();
        JSONObject notificationBodyJo=new JSONObject();


        try {
            notificationBodyJo.put("notificationType",NOTIFICATION_TYPE);
            notificationBodyJo.put("sender",onlineUserId);
            notificationBodyJo.put("pId",pId);
            notificationBodyJo.put("pTitle",NOTIFICATION_TITLE);
            notificationBodyJo.put("pDescription",NOTIFICATION_MESSAGE);


            //video 21 -23 min


            //where to send
            notificationJo.put("to",NOTIFICATION_TOPIC);
            notificationJo.put("Data",notificationBodyJo);//combine Data to be sent
        } catch (JSONException e) {
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        sendPostNotification(notificationJo);


    }

    private void sendPostNotification(JSONObject notificationJo) {
        //send volley object request
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("FCM_RESPONSE","onRespone: "+response.toString());


                    }
                },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //error occured
                Toast.makeText(askActivity.this,""+error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                //put required headers
                Map<String ,String> headers=new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","key=AAAAfwu0JBc:APA91bEJ16K-Q9JJtnpZ7HyXuGFc3ESP1s1mVBgbD9Q1x1ON3DZfwVZmQPH4N_t7VHeAZVi8atcMPfPj9Dis2cRTOFm2LkWXNQBNfLpvvsw-bZWFoMmw9xD31bb1xZcJs64D67F_j-z7");

                return headers;

            }
        };

        //Toast.makeText(this,"here now",Toast.LENGTH_SHORT).show();

        //enqueue the volley request
        Volley.newRequestQueue(this).add(jsonObjectRequest);


    }


}