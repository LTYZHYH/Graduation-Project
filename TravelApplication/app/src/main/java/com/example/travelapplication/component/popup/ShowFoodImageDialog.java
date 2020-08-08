package com.example.travelapplication.component.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.travelapplication.R;
import com.example.travelapplication.adapter.GlideApp;
import com.example.travelapplication.infrastructure.utils.Global_Variable;

public class ShowFoodImageDialog extends Dialog {
    private String foodIntroduction;
    private ImageView bigPic;
    private TextView PicIntroduction;
    private Context mContext;
    private String PicPath;
    private ConstraintLayout BigPicLayout;

    public ShowFoodImageDialog(@NonNull Context context,String foodIntroductions,String picPath) {
        super(context, R.style.ShowImageDialog);
        this.foodIntroduction = foodIntroductions;
        this.mContext = context;
        this.PicPath = picPath;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_picture);
        PicIntroduction = findViewById(R.id.big_food_introduction);
        PicIntroduction.setText(foodIntroduction);
        bigPic = findViewById(R.id.big_food_pic);
        BigPicLayout = findViewById(R.id.big_Pic_Layout);
        GlideApp.with(mContext)
                .load(Global_Variable.IP + "/food/FoodPicture/" + PicPath)
                .placeholder(R.drawable.loading)
                .into(bigPic);
        setCanceledOnTouchOutside(true); // 设置点击屏幕或物理返回键，dialog是否消失
        Window window = getWindow();
        assert window != null;
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = window.getWindowManager().getDefaultDisplay().getHeight();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        onWindowAttributesChanged(layoutParams);
        BigPicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
