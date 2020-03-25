package com.example.infectionlive;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class bedsAdapter extends RecyclerView.Adapter<bedsAdapter.bedsHolder>{

    private Context context;
    private ArrayList<beds> bedss;

    public bedsAdapter(Context context, ArrayList<beds> bedss) {
        this.context = context;
        this.bedss = bedss;
    }


    @Override
    public bedsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_beds, parent, false);
        return new bedsHolder(view);
    }

    @Override
    public void onBindViewHolder(bedsHolder holder, int position) {
        beds beds = bedss.get(position);
        holder.setDetails(beds);
    }

    @Override
    public int getItemCount() {
        return bedss.size();
    }

    public class bedsHolder extends RecyclerView.ViewHolder {
        private TextView state,rural_hospitals,rural_beds,urban_hospitals,urban_beds,total_hospitals,total_beds;


        public bedsHolder(View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.beds_state);
            rural_hospitals = itemView.findViewById(R.id.rural_hospitals);
            rural_beds = itemView.findViewById(R.id.rural_beds);
            urban_beds = itemView.findViewById(R.id.urban_beds);
            urban_hospitals = itemView.findViewById(R.id.urban_hospitals);
        }
        public void setDetails(beds beds) {
//            if(beds.getState().equals("Poor")){
//                itemView.setBackgroundColor(Color.parseColor("#ff0000"));
//            }else if (beds.getState().equals("Good")){
//                itemView.setBackgroundColor(Color.parseColor("#00A0FF"));
//            }else{
//                itemView.setBackgroundColor(Color.parseColor("#00796B"));
//            }


            state.setText(beds.getState());
            rural_beds.setText("rural beds: "+Integer.toString(beds.getRural_beds()));
            rural_hospitals.setText("rural hospitals: "+Integer.toString(beds.getRural_beds()));
            rural_beds.setText("rural beds: "+Integer.toString(beds.getRural_beds()));
            rural_beds.setText("rural beds: "+Integer.toString(beds.getRural_beds()));
            recovered.setText(Integer.toString(beds.getRecovered()));
            deaths.setText(Integer.toString(beds.getDeaths()));
            int total = beds.getForeign() + beds.getIndian();
            if(total>=50){
                itemView.setBackgroundColor(Color.parseColor("#5B2949"));
            }else if(total>=20){
                itemView.setBackgroundColor(Color.parseColor("#2E344E"));
            }else{
                itemView.setBackgroundColor(Color.parseColor("#004039"));
            }
            tot.setText("total : " +Integer.toString(total));

        }
    }
}




