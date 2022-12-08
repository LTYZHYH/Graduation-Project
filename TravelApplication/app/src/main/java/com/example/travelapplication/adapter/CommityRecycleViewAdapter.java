package com.example.travelapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.R;
import com.example.travelapplication.activity.comment.CommentStrategyActivity;
import com.example.travelapplication.activity.comment.ReplyActivity;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.model.Commity;
import com.example.travelapplication.service.business.ReportBusinessService;
import com.example.travelapplication.service.business.listener.OnResultListener;

import java.util.List;

import static com.example.travelapplication.infrastructure.utils.TimeFormatUtils.getFormatDatetime;

public class CommityRecycleViewAdapter extends RecyclerView.Adapter<CommityRecycleViewAdapter.GridViewHolder> {
    private Context mContext;
    private List<Commity> commityBeanList;

    public CommityRecycleViewAdapter(Context context){
        mContext = context;
    }

    public void setGridDataList(List<Commity> list) {
        commityBeanList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommityRecycleViewAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_strategy_item, parent, false);
        return new CommityRecycleViewAdapter.GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, final int position) {
        holder.userName.setText(commityBeanList.get(position).getUser().getUserName());
        holder.replyContent.setText(commityBeanList.get(position).getCommityContent());
        holder.replyDate.setText(getFormatDatetime(commityBeanList.get(position).getCommityTime()));
        GlideApp.with(mContext)
                .load(BuildConfig.BASE_URL + "/user/userPicture/" + commityBeanList.get(position).getUser().getUserPhoto())
                .placeholder(R.drawable.loading)
                .into(holder.userPicture);
        holder.toReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentStrategyActivity commentStrategyActivity = (CommentStrategyActivity) mContext;
                Intent intent = new Intent(commentStrategyActivity, ReplyActivity.class);
                intent.putExtra("commentId", commityBeanList.get(position).getCommityId());
                commentStrategyActivity.startActivity(intent);
            }
        });
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportBusinessService reportBusinessService = new ReportBusinessService();
                reportBusinessService.report(new OnResultListener() {
                    @Override
                    public void onSuccess(Object object) {
                        SnackBarUtils.showSuccessMessage(holder.report,"举报成功");
                    }

                    @Override
                    public void onError(Object object) {

                    }
                },commityBeanList.get(position).getCommityId(),"reportCommit");
            }
        });
    }


    @Override
    public int getItemCount() {
        return commityBeanList == null ? 0 : commityBeanList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView replyContent;
        TextView replyDate;
        ImageView userPicture;
        ImageButton toReply;
        ImageButton report;//举报按钮

        public GridViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            replyContent = itemView.findViewById(R.id.reply_content);
            replyDate = itemView.findViewById(R.id.reply_date);
            userPicture = itemView.findViewById(R.id.user_photo);
            toReply = itemView.findViewById(R.id.to_reply);
            report = itemView.findViewById(R.id.report_reply);
        }

    }
}
