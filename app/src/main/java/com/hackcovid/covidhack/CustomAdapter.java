package com.hackcovid.covidhack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<differentCountries> {

    private Context context;
    private List<differentCountries> differentCountriesList;
    private List<differentCountries> differentCountriesListSearched;

    public CustomAdapter( Context context, List<differentCountries> differentCountriesList) {
        super(context, R.layout.different_countries_custom_item, differentCountriesList );

        this.context=context;
        this.differentCountriesList=differentCountriesList;
        this.differentCountriesListSearched=differentCountriesList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.different_countries_custom_item, null , true);
        TextView  CountryNameTxtView=view.findViewById(R.id.CountryNameId);
        ImageView imageView=view.findViewById(R.id.flagImageId);

        CountryNameTxtView.setText(differentCountriesListSearched.get(position).getCountryName());
        Glide.with(context).load(differentCountriesListSearched.get(position).getFlagAdress()).into(imageView);



        return view;
    }

    @Override
    public int getCount() {
        return differentCountriesListSearched.size();

    }

    @Nullable
    @Override
    public differentCountries getItem(int position) {

        return differentCountriesListSearched.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if(constraint==null || constraint.length()==0)
                {
                    filterResults.count= differentCountriesList.size();
                    filterResults.values=differentCountriesList;

                }
                else
                {
                    List<differentCountries> finalList=new ArrayList<>();
                    String typedString=constraint.toString().toLowerCase();

                    for(differentCountries x:differentCountriesList)
                    {
                        if(x.getCountryName().toLowerCase().contains(typedString))
                        finalList.add(x);
                    }
                    filterResults.count=finalList.size();
                    filterResults.values=finalList;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                differentCountriesListSearched=(List<differentCountries>) results.values;
                premiumCatagory1Activity.differentCountriesList=(List<differentCountries>)results.values;
                notifyDataSetChanged();
            }
        };
        return filter;





        
    }
}
