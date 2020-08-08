package com.example.travelapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapplication.R;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.model.Reply;
import com.example.travelapplication.service.business.ReportBusinessService;
import com.example.travelapplication.service.business.listener.OnResultListener;

import java.util.List;

import static com.example.travelapplication.infrastructure.utils.TimeFormatUtils.getFormatDatetime;

public class ReplyRecycleViewAdapter extends RecyclerView.Adapter<ReplyRecycleViewAdapter.GridViewHolder>{
    private Context mContext;
    private List<Reply> replyList;

    public ReplyRecycleViewAdapter(Context context){
        mContext = context;
    }

    public void setGridDataList(List<Reply> list) {
        replyList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reply_item, parent, false);
        return new ReplyRecycleViewAdapter.GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, final int position) {
        holder.userName.setText(replyList.get(position).getUser().getUserName());
        holder.replyContent.setText(replyList.get(position).getReplyContent());
        holder.replyDate.setText(getFormatDatetime(replyList.get(position).getReplyTime()));
        GlideApp.with(mContext)
                .load(Global_Variable.IP + "/user/userPicture/" + replyList.get(position).getUser().getUserPhoto())
                .placeholder(R.drawable.loading)
                .into(holder.userPicture);
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
                },replyList.get(position).getReplyId(),"reportReply");
            }
        });
    }

    @Override
    public int getItemCount() {
        return replyList == null ? 0 : replyList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView replyContent;
        TextView replyDate;
        ImageView userPicture;
        ImageButton report;

        public GridViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.reply_user_name);
            replyContent = itemView.findViewById(R.id.reply_content);
            replyDate = itemView.findViewById(R.id.reply_time);
            userPicture = itemView.findViewById(R.id.reply_user_photo);
            report = itemView.findViewById(R.id.r_reply);
        }

    }
}
