package com.example.travelapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapplication.R;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.GridViewHolder> {
    private Context mContext;
    private List<ForecastBase> forecastBaseList;

    public WeatherRecyclerViewAdapter(Context context){
        mContext = context;
    }
    public void setGridDataList(List<ForecastBase> forecastBases) {
        forecastBaseList = forecastBases;

        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_item, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        holder.wdate1.setText(forecastBaseList.get(position).getDate());
        holder.wmax1.setText(forecastBaseList.get(position).getTmp_max());
        holder.wmin1.setText(forecastBaseList.get(position).getTmp_min());
        holder.weather1.setText(forecastBaseList.get(position).getCond_txt_d());
        holder.weatherN1.setText(forecastBaseList.get(position).getCond_txt_n());
    }

    @Override
    public int getItemCount() {
        return forecastBaseList == null ? 0 : forecastBaseList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        private TextView wdate1;
        private TextView wmax1;
        private TextView wmin1;
        private TextView weather1;
        private TextView weatherN1;

        public GridViewHolder(View itemView) {
            super(itemView);
            wdate1 = itemView.findViewById(R.id.w_date1);
            wmax1 = itemView.findViewById(R.id.w_max1);
            wmin1 = itemView.findViewById(R.id.w_min1);
            weather1 = itemView.findViewById(R.id.weather1);
            weatherN1 = itemView.findViewById(R.id.weatherN1);
        }

    }
}
