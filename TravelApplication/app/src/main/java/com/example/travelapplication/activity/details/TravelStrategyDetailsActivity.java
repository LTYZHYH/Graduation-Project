package com.example.travelapplication.activity.details;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.R;
import com.example.travelapplication.activity.base.BaseActivity;
import com.example.travelapplication.activity.comment.CommentStrategyActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.adapter.GlideApp;
import com.example.travelapplication.adapter.LoopDetailsBackgroundAdapter;
import com.example.travelapplication.adapter.WeatherRecyclerViewAdapter;
import com.example.travelapplication.infrastructure.utils.DecideErrorUtils;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.model.TravelStrategy;
import com.example.travelapplication.service.business.ReportBusinessService;
import com.example.travelapplication.service.business.TravelStrategyBusinessService;
import com.example.travelapplication.service.business.listener.BaseHandler;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.business.listener.pagerOnClickListener;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import retrofit2.Response;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;
import static com.example.travelapplication.infrastructure.utils.TimeFormatUtils.getFormatDatetime;

public class TravelStrategyDetailsActivity extends BaseActivity implements LoadingView, BaseView {
    private final static String TAG = "result:";
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;

    private TextView themeText;
    private TextView areaText;
    private TextView costText;
    private TextView travelDaysText;
    private TextView userName;
    private TextView scenicNumberText;
    private TextView issueTime;
    private TextView strategyContent;
    private ImageButton commentBtn;
    private ImageButton favoriteBtn;
    private ImageButton reportBtn;
    private RadioGroup radioGroup;
    private Button issueReportBtn;
    private ViewPager viewPager;  //轮播图模块
    private int[] mImg_id;
    private ArrayList<ImageView> mImgList;
    private int previousSelectedPosition;//之前选择的位置

    private Dialog loadingDialog;
    private Integer strategyId;
    private String cityName;
    private String reportReson;

