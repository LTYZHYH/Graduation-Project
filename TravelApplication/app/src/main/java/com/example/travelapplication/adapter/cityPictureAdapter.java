package com.example.travelapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.travelapplication.R;
import com.example.travelapplication.model.CityPictureBean;

import java.util.List;

public class cityPictureAdapter extends BaseAdapter {
    private LinearLayout mlinearLayout;
    private Context mcontext;
    private List<CityPictureBean> mlist;

    public cityPictureAdapter(Context mcontext,List<CityPictureBean> mlist){
        this.mcontext = mcontext;
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //判断view是否可以重载
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mcontext);
            mlinearLayout = (LinearLayout) inflater.inflate(R.layout.index_item, null);
            //获取ID
            viewHolder.textTitle = mlinearLayout.findViewById(R.id.cityTitle);
            viewHolder.imageView = mlinearLayout.findViewById(R.id.cityPicture);
            //设置数据
            viewHolder.textTitle.setText(mlist.get(position).getCityTitle());

            //使用glide加载图片
            GlideApp.with(mcontext)
                    .load(mlist.get(position).getCityPicture())
                    .placeholder(R.drawable.logo)
                    //加载未完成时显示占位图
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //显示位置
                    .into(viewHolder.imageView);
            //标记当前view
            mlinearLayout.setTag(viewHolder);
        } else {
            //可以重载则直接使用原来的view
                    mlinearLayout = (LinearLayout) convertView;
            viewHolder = (ViewHolder) mlinearLayout.getTag();
            //获取id
            viewHolder.textTitle = mlinearLayout.findViewById(R.id.cityTitle);
            viewHolder.imageView = mlinearLayout.findViewById(R.id.cityPicture);
            //设置数据
            viewHolder.textTitle.setText(mlist.get(position).getCityTitle());
            //使用glide加载图片
            GlideApp.with(mcontext)
                    .load(mlist.get(position).getCityPicture()) //加载地址
                    .placeholder(R.drawable.logo)//加载未完成时显示占位图
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewHolder.imageView);//显示的位置
        }
        return mlinearLayout;
    }

    //使用viewHolder缓存数据
    static class ViewHolder {
        TextView textTitle;
        TextView url;
        ImageView imageView;
    }
}
