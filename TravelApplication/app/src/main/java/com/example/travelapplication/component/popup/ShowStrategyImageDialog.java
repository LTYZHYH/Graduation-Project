package com.example.travelapplication.component.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.R;
import com.example.travelapplication.adapter.GlideApp;
import com.example.travelapplication.infrastructure.utils.Global_Variable;

public class ShowStrategyImageDialog extends Dialog {
    private Context mContext;
    private String PicPath;
    private ImageView bigPic;
    private ImageView img;

    public ShowStrategyImageDialog(@NonNull Context context,String imgPath) {
        super(context,R.style.ShowImageDialog);
        this.mContext = context;
        this.PicPath = imgPath;
    }

    public ShowStrategyImageDialog(@NonNull Context context,ImageView imageView){
        super(context,R.style.ShowImageDialog);
        this.mContext = context;
        this.img = imageView;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_strategy_pic);
        bigPic = findViewById(R.id.big_strategy_pic);
        GlideApp.with(mContext)
                .load(BuildConfig.BASE_URL + "/travelstrategy/picture/" + PicPath)
                .placeholder(R.drawable.loading)
                .into(bigPic);
        setCanceledOnTouchOutside(true); // 设置点击屏幕或物理返回键，dialog是否消失
        Window window = getWindow();
        assert window != null;
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        onWindowAttributesChanged(layoutParams);
        bigPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
