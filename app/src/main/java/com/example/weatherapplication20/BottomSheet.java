package com.example.weatherapplication20;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BottomSheet extends BottomSheetDialogFragment {

    private TextView day;
    private TextView description;
    private TextView temp_min;
    private TextView temp_max;
    private TextView sea_level;
    private TextView cloudiness;
    private ImageView icon;
    private TextView rains;


    public BottomSheet() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView=inflater.inflate(R.layout.bottom_sheet,container,false);
        description = (TextView)rootView.findViewById(R.id.description);
        temp_min=(TextView)rootView.findViewById(R.id.temp_min);
        temp_max=(TextView)rootView.findViewById(R.id.temp_max);
        sea_level=(TextView)rootView.findViewById(R.id.sea_level);
        cloudiness=(TextView)rootView.findViewById(R.id.cloudiness);
        icon=(ImageView)rootView.findViewById(R.id.icon);
        day=(TextView)rootView.findViewById(R.id.day);
        rains=(TextView)rootView.findViewById(R.id.rains);


        Bundle bundle = getArguments();

        temp_min.setText(" Temperature Min. = "+String.valueOf(Math.round(bundle.getDouble("temp_min")-273))+"\u2103");
        temp_max.setText(" Temperature Max. = "+String.valueOf(Math.round(bundle.getDouble("temp_max")-273))+"\u2103");
        sea_level.setText(" Pressure On Sea Level = "+String.valueOf(bundle.getDouble("sea_level"))+" hPa");
        cloudiness.setText(" Cloudiness = "+String.valueOf(bundle.getInt("cloudiness"))+"%");
        description.setText(bundle.getString("description"));

        if(bundle.getString("rains").isEmpty())
            rains.setText(" Rain volume for last 3 hours = 0.0 mm");
        else
            rains.setText(" Rain volume for last 3 hours = "+String.valueOf(Math.round(Double.parseDouble(bundle.getString("rains"))*100.0)/100.0)+" mm");

        Glide.with(rootView)
                .load("http://openweathermap.org/img/w/"+bundle.getString("icon")+".png")
                .into(icon);

        Long seconds = bundle.getLong("unix_time");
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        Date date = new Date(seconds * 1000);
        day.setText(formatter.format(date));

        return rootView ;
    }
}
