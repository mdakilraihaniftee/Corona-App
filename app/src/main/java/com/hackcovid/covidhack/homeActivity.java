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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class homeActivity extends AppCompatActivity {

    private TextView death_no,affected_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //death_no=(TextView)findViewById(R.id.textView6);
        //affected_no=(TextView)findViewById(R.id.textView7);

        //fetchData();
    }

    private void fetchData() {
        String url="https://coronavirus-19-api.herokuapp.com/countries/bangladesh";
        JsonObjectRequest jor=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(homeActivity.this,response.toString(),Toast.LENGTH_SHORT).show();

                        try{


                            death_no.setText(response.getString("todayDeaths"));
                            affected_no.setText(response.getString("todayCases"));

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(homeActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();




                    }
                }
        );

        int retry_time=80000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(retry_time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        jor.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue= Volley.newRequestQueue(homeActivity.this);
        requestQueue.add(jor);






    }


}