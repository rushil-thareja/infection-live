package com.example.infectionlive;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class disclaimer extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disclaimer);
        final SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        String pass =  sharedPref.getString("user_name","default");
        if(!pass.equals("default")){
            Intent ccco = new Intent(disclaimer.this,loading.class);
            startActivity(ccco);
            finish();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    sharedPref.edit().putString("user_name","name").commit();
                    Intent ccco = new Intent(disclaimer.this,loading.class);
                    startActivity(ccco);
                    finish();
                }
            }, 5000);
        }



    }
}

