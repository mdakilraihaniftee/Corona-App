package com.hackcovid.covidhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class premiumCatagory1Activity extends AppCompatActivity {

     EditText SearchEditText;
     ListView CountriesListView;
     SimpleArcLoader simpleArcLoader;

    public  static List<differentCountries> differentCountriesList=new ArrayList<>();
    differentCountries countryModel;
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_catagory1);

        SearchEditText=(EditText)findViewById(R.id.searchEditTextId);
        CountriesListView=(ListView)findViewById(R.id.listviewCountriesId);
        simpleArcLoader=(SimpleArcLoader)findViewById(R.id.arcloaderId);


        getdata();

        CountriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),detailsCovidStatusActivity.class).putExtra("position",position));

            }
        });

        SearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customAdapter.getFilter().filter(s);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void getdata() {

        String url="https://corona.lmao.ninja/v2/countries";
        StringRequest jor=new StringRequest(Request.Method.GET,url,
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                        try{

                            JSONArray jsonArray=new JSONArray(response);

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String  countryName=jsonObject.getString("country");

                                String totalConfirmedCases= jsonObject.getString("cases");
                                String todayConfirmedCases =jsonObject.getString("todayCases");
                                String totalDeath=jsonObject.getString("deaths");
                                String todayDeath=jsonObject.getString("todayDeaths");

                                String confirmedRecoveries=jsonObject.getString("recovered");

                                String todayRecoveries=jsonObject.getString("todayRecovered");


                                String activeConfirmedCases=jsonObject.getString("active");

                                String critical=jsonObject.getString("critical");

                                String totalTests=jsonObject.getString("tests");
                                String testPerMillion=jsonObject.getString("testsPerOneMillion");

                                String population=jsonObject.getString("population");
                                String continent=jsonObject.getString("continent");

                                JSONObject countrydetails=jsonObject.getJSONObject("countryInfo");
                                String flagAdress=countrydetails.getString("flag");

                                countryModel=new  differentCountries(totalConfirmedCases, todayConfirmedCases, totalDeath, todayDeath, confirmedRecoveries, todayRecoveries,  activeConfirmedCases,  critical, totalTests, testPerMillion,  population, continent, flagAdress, countryName);


                                differentCountriesList.add(countryModel);


                            }


                            //Toast.makeText(getApplicationContext(),differentCountriesList.toString(), Toast.LENGTH_LONG).show();
                            customAdapter =new CustomAdapter(premiumCatagory1Activity.this, differentCountriesList);
                            //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
                            CountriesListView.setAdapter(customAdapter);
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);



                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        simpleArcLoader.stop();
                        simpleArcLoader.setVisibility(View.GONE);
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

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jor);

    }



}