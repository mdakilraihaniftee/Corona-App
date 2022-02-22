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

import org.json.JSONException;
import org.json.JSONObject;

public class worldStatusActivity extends AppCompatActivity {
    private TextView TotalConfirmedCasesText,TodayConfirmedCasesText,TotalDeathText,TodayDeathText,ConfirmedRecoveriesText, TodayRecoveriesText,ActiveConfirmedCasesText,CriticalText,TotalTestsText,TestPerMillionText,PopulationText,ContinentText,flagAdressText,CountryNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_status);

        TotalConfirmedCasesText= (TextView)findViewById(R.id.TotalconfirmedcasesId);
        TodayConfirmedCasesText=(TextView)findViewById(R.id.TodayConfirmedId);
        TotalDeathText=(TextView)findViewById(R.id.TotaldeathsId);
        TodayDeathText=(TextView)findViewById(R.id.TodaydeathsId);
        ConfirmedRecoveriesText=(TextView)findViewById(R.id.ConfirmedrecoveriesId);
        TodayRecoveriesText=(TextView)findViewById(R.id.TodayConfirmedrecoveriesId);
        ActiveConfirmedCasesText=(TextView)findViewById(R.id.ActiveconfirmedcasesId);
        CriticalText=(TextView)findViewById(R.id.CriticalId);
        TotalTestsText=(TextView)findViewById(R.id.TotaltestsId);
        TestPerMillionText=(TextView)findViewById(R.id.TestPerMillionId);
        PopulationText=(TextView)findViewById(R.id.PopulationId);
        ContinentText= (TextView)findViewById(R.id.ContinentId);

        fetchData();
        
    }

    private void fetchData() {

        String url="https://corona.lmao.ninja/v2/all";
        StringRequest jor=new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try{

                            //JSONArray jsonArray=new JSONArray(response);


                            //JSONObject jsonObject=jsonArray.getJSONObject(15);

                            JSONObject jsonObject=new JSONObject(response);

                            TotalConfirmedCasesText.setText(jsonObject.getString("cases"));
                            TodayConfirmedCasesText.setText(jsonObject.getString("todayCases"));
                            TotalDeathText.setText(jsonObject.getString("deaths"));
                            TodayDeathText.setText(jsonObject.getString("todayDeaths"));
                            ConfirmedRecoveriesText.setText(jsonObject.getString("recovered"));
                            TodayRecoveriesText.setText(jsonObject.getString("todayRecovered"));
                            ActiveConfirmedCasesText.setText(jsonObject.getString("active"));
                            CriticalText.setText(jsonObject.getString("critical"));
                            TotalTestsText.setText(jsonObject.getString("tests"));
                            TestPerMillionText.setText(jsonObject.getString("testsPerOneMillion"));
                            PopulationText.setText(jsonObject.getString("population"));
                            ContinentText.setText(jsonObject.getString("affectedCountries"));

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

        RequestQueue requestQueue= Volley.newRequestQueue(worldStatusActivity.this);
        requestQueue.add(jor);


    }
}