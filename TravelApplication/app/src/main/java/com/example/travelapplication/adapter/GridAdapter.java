package com.example.travelapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapplication.R;
import com.example.travelapplication.activity.IndexActivity;
import com.example.travelapplication.activity.details.cityDetailsActivity;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.model.CityPictureBean;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

    private Context mContext;
    private String user;

    private List<CityPictureBean> cityPictureBeanList;


    public GridAdapter(Context context,String userName) {
        mContext = context;
        user = userName;
    }

    public void setGridDataList(List<CityPictureBean> list) {
        cityPictureBeanList = list;

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GridAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.index_item, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.GridViewHolder holder, final int position) {
        holder.cityTitle.setText(cityPictureBeanList.get(position).getCityTitle());
        GlideApp.with(mContext)
                .load(Global_Variable.IP + cityPictureBeanList.get(position).getCityPicture())//获取图片（IP+文件储存路径）
                .placeholder(R.drawable.loading)//加载未完成时显示占位图
                .into(holder.cityPicture);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexActivity indexActivity = (IndexActivity) mContext;
                Intent intent = new Intent(indexActivity, cityDetailsActivity.class);
                intent.putExtra("user_Name", user);
                intent.putExtra("city_id", cityPictureBeanList.get(position).getCityId());
                indexActivity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return cityPictureBeanList == null ? 0 : cityPictureBeanList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        TextView cityTitle;

        ImageView cityPicture;

        public GridViewHolder(View itemView) {
            super(itemView);
            cityTitle = itemView.findViewById(R.id.cityTitle);
            cityPicture = itemView.findViewById(R.id.cityPicture);
        }

    }
}
