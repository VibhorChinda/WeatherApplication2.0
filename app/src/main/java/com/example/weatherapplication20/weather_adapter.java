package com.example.weatherapplication20;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class weather_adapter extends RecyclerView.Adapter <weather_adapter.weatherViewHolder>
{

   private Context context;
   private int row_layout;
   private ArrayList<list> weather_list;




    public weather_adapter(ArrayList<list> weather_list,int row_layout,Context context) {
        this.context=context;
        this.row_layout=row_layout;
        this.weather_list=weather_list;
    }

    @NonNull
    @Override
    public weatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(row_layout,parent,false);
        return new weatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final weatherViewHolder holder, final int position) {

        final list object = weather_list.get(position);
        holder.temperature.setText((String.valueOf(Math.round(object.getMain().getTemp()-273))+"\u2103"));
        holder.pressure.setText(" " + String.valueOf(object.getMain().getPressure())+" hPa");
        holder.humidity.setText(" " + String.valueOf(object.getMain().getHumidity())+" % Humid");
        holder.wind_speed.setText(" " + String.valueOf(object.getWind().getSpeed())+" m/sec");
        holder.description.setText("("+object.getWeather().get(0).getDescription()+")");


        Glide.with(context)
                .load("http://openweathermap.org/img/w/"+object.getWeather().get(0).getIcon()+".png")
                .into(holder.night_day);


        Long seconds = object.getDt();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        Date date = new Date(seconds * 1000);
        holder.day.setText(formatter.format(date));

        final SharedPreferences sharedPreferences =context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
        holder.city.setText(sharedPreferences.getString("city name",null));

        holder.hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("description",object.getWeather().get(0).getDescription());
                editor.apply();

                SecondActivity.getInterface1().openBottomSheet(weather_list.get(position));
            }
        });



    }

    @Override
    public int getItemCount() {
        return weather_list.size();
    }

    class weatherViewHolder extends RecyclerView.ViewHolder {

        private TextView temperature;
        private TextView city;
        private TextView pressure;
        private TextView wind_speed;
        private TextView day;
        private TextView humidity;
        private ImageView night_day;
        private TextView description;
        private TextView hamburger;


        public weatherViewHolder(@NonNull View itemView) {
            super(itemView);


            day=(TextView)itemView.findViewById(R.id.day);
            temperature=(TextView)itemView.findViewById(R.id.temperature);
            city=(TextView)itemView.findViewById(R.id.city_name);
            night_day=(ImageView) itemView.findViewById(R.id.night_day);
            humidity=(TextView)itemView.findViewById(R.id.Humidty);
            description=(TextView)itemView.findViewById(R.id.description);
            pressure=(TextView) itemView.findViewById(R.id.pressure);
            wind_speed=(TextView)itemView.findViewById(R.id.wind_speed);
            hamburger=(TextView)itemView.findViewById(R.id.hamburger);


        }
    }
}
