package com.example.travelapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.R;
import com.example.travelapplication.component.popup.ShowFoodImageDialog;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.model.Food;

import java.util.List;

public class WaterfallAdapter extends RecyclerView.Adapter<WaterfallAdapter.WaterfallViewHolder> {
    private Context mContext;

    private List<Food> foodList;

    public WaterfallAdapter(Context context) {
        mContext = context;
    }

    public void setWaterfallData(List<Food> list) {
        foodList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WaterfallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.waterfall_recycle_item, parent, false);
        return new WaterfallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WaterfallViewHolder holder, final int position) {
        holder.foodName.setText(foodList.get(position).getFoodName());
        holder.foodArea.setText(foodList.get(position).getArea().getAreaName());
        if (position % 2 == 0){
            holder.foodItem.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 450));
        } else {
            holder.foodItem.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600));
        }
        GlideApp.with(mContext)
                .load(BuildConfig.BASE_URL + "/food/FoodPicture/" + foodList.get(position).getFoodPhoto())
                .placeholder(R.drawable.loading)
                .into(holder.foodPhoto);
        holder.foodPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFoodImageDialog showFoodImageDialog = new ShowFoodImageDialog(mContext,foodList.get(position).getFoodIntroduction(),foodList.get(position).getFoodPhoto());
                showFoodImageDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList == null ? 0 : foodList.size();
    }

    public class WaterfallViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView foodArea;
        ImageView foodPhoto;
        CardView foodItem;

        public WaterfallViewHolder(View itemView) {
            super(itemView);
            foodPhoto = itemView.findViewById(R.id.food_photo);
            foodArea = itemView.findViewById(R.id.foodArea);
            foodName = itemView.findViewById(R.id.foodName);
            foodItem = itemView.findViewById(R.id.food_item);
        }
    }
}
