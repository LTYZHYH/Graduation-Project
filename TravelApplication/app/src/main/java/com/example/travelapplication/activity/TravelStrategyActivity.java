package com.example.travelapplication.activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.request.RequestOptions;
import com.example.travelapplication.R;
import com.example.travelapplication.activity.base.BaseFormActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.adapter.GlideApp;
import com.example.travelapplication.component.popup.BaseListPopup;
import com.example.travelapplication.component.popup.listener.PopupClickListener;
import com.example.travelapplication.infrastructure.utils.DecideErrorUtils;
import com.example.travelapplication.infrastructure.utils.PopupUtils;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.model.Area;
import com.example.travelapplication.pictureselector.FileUtils;
import com.example.travelapplication.pictureselector.PictureBean;
import com.example.travelapplication.pictureselector.PictureSelector;
import com.example.travelapplication.service.business.AreaBusinessService;
import com.example.travelapplication.service.business.FoodBusinessService;
import com.example.travelapplication.service.business.IssueTravelStrategyBusinessService;
import com.example.travelapplication.service.business.listener.BaseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Response;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;

public class TravelStrategyActivity extends BaseFormActivity implements BaseView, LoadingView {

    public static final String    TAG = "PictureSelector";
    private BaseListPopup areaListPopup;
    private Area area;

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView foodPhoto;

    private TextView themeText;
    private TextView areaText;
    private TextView costText;
    private TextView travelDaysText;
    private TextView userName;
    private TextView scenicNumberText;
    private TextView issueTime;
    private TextView strategyContent;
    private ImageButton upload;
    private ImageView addFoodBtn;
    private TextView foodNameText;
    private TextView foodAreaText;
    private TextView foodIntroduction;
    private Button issueFoodBtn;

    private Dialog loadingDialog;

