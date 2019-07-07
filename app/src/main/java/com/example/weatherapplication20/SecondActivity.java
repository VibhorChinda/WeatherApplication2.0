package com.example.weatherapplication20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String lat;
    private String lon;
    private static sheet_interface interface1;
    private ProgressBar pBar;

    private  int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=1;
    private String current_location;
    public static sheet_interface getInterface1() {
        return interface1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pBar=(ProgressBar)findViewById(R.id.pBar);

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

         if (Objects.equals(sharedPreferences.getString("current", null),"true")) {

             pBar.setVisibility(recyclerView.VISIBLE);
             ask_permission();
         }

         else
         {
             pBar.setVisibility(recyclerView.VISIBLE);
             networking();
         }
    }

    private void ask_permission() {


        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (ContextCompat.checkSelfPermission(SecondActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SecondActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                final AlertDialog dialog = new AlertDialog.Builder(SecondActivity.this).create();
                dialog.setTitle("Require Last Location");
                dialog.setMessage("We require your last location for this feature");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "ALLOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(SecondActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "DENY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();

            } else {
                ActivityCompat.requestPermissions(SecondActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }
        }
        else {

            FusedLocationProviderClient fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                editor.putString("lat",String.valueOf(location.getLatitude()));
                                editor.putString("lon",String.valueOf(location.getLongitude()));
                                editor.apply();

                                networking();
                            }
                        }
                    });

        }
    }

    private void networking() {

        final String base_url = "https://api.openweathermap.org/data/2.5/";
        final String api_key = "31f2de2ce0453a77aa01d33a0c54ccdb";


        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("preferences", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api_interface api = retrofit.create(api_interface.class);
        Call<ModalClass> call = api.GetWeatherConditions(sharedPreferences.getString("lat",null),sharedPreferences.getString("lon",null),api_key);

        call.enqueue(new Callback<ModalClass>() {
            @Override
            public void onResponse(Call<ModalClass> call, Response<ModalClass> response) {
                if (!response.isSuccessful()) {
                    pBar.setVisibility(recyclerView.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG);
                    toast.show();
                }

                else {

                    if (response.body().getCity().getName() == null)
                        editor.putString("city name", "Unknown Place");
                    else
                        editor.putString("city name", response.body().getCity().getName());
                    editor.apply();

                    final ArrayList<list> list1 = response.body().getList();

                    final ArrayList<list> list_final = new ArrayList<list>();
                    list_final.add(list1.get(0));
                    for (list obj : list1) {
                        if (list1.indexOf(obj) == 0)
                            continue;
                        else {
                            long seconds = obj.getDt() - list1.get(0).getDt();
                            if (seconds % 86400 == 0)
                                list_final.add(obj);
                        }
                    }

                    interface1 = new sheet_interface() {
                        @Override
                        public void openBottomSheet(list obj) {

                            BottomSheet sheet = new BottomSheet();
                            Bundle bundle = new Bundle();
                            bundle.putLong("unix_time", obj.getDt());
                            bundle.putDouble("temp_min", obj.getMain().getTemp_min());
                            bundle.putDouble("temp_max", obj.getMain().getTemp_max());
                            bundle.putDouble("sea_level", obj.getMain().getSea_level());
                            bundle.putInt("cloudiness", obj.getClouds().getAll());
                            bundle.putString("description", obj.getWeather().get(0).getDescription());
                            bundle.putString("icon", obj.getWeather().get(0).getIcon());
                            if(obj.getRain()!= null)
                                bundle.putString("rains",String.valueOf(obj.getRain().getVolume()));
                            else
                                bundle.putString("rains","");
                            sheet.setArguments(bundle);
                            sheet.show(getSupportFragmentManager(), sheet.getTag());
                        }
                    };

                    recyclerView.setAdapter(new weather_adapter(list_final, R.layout.view_layout, getApplicationContext()));
                    pBar.setVisibility(recyclerView.GONE);
                }
            }

            @Override
            public void onFailure(Call<ModalClass> call, Throwable t) {
                pBar.setVisibility(recyclerView.GONE);
                Toast toast = Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ask_permission();
                } else {
                    finish();
                }
                return;
            }
        }
    }
}
