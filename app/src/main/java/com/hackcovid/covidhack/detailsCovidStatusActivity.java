package com.hackcovid.covidhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class detailsCovidStatusActivity extends AppCompatActivity {
    private int countryPosition;
    private TextView TotalConfirmedCasesText,TodayConfirmedCasesText,TotalDeathText,TodayDeathText,ConfirmedRecoveriesText, TodayRecoveriesText,ActiveConfirmedCasesText,CriticalText,TotalTestsText,TestPerMillionText,PopulationText,ContinentText,flagAdressText,CountryNameText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_covid_status);

        Intent intent=getIntent();
        countryPosition=intent.getIntExtra("position",0);

        TotalConfirmedCasesText= (TextView)findViewById(R.id.TotalconfirmedcasesIdPremium);
        TodayConfirmedCasesText=(TextView)findViewById(R.id.TodayConfirmedIdPremium);
        TotalDeathText=(TextView)findViewById(R.id.TotaldeathsIdPremium);
        TodayDeathText=(TextView)findViewById(R.id.TodaydeathsIdPremium);
        ConfirmedRecoveriesText=(TextView)findViewById(R.id.ConfirmedrecoveriesIdPremium);
        TodayRecoveriesText=(TextView)findViewById(R.id.TodayConfirmedrecoveriesIdPremium);
        ActiveConfirmedCasesText=(TextView)findViewById(R.id.ActiveconfirmedcasesIdPremium);
        CriticalText=(TextView)findViewById(R.id.CriticalIdPremium);
        TotalTestsText=(TextView)findViewById(R.id.TotaltestsIdPremium);
        TestPerMillionText=(TextView)findViewById(R.id.TestPerMillionIdPremium);
        PopulationText=(TextView)findViewById(R.id.PopulationIdPremium);
        ContinentText= (TextView)findViewById(R.id.ContinentIdPremium);
        CountryNameText=(TextView)findViewById(R.id.CountryIdPremium);


        TotalConfirmedCasesText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getTotalConfirmedCases());
        TodayConfirmedCasesText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getTodayConfirmedCases());
        TotalDeathText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getTotalDeath());
        TodayDeathText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getTodayDeath());
        ConfirmedRecoveriesText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getConfirmedRecoveries());
        TodayRecoveriesText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getTodayRecoveries());
        ActiveConfirmedCasesText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getActiveConfirmedCases());
        CriticalText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getCritical());
        TotalTestsText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getTotalTests());
        TestPerMillionText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getTestPerMillion());
        PopulationText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getPopulation());
        ContinentText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getContinent());
        CountryNameText.setText(premiumCatagory1Activity.differentCountriesList.get(countryPosition).getCountryName());



    }
}