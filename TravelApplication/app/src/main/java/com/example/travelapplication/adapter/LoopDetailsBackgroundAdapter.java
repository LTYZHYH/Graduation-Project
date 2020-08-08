package com.example.travelapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.travelapplication.component.popup.ShowStrategyImageDialog;
import com.example.travelapplication.model.TravelStrategy;

import java.util.ArrayList;

public class LoopDetailsBackgroundAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<ImageView> imageViewList;

    //以下是攻略详情才用的控件
    private boolean isS;
    private String imgPath1;
    private String imgPath2;
    private String imgPath3;
    private Integer strId;
    private TravelStrategy travelStrategy;
    private ShowStrategyImageDialog showStrategyImageDialog;

    public LoopDetailsBackgroundAdapter(Context context,ArrayList<ImageView> mImgList){
        this.mContext = context;
        this.imageViewList = mImgList;
    }

    public LoopDetailsBackgroundAdapter(Context context,ArrayList<ImageView> mImgList, boolean isStrategy,Integer strategyId){
        this.mContext = context;
        this.imageViewList = mImgList;
        this.isS = isStrategy;
        this.strId = strategyId;

    }

    // 1. 返回要显示的条目内容, 创建条目
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // container: 容器: ViewPager
        // position: 当前要显示条目的位置 0 -> ？
        //newPosition = position % ？
        int newPosition = position % imageViewList.size();
        final ImageView img = imageViewList.get(newPosition);
        /**
            防止出现java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
         */
        ViewGroup parent= (ViewGroup) img.getParent();
        if(parent!=null){
            parent.removeView(img);
        }
        // a. 把View对象添加到container中
        container.addView(img);

//        if (isS == true){
//            TravelStrategyBusinessService travelStrategyBusinessService = new TravelStrategyBusinessService();
//            travelStrategyBusinessService.getStrategyDatail(new OnResultListener() {
//                @Override
//                public void onSuccess(Object object) {
//                    travelStrategy = (TravelStrategy) object;
//                    imgPath1 = travelStrategy.getStrategyPicture1();
//                    imgPath2 = travelStrategy.getStrategyPicture2();
//                    imgPath3 = travelStrategy.getStrategyPicture3();
//                }
//
//                @Override
//                public void onError(Object object) {
//
//                }
//            },strId);
//            switch (newPosition){
//                case 0:showStrategyImageDialog = new ShowStrategyImageDialog(mContext,imgPath3);
//                break;
//                case 1:showStrategyImageDialog = new ShowStrategyImageDialog(mContext,imgPath1);
//                break;
//                case 2:showStrategyImageDialog = new ShowStrategyImageDialog(mContext,imgPath2);
//                break;
//            }
//            img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showStrategyImageDialog.show();
//                }
//            });
//        }

        // b. 把View对象返回给框架, 适配器
        return img;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        注释这句以解决用手滑动轮播图时出现空白页
//        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;   //返回一个无限大的值，可以 无限循环!!!!!
    }

    /**
     * 判断是否使用缓存, 如果返回的是true, 使用缓存. 不去调用instantiateItem方法创建一个新的对象
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
