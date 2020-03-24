package com.example.infectionlive;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class statsAdapter extends RecyclerView.Adapter<statsAdapter.statsHolder>{

    private Context context;
    private ArrayList<stats> statss;

    public statsAdapter(Context context, ArrayList<stats> statss) {
        this.context = context;
        this.statss = statss;
    }


    @Override
    public statsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new statsHolder(view);
    }

    @Override
    public void onBindViewHolder(statsHolder holder, int position) {
        stats stats = statss.get(position);
        holder.setDetails(stats);
    }

    @Override
    public int getItemCount() {
        return statss.size();
    }

    public class statsHolder extends RecyclerView.ViewHolder {
        private TextView state,indian,foreign,recovered,deaths,tot;


        public statsHolder(View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.txt_state);
            indian = itemView.findViewById(R.id.txt_indian);
            foreign = itemView.findViewById(R.id.txt_foreign);
            recovered = itemView.findViewById(R.id.txt_recovered);
            deaths = itemView.findViewById(R.id.txt_deaths);
            tot = itemView.findViewById(R.id.txt_total);
        }
        public void setDetails(stats stats) {
//            if(stats.getState().equals("Poor")){
//                itemView.setBackgroundColor(Color.parseColor("#ff0000"));
//            }else if (stats.getState().equals("Good")){
//                itemView.setBackgroundColor(Color.parseColor("#00A0FF"));
//            }else{
//                itemView.setBackgroundColor(Color.parseColor("#00796B"));
//            }


            state.setText(stats.getState());
            indian.setText(Integer.toString(stats.getIndian()));
            foreign.setText(Integer.toString(stats.getForeign()));
            recovered.setText(Integer.toString(stats.getRecovered()));
            deaths.setText(Integer.toString(stats.getDeaths()));
            int total = stats.getForeign() + stats.getIndian();
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




