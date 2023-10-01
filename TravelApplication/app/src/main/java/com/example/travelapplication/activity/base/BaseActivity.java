package com.example.travelapplication.activity.base;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.PermissionUtils;

public abstract class BaseActivity extends AppCompatActivity{
    PermissionUtils permissionUtils;
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置自动旋转屏幕ea

        permissionUtils = PermissionUtils.permission(permissions);
        permissionUtils.request();
    }

    protected boolean isGranted(){
        return PermissionUtils.isGranted(permissions);
    }
}
