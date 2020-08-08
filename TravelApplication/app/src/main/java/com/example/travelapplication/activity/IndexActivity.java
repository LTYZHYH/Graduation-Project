package com.example.travelapplication.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.travelapplication.R;
import com.example.travelapplication.activity.base.BaseActivity;
import com.example.travelapplication.activity.register.PersonalCenterActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.adapter.GridAdapter;
import com.example.travelapplication.infrastructure.utils.DecideErrorUtils;
import com.example.travelapplication.model.CityPictureBean;
import com.example.travelapplication.service.business.IndexBusinessService;
import com.example.travelapplication.service.business.listener.BaseHandler;

import java.util.ArrayList;
import java.util.List;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;

public class IndexActivity extends BaseActivity implements BaseView, LoadingView {
    public Dialog loadingDialog;

    private CityPictureBean cityPictureBean;

    private GridAdapter.GridViewHolder gridViewHolder;

    private ImageView indexbg;
    private Button personalBotton;
    private Button foodBtn;

    private List<CityPictureBean> list = new ArrayList<>();
    private ImageView cityPicture;
    private String userName1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initView();
        initData();
        setListener();
    }

    private void initData(){
        userName1 = getIntent().getStringExtra("name");

        setLoadingDialog(this);
        IndexBusinessService indexBusinessService = new IndexBusinessService();
        indexBusinessService.getIndexData(new BaseHandler(this,this) {
            @Override
            public void onSuccess(Object object) {
                List<CityPictureBean> cityPictureBeanList = JSONArray.parseArray(JSONObject.toJSONString(object),CityPictureBean.class);
                initCity(cityPictureBeanList);
                finishLoading();
            }

            @Override
            public void onError(Object object) {
                finishLoading();
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        });

    }

    private void initView(){
        cityPicture = findViewById(R.id.cityPicture);
        indexbg = findViewById(R.id.indexbg);
        personalBotton = findViewById(R.id.personal);
        foodBtn = findViewById(R.id.food_btn);
    }

    private void setListener(){
        personalBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonalCenterActivity.class);
                startActivity(intent);
            }
        });

        foodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FoodActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initCity(List<CityPictureBean> cityPictureBeanList){
        GridAdapter gridAdapter = new GridAdapter(this,userName1);
        RecyclerView recyclerView = findViewById(R.id.rcv_grid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);


        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(gridAdapter);

        gridAdapter.setGridDataList(cityPictureBeanList);
    }


    public void setLoadingDialog(Context context) {
        loadingDialog = createLoadingDialog(context);
    }

    @Override
    public View getAnchorView() {
        return indexbg;
    }

    @Override
    public void finishLoading() {
        closeLoadingDialog(loadingDialog);
    }

    @Override
    public void finishRefresh() {

    }
}
