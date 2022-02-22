package com.hackcovid.covidhack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class dhakaActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhaka);

        textView=findViewById(R.id.detailsDhakaId);

        fetchData();



    }

    private void fetchData() {
        String url="http://teamtigers.tech/covid19-dataset-bd/dhakacity/dhakacity.json";
        StringRequest jor=new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        textView.setText(response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        );

        int retry_time=80000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(retry_time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        jor.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue= Volley.newRequestQueue(dhakaActivity.this);
        requestQueue.add(jor);



    }
}