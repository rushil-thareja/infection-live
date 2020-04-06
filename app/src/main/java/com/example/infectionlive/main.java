package com.example.infectionlive;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class main extends Activity {
    private ImageView graph , history , beds , help;
    private RecyclerView recyclerView;
    private RecyclerView bed;
    private statsAdapter adapter;
    private bedsAdapter bedsadapter;
    private ArrayList<stats> planetArrayList;
    private ArrayList<beds> hospitals;
    private OkHttpClient client;
    private TextView tot;
    LottieAnimationView refresh;
    int current;
    private TextView which;
    private TextView stats_text , history_text , beds_text , helplines_text;
    private LineChart chart;
    private LinearLayout ll;
    private List<Entry> entries = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private TextView t;
    private Spinner spinner;
    int fill;
    int fill2;
    String[] dates;
    int[] vals;
    FloatingActionButton stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        current = 0;
        graph = findViewById(R.id.graph);
        history = findViewById(R.id.history);
        beds = findViewById(R.id.beds);
        help = findViewById(R.id.help);
        tot = (TextView) findViewById(R.id.tot);
        which = (TextView) findViewById(R.id.which);
        stats_text = findViewById(R.id.graph1);
        history_text = findViewById(R.id.history1);
        beds_text = findViewById(R.id.beds1);
        helplines_text = findViewById(R.id.help1);
        chart = (LineChart) findViewById(R.id.chart);
        ll = findViewById(R.id.llchart);
        t = findViewById(R.id.tot);
        client = new OkHttpClient();
        fill = 0;
        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(this,
                R.array.india_states, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapters);
        //spinner.setOnItemSelectedListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("chu dia",i+" "+l+" "+adapterView.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        categories.add("india");
        categories.add("Andaman and Nicobar Islands");
        categories.add("Andhra Pradesh");
        categories.add("Arunachal Pradesh");
        categories.add("Assam");
        categories.add("Bihar");
        categories.add("Chandigarh");
        categories.add("Chhattisgarh");
        categories.add("Dadra and Nagar Haveli");
        categories.add("Daman and Diu");
        categories.add("Delhi");
        categories.add("Goa");
        categories.add("Gujarat");
        categories.add("Haryana");
        categories.add("Himachal Pradesh");
        categories.add("Jammu and Kashmir");
        categories.add("Jharkhand");
        categories.add("Karnataka");
        categories.add("Kerala");
        categories.add("Lakshadweep");
        categories.add("Madhya Pradesh");
        categories.add("Maharashtra");
        categories.add("Manipur");
        categories.add("Meghalaya");
        categories.add("Mizoram");
        categories.add("Nagaland");
        categories.add("Orissa");
        categories.add("Pondicherry");
        categories.add("Punjab");
        categories.add("Rajasthan");
        categories.add("Sikkim");
        categories.add("Tamil Nadu");
        categories.add("Telangana");
        categories.add("Tripura");
        categories.add("Uttaranchal");
        categories.add("Uttar Pradesh");
        categories.add("West Bengal");

        //get_dates("India");

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bed = (RecyclerView) findViewById(R.id.beds_recy);
        bed.setLayoutManager(new LinearLayoutManager(this));

        planetArrayList = new ArrayList<>();
        hospitals = new ArrayList<>();

        adapter = new statsAdapter(this, planetArrayList);
        recyclerView.setAdapter(adapter);

        bedsadapter = new bedsAdapter(this,hospitals);
        bed.setAdapter(bedsadapter);

        refresh = findViewById(R.id.refresh);


        stop = findViewById(R.id.fab);

        graph.setBackgroundResource(R.drawable.ic_graph__blue);
        beds.setBackgroundResource(R.drawable.ic_beds_black);
        history.setBackgroundResource(R.drawable.ic_history_black);
        help.setBackgroundResource(R.drawable.ic_help_black);

        load_stats();

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();
                float y = e.getY();
                String selected_day = dates[(int)x];

                String to_be_set = "day : "+selected_day+" cases : " + y;
                TextView set = findViewById(R.id.chosen);
                set.setText(to_be_set);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stop.hide();

                chart.clear();
                make_black();
                TextView set = findViewById(R.id.chosen);
                set.setVisibility(View.VISIBLE);
                t.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(main.class,R.layout.support_simple_spinner_dropdown_item);

//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        Log.d("chua",(String)adapterView.getSelectedItem());
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                });

                current = 2;

                history_text.setTextColor(Color.parseColor("#00A0FF"));
                history.setBackgroundResource(R.drawable.ic_history_blue);
                which.setText("Historical Statistics");
                refresh.setVisibility(View.VISIBLE);
                refresh.playAnimation();

                dates = new String[10000];
                vals = new int[10000];
                get_dates("India",dates,vals);




                recyclerView.setVisibility(View.INVISIBLE);
                bed.setVisibility(View.INVISIBLE);
                pause_buttons();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < fill ; i++){
                            Log.d("gash",dates[i]);
                        }

                        ll.setVisibility(View.VISIBLE);
                        chart.setVisibility(View.VISIBLE);


                        refresh.cancelAnimation();
                        refresh.setVisibility(View.INVISIBLE);



                        String[] pass = new String[fill];

                        for(int i = 0; i < fill ; i++){
                            pass[i] = Integer.toString(i);
                        }

                        for(int i = 0 ; i < fill ; i++){

                            entries.add(new Entry(i,vals[i]));
                        }
                        LineDataSet dataSet = new LineDataSet(entries, " ");

                        dataSet.setDrawCircles(true);
                        dataSet.setValueTextColor(Color.parseColor("#00A0FF"));
                        dataSet.setValueTextSize(16);
                        dataSet.setHighLightColor(Color.parseColor("#ffffff"));
                        dataSet.setFillColor(Color.parseColor("#00A0FF"));
                        LineData lineData = new LineData(dataSet);
                        chart.setData(lineData);
                        chart.invalidate();
                        chart.animateXY(1000,2000);
                        Description d = new Description();
                        d.setText("start day : "+dates[0]+"end day : "+dates[fill-1]);
                        d.setTextColor(Color.parseColor("#ffffff"));
                        //d.setPosition(d.getXOffset(),d.getYOffset()-2f);
                        d.setTextSize(14f);
                        chart.setDescription(d);

                        XAxis xAxis = chart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setTextColor(Color.parseColor("#ffffff"));
                        xAxis.setTextSize(16f);



                        xAxis.setValueFormatter(new DateAxisValueFormatter(pass));

                        YAxis leftAxis = chart.getAxisLeft();
                        leftAxis.setTextColor(Color.parseColor("#ffffff"));
                        leftAxis.setTextSize(16f);

                        YAxis rightAxis = chart.getAxisRight();
                        rightAxis.setTextColor(Color.parseColor("#211348"));
                        play_buttons();

                    }
                }, 3000);



            }
        });

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop.show();
                t.setVisibility(View.VISIBLE);
                current = 0;
                make_black();
                stats_text.setTextColor(Color.parseColor("#00A0FF"));
                graph.setBackgroundResource(R.drawable.ic_graph__blue);
                which.setText("Latest Statistics");
                load_stats();
                recyclerView.setVisibility(View.INVISIBLE);
                bed.setVisibility(View.INVISIBLE);
                refresh.setVisibility(View.VISIBLE);
                refresh.playAnimation();
                pause_buttons();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        play_buttons();
                        refresh.cancelAnimation();
                        refresh.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);



                    }
                }, 1000);
            }
        });

        beds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tot.setVisibility(View.VISIBLE);
                stop.show();
                t.setVisibility(View.VISIBLE);
                current = 1;
                make_black();
                beds_text.setTextColor(Color.parseColor("#00A0FF"));
                beds.setBackgroundResource(R.drawable.ic_beds_blue);
                which.setText("Hospital Statistics");
                load_beds();
                recyclerView.setVisibility(View.INVISIBLE);
                bed.setVisibility(View.INVISIBLE);
                refresh.setVisibility(View.VISIBLE);
                refresh.playAnimation();
                pause_buttons();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        play_buttons();
                        refresh.cancelAnimation();
                        refresh.setVisibility(View.INVISIBLE);
                        bed.setVisibility(View.VISIBLE);



                    }
                }, 1000);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current==0){
                    load_stats();
                    refresh.setVisibility(View.VISIBLE);
                    refresh.playAnimation();
                    recyclerView.setVisibility(View.INVISIBLE);
                    pause_buttons();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            play_buttons();
                            refresh.cancelAnimation();
                            refresh.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);



                        }
                    }, 3000);
                }else if (current ==1){
                    load_beds();
                    refresh.setVisibility(View.VISIBLE);
                    refresh.playAnimation();
                    bed.setVisibility(View.INVISIBLE);
                    play_buttons();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            pause_buttons();
                            refresh.cancelAnimation();
                            refresh.setVisibility(View.INVISIBLE);
                            bed.setVisibility(View.VISIBLE);



                        }
                    }, 3000);
                }


            }
        });

        //load_stats();
    }
    public  void get_dates(final String state , final String[] dat , final int[] vals){

        final ArrayList<String> d = new ArrayList<>();
        final ArrayList<Integer> v = new ArrayList<>();
        final Request request = new Request.Builder().url("https://api.rootnet.in/covid19-in/unofficial/covid19india.org/statewise/history").build();
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
                            //hospitals.clear();

                            String  res = response.body().string();

                            JSONObject out = new JSONObject(res);
                            boolean suc = out.getBoolean("success");
                            if(suc != true){
                                Toast.makeText(main.this,"sorry could not get valid data",Toast.LENGTH_LONG).show();
                                return;
                            }
                            Log.d("dates ayi",res);
                            JSONObject di = out.getJSONObject("data");
                            JSONArray data = di.getJSONArray("history");
                            if(state.equals("India")){

                            }
                            for(int i =0 ; i< data.length(); i++){
                                JSONObject o = data.getJSONObject(i);
                                JSONObject summ = o.getJSONObject("total");
                                v.add(summ.getInt("confirmed"));
                                d.add(o.getString("day"));
                                Log.d("date at",d.get(i));
                            }

                            fill = d.size();
                            fill2 = v.size();
                            for(int i = 0 ; i<d.size(); i++){

                                vals[i] = v.get(i);
                                dat[i] = d.get(i);
                                Log.d("date put in . . .",dat[i]);
                            }


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

    public void load_beds(){
        final Request request = new Request.Builder().url("https://api.rootnet.in/covid19-in/stats/hospitals").build();
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
                            hospitals.clear();

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
                            int tot = summ.getInt("ruralHospitals");
                            int indian = summ.getInt("ruralBeds");
                            int foreign = summ.getInt("urbanHospitals");
                            int recovered = summ.getInt("urbanBeds");
                            int deaths = summ.getInt("totalHospitals");
                            int confirmed_but_unknown = summ.getInt("totalBeds");

                            final String fin = "total hospitals: "+deaths + "total beds: "+confirmed_but_unknown+"rural hospitals: "+tot+"| rural beds: " + indian + "| urban hospitals: " + foreign + "| urban Beds: " + recovered;
                            Log.d("dat",fin);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView t = findViewById(R.id.tot);
                                    t.setText(" ");
                                }
                            });

                            JSONArray items = data.getJSONArray("regional");
                            for(int i = 0 ; i < items.length() ; i++){
                                JSONObject current = items.getJSONObject(i);
                                String name = current.getString("state");
                                Log.d("dat","sehenhsa"+name);
                                int india = current.getInt("ruralHospitals");
                                int fore = current.getInt("ruralBeds");
                                int dis = current.getInt("urbanHospitals");
                                int death = current.getInt("urbanBeds");
                                int toth = current.getInt("totalHospitals");
                                int totb = current.getInt("totalBeds");

                                hospitals.add(new beds(name,india,fore,dis,death,toth,totb));


                            }

                            Collections.sort(hospitals);



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
    public void load_stats(){
        final Request request = new Request.Builder().url("https://api.rootnet.in/covid19-in/unofficial/covid19india.org/statewise").build();
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
                            JSONObject summ = data.getJSONObject("total");
                            int tot = summ.getInt("confirmed");
                            int indian = summ.getInt("active");
                            //int foreign = summ.getInt("confirmedCasesForeign");
                            int recovered = summ.getInt("recovered");
                            int deaths = summ.getInt("deaths");
                            int confirmed_but_unknown = 0;

                            final String fin = "total: "+tot+"| active: " + indian +  "| recovered: " + recovered + "| deaths: " + deaths;
                            Log.d("dat",fin);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView t = findViewById(R.id.tot);
                                    t.setText(fin);
                                }
                            });

                            JSONArray items = data.getJSONArray("statewise");
                            for(int i = 0 ; i < items.length() ; i++){
                                JSONObject current = items.getJSONObject(i);
                                String name = current.getString("state");
                                //Log.d("dat","sehenhsa"+name);
                                int india = current.getInt("confirmed");
                                int fore = current.getInt("active");
                                int dis = current.getInt("recovered");
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
    public void make_black(){
        spinner.setVisibility(View.GONE);
        TextView set = findViewById(R.id.chosen);

        graph.setBackgroundResource(R.drawable.ic_graph_black);
        beds.setBackgroundResource(R.drawable.ic_beds_black);
        history.setBackgroundResource(R.drawable.ic_history_black);
        help.setBackgroundResource(R.drawable.ic_help_black);

        stats_text.setTextColor(Color.parseColor("#ffffff"));
        history_text.setTextColor(Color.parseColor("#ffffff"));
        beds_text.setTextColor(Color.parseColor("#ffffff"));
        helplines_text.setTextColor(Color.parseColor("#ffffff"));

        ll.setVisibility(View.GONE);
        chart.setVisibility(View.GONE);
    }
    public void pause_buttons(){
        graph.setClickable(false);
        beds.setClickable(false);
        history.setClickable(false);
        help.setClickable(false);
        stop.hide();
    }
    public void play_buttons(){
        graph.setClickable(true);
        beds.setClickable(true);
        history.setClickable(true);
        help.setClickable(true);
        stop.show();
    }

    public void selected_state(String state){
        Log.d("chua",state);
    }

//
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        Log.d("chua",(String)adapterView.getSelectedItem());
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
}
