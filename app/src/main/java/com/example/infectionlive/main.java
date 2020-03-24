package com.example.infectionlive;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class main extends Activity {
    private ImageView graph , history , beds , help;
    private RecyclerView recyclerView;
    private statsAdapter adapter;
    private ArrayList<stats> planetArrayList;
    private OkHttpClient client;
    private TextView tot;
    LottieAnimationView refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        graph = findViewById(R.id.graph);
        history = findViewById(R.id.history);
        beds = findViewById(R.id.beds);
        help = findViewById(R.id.help);
        tot = (TextView) findViewById(R.id.tot);

        client = new OkHttpClient();

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        planetArrayList = new ArrayList<>();
        adapter = new statsAdapter(this, planetArrayList);
        recyclerView.setAdapter(adapter);

        refresh = findViewById(R.id.refresh);


        final FloatingActionButton stop = findViewById(R.id.fab);

        graph.setBackgroundResource(R.drawable.ic_graph__blue);
        beds.setBackgroundResource(R.drawable.ic_beds_black);
        history.setBackgroundResource(R.drawable.ic_history_black);
        help.setBackgroundResource(R.drawable.ic_help_black);


        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                load_stats();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                load_stats();
                refresh.setVisibility(View.VISIBLE);
                refresh.playAnimation();
                recyclerView.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        refresh.cancelAnimation();
                        refresh.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);



                    }
                }, 3000);

            }
        });

        load_stats();
    }
    public void load_stats(){
        final Request request = new Request.Builder().url("https://api.rootnet.in/covid19-in/stats/latest").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(main.this,"could not make connection check internet",Toast.LENGTH_LONG).show();

                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            planetArrayList.clear();

                            String  res = response.body().string();

                            JSONObject out = new JSONObject(res);
                                boolean suc = out.getBoolean("success");
                                if(suc != true){
                                Toast.makeText(main.this,"sorry could not get valid data",Toast.LENGTH_LONG).show();
                                return;
                            }
                            Log.d("dat",res);
                            JSONObject data = out.getJSONObject("data");
                            JSONObject summ = data.getJSONObject("summary");
                            int tot = summ.getInt("total");
                            int indian = summ.getInt("confirmedCasesIndian");
                            int foreign = summ.getInt("confirmedCasesForeign");
                            int recovered = summ.getInt("discharged");
                            int deaths = summ.getInt("deaths");
                            int confirmed_but_unknown = summ.getInt("confirmedButLocationUnidentified");

                            final String fin = "total: "+tot+"| indian: " + indian + "| foreign: " + foreign + "| recovered: " + recovered + "| deaths: " + deaths+"| not located : " + confirmed_but_unknown;
                            Log.d("dat",fin);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView t = findViewById(R.id.tot);
                                    t.setText(fin);
                                }
                            });

                            JSONArray items = data.getJSONArray("regional");
                            for(int i = 0 ; i < items.length() ; i++){
                                JSONObject current = items.getJSONObject(i);
                                String name = current.getString("loc");
                                Log.d("dat","sehenhsa"+name);
                                int india = current.getInt("confirmedCasesIndian");
                                int fore = current.getInt("confirmedCasesForeign");
                                int dis = current.getInt("discharged");
                                int death = current.getInt("deaths");

                                planetArrayList.add(new stats(name,india,fore,dis,death));


                            }

                            Collections.sort(planetArrayList);



                            adapter.notifyDataSetChanged();


//                            Log.d("dat","l"+items.length());
//                            for(int i = 0 ; i < items.length() ; i++){
//                                JSONObject obj  = items.getJSONObject(i);
//                                Log.d("dat",obj.toString());
//                                String name = obj.getString("ItemDescription");
//                                String age = Integer.toString(obj.getInt("ItemAge"));
//                                String security = obj.getString("Security");
//                                String fees = Integer.toString(obj.getInt("Fees"));
//                                String category = obj.getString("Category");
//                                String state = obj.getString("State");
//                                Log.d("dat",name+age+security+fees+"cat"+category+"stat"+state);
//                                planetArrayList.add(new Planet(name,age,security,fees,category,state));
//                            }
//                            adapter.notifyDataSetChanged();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(main.this,"sorry could not load data",Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                });
            }
        });

    }

}