    boolean isRunning = false;
    //以下是天气控件
    private ImageView openWeather;
    private ImageView closeWeather;
    private ConstraintLayout weatherDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_strategy_details);
        HeConfig.init("HE2003121843011687", "5ccffd3aaa834e86a52f4faaba4507cd");
        HeConfig.switchToFreeServerNode();
        initView();
        strategyId = getIntent().getIntExtra("strategy_id", 0);
        initData();
        setListener();
        initLoopView();
    }

    private void initView(){
        img1 = findViewById(R.id.strategy_p1);
        img2 = findViewById(R.id.strategy_p2);
        img3 = findViewById(R.id.strategy_p3);

        themeText = findViewById(R.id.theme_text);
        areaText = findViewById(R.id.area_text);
        costText = findViewById(R.id.cost_text);
        travelDaysText = findViewById(R.id.travel_days_text);
        userName = findViewById(R.id.issueUser_text);
        scenicNumberText = findViewById(R.id.scenic_text);
        issueTime = findViewById(R.id.issueTime_text);
        strategyContent = findViewById(R.id.travel_content_text);
        commentBtn = findViewById(R.id.commentBtn);
        favoriteBtn = findViewById(R.id.star);
        reportBtn = findViewById(R.id.reportStr);

        openWeather = findViewById(R.id.open_weather);
        closeWeather = findViewById(R.id.close_weather);
        weatherDetail = findViewById(R.id.weather_detail);
    }

    private void initData(){
        loadingDialog = createLoadingDialog(getAnchorView().getContext());
        TravelStrategyBusinessService travelStrategyBusinessService = new TravelStrategyBusinessService();
        travelStrategyBusinessService.getStrategyDatail(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                TravelStrategy travelStrategy = (TravelStrategy) object;
                initStrategy(travelStrategy);
                finishLoading();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },strategyId);
    }

    private void initWeatherDate(){
        HeWeather.getWeatherForecast(TravelStrategyDetailsActivity.this, cityName, Lang.CHINESE_SIMPLIFIED, Unit.METRIC,
                new HeWeather.OnResultWeatherForecastBeanListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        DecideErrorUtils.showErrorMessage(getAnchorView(), throwable);
                    }

                    @Override
                    public void onSuccess(Forecast forecast) {
                        if ( Code.OK.getCode().equalsIgnoreCase(forecast.getStatus()) ){
                            //此时返回数据
                            List<ForecastBase> forecastBases = forecast.getDaily_forecast();
                            initWeather(forecastBases);
                        } else {
                            //在此查看返回数据失败的原因
                            String status = forecast.getStatus();
                            Code code = Code.toEnum(status);
                            Log.i(TAG, "failed code: " + code);
                        }
                    }
                });
    }

    private void setListener(){
        openWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initWeatherDate();
                weatherDetail.setVisibility(View.VISIBLE);
                openWeather.setVisibility(View.GONE);
                closeWeather.setVisibility(View.VISIBLE);
            }
        });

        closeWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherDetail.setVisibility(View.GONE);
                closeWeather.setVisibility(View.GONE);
                openWeather.setVisibility(View.VISIBLE);
            }
        });

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TravelStrategyDetailsActivity.this, CommentStrategyActivity.class);
                intent.putExtra("strategy_id", strategyId);
                startActivity(intent);
            }
        });

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TravelStrategyBusinessService travelStrategyBusinessService = new TravelStrategyBusinessService();
                travelStrategyBusinessService.addFavoriteStrategy(new BaseHandler(getBaseView(),getLoadingView()) {
                    @Override
                    public void onSuccess(Object object) {
                        SnackBarUtils.showSuccessMessage(v,"收藏成功");
                        finishLoading();
                    }
                    @Override
                    protected void on401Error(Response response) {
                        onDefaultError(response);
                    }
                },strategyId);
            }
        });

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportPopupWindow();
            }
        });
    }

    private void initWeather(List<ForecastBase> forecastBaseList){
        WeatherRecyclerViewAdapter weatherRecyclerViewAdapter = new WeatherRecyclerViewAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.weather_scroll_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(weatherRecyclerViewAdapter);
        weatherRecyclerViewAdapter.setGridDataList(forecastBaseList);
    }

    private void initStrategy(TravelStrategy travelStrategy) {
        GlideApp.with(this).load(BuildConfig.BASE_URL + "/travelstrategy/picture/" + travelStrategy.getStrategyPicture1()).placeholder(R.drawable.loading).into(img1);
        GlideApp.with(this).load(BuildConfig.BASE_URL + "/travelstrategy/picture/" + travelStrategy.getStrategyPicture2()).placeholder(R.drawable.loading).into(img2);
        GlideApp.with(this).load(BuildConfig.BASE_URL + "/travelstrategy/picture/" + travelStrategy.getStrategyPicture3()).placeholder(R.drawable.loading).into(img3);
        cityName = travelStrategy.getArea();
        themeText.setText(travelStrategy.getTheme());
        areaText.setText(travelStrategy.getArea());
        costText.setText(travelStrategy.getOverheadCost());
        travelDaysText.setText(travelStrategy.getTravelDays());
        userName.setText(travelStrategy.getUser().getUserName());
        scenicNumberText.setText(travelStrategy.getScenicNumber());
        issueTime.setText(getFormatDatetime(travelStrategy.getIssueTime()));
        strategyContent.setText(travelStrategy.getStrategyContent());
    }

    private void initLoopView() {
        viewPager = findViewById(R.id.loop_strategy_detail);
        mImg_id = new int[]{
                R.id.strategy_p1,
                R.id.strategy_p2,
                R.id.strategy_p3,
        };
        // 初始化要展示的ImageView
        mImgList = new ArrayList<>();
        ImageView imageView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < 3; i++) {
            //初始化要显示的图片对象
            if (i == 0){
                imageView = img1;
            } else if (i == 1){
                imageView = img2;
            } else {
                imageView = img3;
            }
            imageView.setId(mImg_id[i]);

            mImgList.add(imageView);
            layoutParams = new LinearLayout.LayoutParams(10, 10);
            if (i != 0) {
                layoutParams.leftMargin = 10;
            }
        }


        previousSelectedPosition = 0;
        //设置适配器
        viewPager.setAdapter(new LoopDetailsBackgroundAdapter(TravelStrategyDetailsActivity.this,mImgList,true,strategyId));
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImgList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        viewPager.setCurrentItem(currentPosition);
        viewPager.setOnClickListener(new pagerOnClickListener(getApplicationContext()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                previousSelectedPosition = i;
            }

            @Override
            public void onPageSelected(int i) {
                int newPosition = i % mImgList.size();
                previousSelectedPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        // 开启轮询
        new Thread(){
            public void run(){
                isRunning = true;
                while(isRunning){
                    try{
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //下一条
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });
                }
            }
        }.start();
    }

    private void showReportPopupWindow(){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.report_strategy_reason,null,false);
        final PopupWindow popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        reportReson = "攻略没有营养";
        radioGroup = view.findViewById(R.id.report_reason_radio_group);
        issueReportBtn = view.findViewById(R.id.issue_report);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb1:
                        reportReson = "攻略没有营养";
                        break;
                    case R.id.rb2:
                        reportReson = "攻击政府、反动";
                        break;
                    case R.id.rb3:
                        reportReson = "色情";
                        break;
                    case R.id.rb4:
                        reportReson = "标题与内容不符";
                        break;
                    case R.id.rb5:
                        reportReson = "抄袭";
                        break;
                }
            }
        });
        issueReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                setLoadingDialog(issueReportBtn.getContext());
                ReportBusinessService reportBusinessService = new ReportBusinessService();
                reportBusinessService.reportStrategy(new BaseHandler(getBaseView(),getLoadingView()) {
                    @Override
                    public void onSuccess(Object object) {
                        finishLoading();
                        popupWindow.dismiss();
                    }

                    @Override
                    public void on401Error(Response response) {
                        onDefaultError(response);
                    }
                },strategyId,reportReson);
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
        View parentView = LayoutInflater.from(this).inflate(R.layout.report_strategy_reason,null);
        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0,0);

    }

    private void setLoadingDialog(Context context) {
        loadingDialog = createLoadingDialog(context);
    }

    @Override
    public View getAnchorView() {
        return themeText;
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
