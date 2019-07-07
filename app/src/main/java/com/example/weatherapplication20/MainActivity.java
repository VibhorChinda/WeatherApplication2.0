package com.example.weatherapplication20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private TextView Latitude;
    private TextView Longitude;
    private CheckBox checkbox_location;
    private Button submit;
    private String current;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkbox_location=(CheckBox)findViewById(R.id.checkbox);
        Latitude=(TextView)findViewById(R.id.lat);
        Longitude=(TextView)findViewById(R.id.lon);
        submit=(Button)findViewById(R.id.submit_button);


        checkbox_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkbox_location.isChecked()) {
                    Latitude.setFocusable(false);
                    Longitude.setFocusable(false);
                    current="true";
                }
                else
                {
                    Latitude.setFocusableInTouchMode(true);
                    Longitude.setFocusableInTouchMode(true);
                    current="false";
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Longitude.getText().toString().isEmpty() && Latitude.getText().toString().isEmpty() && ! checkbox_location.isChecked()) {
                    Snackbar.make(submit,"Empty Fields", Snackbar.LENGTH_SHORT)
                            .show();
                }

                else if(Longitude.getText().toString().isEmpty() && !checkbox_location.isChecked() || Latitude.getText().toString().isEmpty() && !checkbox_location.isChecked())
                {
                    Snackbar.make(submit,"Fill All Fields", Snackbar.LENGTH_SHORT)
                            .show();
                }


                else{

                    Intent intent= new Intent(MainActivity.this,SecondActivity.class);
                    sharedPreferences = getApplicationContext().getSharedPreferences("preferences",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("current",current);
                    if(checkbox_location.isChecked() )
                    {
                        editor.putString("lat","none");
                        editor.putString("lon","none");
                    }
                    else
                    {
                        editor.putString("lat",Latitude.getText().toString());
                        editor.putString("lon",Longitude.getText().toString());
                    }
                    editor.apply();
                    startActivity(intent);
            } }
        });
    }

}
