package com.example.travelapplication.component.popup;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.travelapplication.R;

public class LoadingDialog {
    public static Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout dialogViewRootLayout = dialogView.findViewById(R.id.dialog_loading_view);// 加载布局
        Dialog loadingDialog = new Dialog(context, R.style.loadingDialog);
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(dialogViewRootLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);
        loadingDialog.show();
        return loadingDialog;
    }

    public static void closeLoadingDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            // TODO: 2018/12/12 临时处理，平板横竖屏切换时，由于activity销毁，无法找到dismiss的位置
            try {
                dialog.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static Dialog createLoadingDialogWindow(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout dialogViewRootLayout = dialogView.findViewById(R.id.dialog_loading_view);// 加载布局
        Dialog loadingDialog = new Dialog(context, R.style.loadingDialog);
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(dialogViewRootLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);
        return loadingDialog;
    }
}
