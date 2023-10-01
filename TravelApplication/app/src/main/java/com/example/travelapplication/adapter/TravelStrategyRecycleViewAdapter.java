package com.example.travelapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.R;
import com.example.travelapplication.activity.details.TravelStrategyDetailsActivity;
import com.example.travelapplication.activity.details.cityDetailsActivity;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.model.TravelStrategy;

import java.util.List;

public class TravelStrategyRecycleViewAdapter extends RecyclerView.Adapter<TravelStrategyRecycleViewAdapter.GridViewHolder> {

    private Context mContext;
    private List<TravelStrategy> travelStrategyBeanList;

    public TravelStrategyRecycleViewAdapter(Context context){
        mContext = context;
    }

    public void setGridDataList(List<TravelStrategy> list) {
        travelStrategyBeanList = list;

        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TravelStrategyRecycleViewAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.travel_strategy_item, parent, false);
        return new TravelStrategyRecycleViewAdapter.GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelStrategyRecycleViewAdapter.GridViewHolder holder, final int position) {
        holder.theme.setText(travelStrategyBeanList.get(position).getTheme());
        holder.area.setText("收藏数:" + travelStrategyBeanList.get(position).getFavoriteNum());
        if (!TextUtils.isEmpty(travelStrategyBeanList.get(position).getStrategyPicture1())){
            GlideApp.with(mContext)
                    .load(BuildConfig.BASE_URL + "/travelstrategy/picture/" + travelStrategyBeanList.get(position).getStrategyPicture1())//获取图片（IP+文件储存路径）
                    .placeholder(R.drawable.loading)//加载未完成时显示占位图
                    .into(holder.strategyPicture1);
        } else {
            GlideApp.with(mContext)
                    .load(R.drawable.loading)
                    .into(holder.strategyPicture1);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityDetailsActivity cityDetailsActivity = (cityDetailsActivity) mContext;
                Intent intent = new Intent(cityDetailsActivity, TravelStrategyDetailsActivity.class);
                intent.putExtra("strategy_id", travelStrategyBeanList.get(position).getStrategyId());
                cityDetailsActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return travelStrategyBeanList == null ? 0 : travelStrategyBeanList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        TextView area;
        TextView theme;
        ImageView strategyPicture1;

        public GridViewHolder(View itemView) {
            super(itemView);
            area = itemView.findViewById(R.id.ts_area);
            theme = itemView.findViewById(R.id.ts_theme);
            strategyPicture1 = itemView.findViewById(R.id.strategy_picture_1);
        }

    }
}