    private String imgPath1;
    private String imgPath2;
    private String imgPath3;
    private String foodPicpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_strategy);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        img1 = findViewById(R.id.iv_image);
        img2 = findViewById(R.id.iv_image2);
        img3 = findViewById(R.id.iv_image3);
        foodPhoto = findViewById(R.id.food_Pic);

        themeText = findViewById(R.id.edit_theme);
        areaText = findViewById(R.id.edit_area);
        costText = findViewById(R.id.edit_overheadCost);
        travelDaysText = findViewById(R.id.edit_travelDays);
        userName = findViewById(R.id.edit_issueUser);
        scenicNumberText = findViewById(R.id.text_scenic);
        issueTime = findViewById(R.id.edit_issueTime);
        strategyContent = findViewById(R.id.share_travel_strategy);
        upload = findViewById(R.id.upload);
        addFoodBtn = findViewById(R.id.add_food_btn);
    }

    private void showFoodPopupWindow(){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_food_popup,null,false);
        final PopupWindow popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        foodPhoto = view.findViewById(R.id.food_Pic);
        foodNameText = view.findViewById(R.id.food_name);
        foodAreaText = view.findViewById(R.id.food_area);
        foodIntroduction = view.findViewById(R.id.food_introduction);
        issueFoodBtn = view.findViewById(R.id.issue_food);
        foodAreaText.setText(areaText.getText().toString());
        issueFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(foodNameText.getText()) || TextUtils.isEmpty(foodAreaText.getText()) || TextUtils.isEmpty(foodIntroduction.getText()) || foodPicpath == null){
                    SnackBarUtils.showWarningMessage(upload,"请填写必要信息！");
                } else {
                    setLoadingDialog(issueFoodBtn.getContext());
                    FoodBusinessService foodBusinessService = new FoodBusinessService();
                    foodBusinessService.addFood(new BaseHandler(getBaseView(),getLoadingView()) {
                        @Override
                        public void onSuccess(Object object) {
                            finishLoading();
                            popupWindow.dismiss();
                        }

                        @Override
                        public void onError(Object object) {
                            DecideErrorUtils.showErrorMessage(getAnchorView(), object);
                        }
                    },foodNameText.getText().toString(),foodIntroduction.getText().toString(),foodAreaText.getText().toString(),foodPicpath);
                }
            }
        });
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//调节透明度
        getWindow().setAttributes(lp);
        //dismiss时恢复原样
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        View parentView = LayoutInflater.from(this).inflate(R.layout.add_food_popup,null);
        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0,0);
    }


    private void setListener(){
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TravelStrategyActivity.super.isValidationPassed(getAnchorView())){
                    if (TextUtils.isEmpty(themeText.getText()) || TextUtils.isEmpty(areaText.getText()) || TextUtils.isEmpty(costText.getText())
                            || TextUtils.isEmpty(travelDaysText.getText()) || TextUtils.isEmpty(scenicNumberText.getText()) || TextUtils.isEmpty(issueTime.getText())
                            || TextUtils.isEmpty(strategyContent.getText())){
                        SnackBarUtils.showWarningMessage(upload,"请填写必要信息！");
                    } else {
                        setLoadingDialog(getAnchorView().getContext());
                        IssueTravelStrategyBusinessService issueTravelStrategyBusinessService = new IssueTravelStrategyBusinessService();
                        issueTravelStrategyBusinessService.IssueTravelStrategy(new BaseHandler(getBaseView(),getLoadingView()) {
                            @Override
                            public void onSuccess(Object object) {
                               onIssueTravelStrategy();
                            }

                            @Override
                            protected void on401Error(Response response) {
                               onDefaultError(response);
                            }
                        },themeText.getText().toString(),areaText.getText().toString(),costText.getText().toString(),getIntent().getIntExtra("city_id",10),
                                travelDaysText.getText().toString(),scenicNumberText.getText().toString(),issueTime.getText().toString(),
                                strategyContent.getText().toString(),
                                imgPath1,imgPath2,imgPath3);
                    }
                }
            }
        });

        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFoodPopupWindow();
            }
        });

        setChooseAreaListener(areaText);
    }

    private void initData(){
        userName.setText(getIntent().getStringExtra("user_Name"));
        SimpleDateFormat   formatter   =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        issueTime.setText(formatter.format(curDate));
        initArea(areaText);
    }

    private void initArea(final TextView chooseAreaView){
        loadingDialog = createLoadingDialog(getAnchorView().getContext());
        AreaBusinessService areaBusinessService = new AreaBusinessService();
        areaBusinessService.getAreaByCityId(new BaseHandler(this,this) {
            @Override
            public void onSuccess(Object object) {
                List<Area> areaList = JSONArray.parseArray(JSONObject.toJSONString(object), Area.class);
                addAreaSetToAreaListPopup(areaList,chooseAreaView);
                if (areaList.size() > 0){
                    area = areaList.get(0);
                    chooseAreaView.setText(area.getAreaName());
                }

                finishLoading();
            }

            @Override
            public void onError(Object object) {
                finishLoading();
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        }, getIntent().getIntExtra("city_id",10));
    }

    public void setChooseAreaListener(final View chooseAreaListener){
        chooseAreaListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areaListPopup == null){
                    chooseAreaListener.setEnabled(false);
                } else {
                    areaListPopup.showPopupWindow();
                }
            }
        });
    }

    private void addAreaSetToAreaListPopup(final List<Area> areas, final TextView chooseAreaView){
        PopupClickListener popupClickListener = new PopupClickListener() {
            @Override
            public void click(Object object) {
                if (object instanceof Area){
                    area = (Area) object;
                    chooseAreaView.setText(area.getAreaName());
                }
            }
        };
        BaseListPopup.Builder builder = new BaseListPopup.Builder(this);
        for (Area area : areas){
            builder.addItem(area,area.getAreaName());
        }
        areaListPopup = PopupUtils.getListPopup(this, builder, popupClickListener);
    }

    public void onIssueTravelStrategy(){
        //实际开发中将图片上传到服务器成功后需要删除全部缓存图片（即裁剪后的无用图片）
        FileUtils.deleteAllCacheImage(this);
        finishLoading();
        finish();
    }

    /**
     * 选择图片按钮点击事件
     *
     * @param view
     */
    public void selectPicture1(View view) {
        /**
         * create()方法参数一是上下文，在activity中传activity.this，在fragment中传fragment.this。参数二为请求码，用于结果回调onActivityResult中判断
         * selectPicture()方法参数分别为 是否裁剪、裁剪后图片的宽(单位px)、裁剪后图片的高、宽比例、高比例。都不传则默认为裁剪，宽200，高200，宽高比例为1：1。
         */
        PictureSelector
                .create(TravelStrategyActivity.this, PictureSelector.SELECT_REQUEST_CODE1)
                .selectPicture(false , 200, 200, 1, 1);
    }

    public void selectPicture2(View view){
        PictureSelector
                .create(TravelStrategyActivity.this, PictureSelector.SELECT_REQUEST_CODE2)
                .selectPicture(false , 200, 200, 1, 1);
    }

    public void selectPicture3(View view){
        PictureSelector
                .create(TravelStrategyActivity.this, PictureSelector.SELECT_REQUEST_CODE3)
                .selectPicture(false , 200, 200, 1, 1);
    }

    public void selectPicture4(View view){
        PictureSelector
                .create(TravelStrategyActivity.this, PictureSelector.SELECT_REQUEST_CODE4)
                .selectPicture(false , 200, 200, 1, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE1) {
            if (data != null) {
                String result = PictureSelector.PICTURE_RESULT;
                if (result != null){
                    PictureBean pictureBean = data.getParcelableExtra(result);

                    Log.i(TAG, "是否裁剪: " + pictureBean.isCut());
                    Log.i(TAG, "原图地址: " + pictureBean.getPath());
                    Log.i(TAG, "图片 Uri: " + pictureBean.getUri());
//                if (pictureBean.isCut()) {
//                    img1.setImageBitmap(BitmapFactory.decodeFile(pictureBean.getPath()));
//                } else {
//                    img1.setImageURI(pictureBean.getUri());
//                }

                    //使用 Glide 加载图片
                    GlideApp.with(this)
                            .load(pictureBean.isCut() ? pictureBean.getPath() : pictureBean.getUri())
                            .apply(RequestOptions.centerCropTransform()).into(img1);

                    imgPath1 = pictureBean.getPath();
                }
            }
        }

        if (requestCode == PictureSelector.SELECT_REQUEST_CODE2){
            if (data != null){
                String result = PictureSelector.PICTURE_RESULT;
                if (result != null){
                    PictureBean pictureBean = data.getParcelableExtra(result);

                    GlideApp.with(this)
                            .load(pictureBean.isCut() ? pictureBean.getPath() : pictureBean.getUri())
                            .apply(RequestOptions.centerCropTransform()).into(img2);

                    imgPath2 = pictureBean.getPath();
                }
            }
        }

        if (requestCode == PictureSelector.SELECT_REQUEST_CODE3){
            if (data != null){
                String result = PictureSelector.PICTURE_RESULT;
                if (result != null){
                    PictureBean pictureBean = data.getParcelableExtra(result);

                    GlideApp.with(this)
                            .load(pictureBean.isCut() ? pictureBean.getPath() : pictureBean.getUri())
                            .apply(RequestOptions.centerCropTransform()).into(img3);

                    imgPath3 = pictureBean.getPath();
                }
            }
        }

        if (requestCode == PictureSelector.SELECT_REQUEST_CODE4){
            if (data != null){
                String result = PictureSelector.PICTURE_RESULT;
                if (result != null){
                    PictureBean pictureBean = data.getParcelableExtra(result);

                    GlideApp.with(this)
                            .load(pictureBean.isCut() ? pictureBean.getPath() : pictureBean.getUri())
                            .apply(RequestOptions.centerCropTransform()).into(foodPhoto);

                    foodPicpath = pictureBean.getPath();
                }
            }
        }
    }


    public void setLoadingDialog(Context context) {
        loadingDialog = createLoadingDialog(context);
    }

    @Override
    protected void doValidate() {

    }

    @Override
    public View getAnchorView() {
        return upload;
    }

    @Override
    public void finishLoading() {
        closeLoadingDialog(loadingDialog);
    }

    @Override
    public void finishRefresh() {

    }

    private BaseView getBaseView() {
        return this;
    }

    private LoadingView getLoadingView() {
        return this;
    }
}
