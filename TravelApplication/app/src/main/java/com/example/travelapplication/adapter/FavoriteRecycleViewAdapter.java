package com.example.travelapplication.adapter;

import android.app.Dialog;
import android.content.Intent;
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
import com.example.travelapplication.activity.register.PersonalCenterActivity;
import com.example.travelapplication.infrastructure.utils.DecideErrorUtils;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.model.Favorite;
import com.example.travelapplication.service.business.UserBusinessService;
import com.example.travelapplication.service.business.listener.OnResultListener;

import java.util.List;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;

public class FavoriteRecycleViewAdapter extends RecyclerView.Adapter<FavoriteRecycleViewAdapter.GridViewHolder> {
    private PersonalCenterActivity mContext;
    private List<Favorite> favoriteList;
    private Dialog loadingDialog;
    public FavoriteRecycleViewAdapter(PersonalCenterActivity context){
        mContext = context;
    }
    public void setGridDataList(List<Favorite> list) {
        favoriteList = list;

        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_favorite_strategy_item,parent,false);
        return new FavoriteRecycleViewAdapter.GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, final int position) {
        holder.favoriteTitle.setText(favoriteList.get(position).getTravelStrategy().getTheme());
        GlideApp.with(mContext)
                .load(BuildConfig.BASE_URL + "/travelstrategy/picture/" + favoriteList.get(position).getTravelStrategy().getStrategyPicture1())
                .placeholder(R.drawable.loading)//加载未完成时显示占位图
                .into(holder.favoritePic);

        holder.favoriteTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalCenterActivity personalCenterActivity = mContext;
                Intent intent = new Intent(personalCenterActivity, TravelStrategyDetailsActivity.class);
                intent.putExtra("strategy_id", favoriteList.get(position).getTravelStrategy().getStrategyId());
                personalCenterActivity.startActivity(intent);
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog = createLoadingDialog(holder.cancel.getContext());
                UserBusinessService userBusinessService = new UserBusinessService();
                userBusinessService.cancelFavorite(new OnResultListener() {
                    @Override
                    public void onSuccess(Object object) {
                        mContext.refreshData(2);
                        closeLoadingDialog(loadingDialog);
                    }

                    @Override
                    public void onError(Object object) {
                        DecideErrorUtils.showErrorMessage(holder.cancel, object);
                        closeLoadingDialog(loadingDialog);
                    }
                },favoriteList.get(position).getTravelStrategy().getStrategyId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList == null ? 0 : favoriteList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        TextView favoriteTitle;
        ImageView favoritePic;
        TextView cancel;

        public GridViewHolder(View itemView) {
            super(itemView);
            favoriteTitle = itemView.findViewById(R.id.favorite_title);
            favoritePic = itemView.findViewById(R.id.favorite_pic);
            cancel = itemView.findViewById(R.id.cancel_favorite);
        }
    }
}
