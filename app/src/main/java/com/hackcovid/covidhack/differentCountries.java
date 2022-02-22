package com.hackcovid.covidhack;

public class differentCountries {
    private  String TotalConfirmedCases,TodayConfirmedCases,TotalDeath,TodayDeath,ConfirmedRecoveries, TodayRecoveries,ActiveConfirmedCases,Critical,TotalTests,TestPerMillion,Population,Continent,flagAdress,CountryName;



    public differentCountries(String totalConfirmedCases, String todayConfirmedCases, String totalDeath, String todayDeath, String confirmedRecoveries, String todayRecoveries, String activeConfirmedCases, String critical, String totalTests, String testPerMillion, String population, String continent, String flagAdress, String countryName) {
        TotalConfirmedCases = totalConfirmedCases;
        TodayConfirmedCases = todayConfirmedCases;
        TotalDeath = totalDeath;
        TodayDeath = todayDeath;
        ConfirmedRecoveries = confirmedRecoveries;
        TodayRecoveries = todayRecoveries;
        ActiveConfirmedCases = activeConfirmedCases;
        Critical = critical;
        TotalTests = totalTests;
        TestPerMillion = testPerMillion;
        Population = population;
        Continent = continent;
        this.flagAdress = flagAdress;
        CountryName = countryName;
    }

    public String getTotalConfirmedCases() {
        return TotalConfirmedCases;
    }

    public void setTotalConfirmedCases(String totalConfirmedCases) {
        TotalConfirmedCases = totalConfirmedCases;
    }

    public String getTodayConfirmedCases() {
        return TodayConfirmedCases;
    }

    public void setTodayConfirmedCases(String todayConfirmedCases) {
        TodayConfirmedCases = todayConfirmedCases;
    }

    public String getTotalDeath() {
        return TotalDeath;
    }

    public void setTotalDeath(String totalDeath) {
        TotalDeath = totalDeath;
    }

    public String getTodayDeath() {
        return TodayDeath;
    }

    public void setTodayDeath(String todayDeath) {
        TodayDeath = todayDeath;
    }

    public String getConfirmedRecoveries() {
        return ConfirmedRecoveries;
    }

    public void setConfirmedRecoveries(String confirmedRecoveries) {
        ConfirmedRecoveries = confirmedRecoveries;
    }

    public String getTodayRecoveries() {
        return TodayRecoveries;
    }

    public void setTodayRecoveries(String todayRecoveries) {
        TodayRecoveries = todayRecoveries;
    }

    public String getActiveConfirmedCases() {
        return ActiveConfirmedCases;
    }

    public void setActiveConfirmedCases(String activeConfirmedCases) {
        ActiveConfirmedCases = activeConfirmedCases;
    }

    public String getCritical() {
        return Critical;
    }

    public void setCritical(String critical) {
        Critical = critical;
    }

    public String getTotalTests() {
        return TotalTests;
    }

    public void setTotalTests(String totalTests) {
        TotalTests = totalTests;
    }

    public String getTestPerMillion() {
        return TestPerMillion;
    }

    public void setTestPerMillion(String testPerMillion) {
        TestPerMillion = testPerMillion;
    }

    public String getPopulation() {
        return Population;
    }

    public void setPopulation(String population) {
        Population = population;
    }

    public String getContinent() {
        return Continent;
    }

    public void setContinent(String continent) {
        Continent = continent;
    }

    public String getFlagAdress() {
        return flagAdress;
    }

    public void setFlagAdress(String flagAdress) {
        this.flagAdress = flagAdress;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }
}
