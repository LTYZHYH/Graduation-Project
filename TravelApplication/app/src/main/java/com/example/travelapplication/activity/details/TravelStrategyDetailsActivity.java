package com.example.travelapplication.activity.details;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Printer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.example.travelapplication.databinding.ActivityTravelStrategyDetailsBinding;
import com.example.travelapplication.infrastructure.utils.DecideErrorUtils;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.model.TravelStrategy;
import com.example.travelapplication.richtext.utils.RichUtils;
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
    ActivityTravelStrategyDetailsBinding travelStrategyDetailsBinding;
    private Context mContext;
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
        travelStrategyDetailsBinding = ActivityTravelStrategyDetailsBinding.inflate(getLayoutInflater());
        setContentView(travelStrategyDetailsBinding.getRoot());
        mContext = TravelStrategyDetailsActivity.this;
        HeConfig.init("HE2003121843011687", "5ccffd3aaa834e86a52f4faaba4507cd");
        HeConfig.switchToFreeServerNode();
        initView();
        strategyId = getIntent().getIntExtra("strategy_id", 0);
        initData();
        setListener();
    }

    private void initView(){
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
        travelStrategyDetailsBinding.include.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    public WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            return super.shouldOverrideUrlLoading(view, request);
        }
    };

    private void initStrategy(TravelStrategy travelStrategy) {
        cityName = travelStrategy.getArea();
        travelStrategyDetailsBinding.tvStrategyTitle.setText(travelStrategy.getTheme());
        travelStrategyDetailsBinding.tvUsername.setText(travelStrategy.getUser().getUserName());
        travelStrategyDetailsBinding.tvIssueTime.setText(getFormatDatetime(travelStrategy.getIssueTime()));
        String data = travelStrategy.getStrategyContent();
        WebSettings settings = travelStrategyDetailsBinding.webView.getSettings();

        //settings.setUseWideViewPort(true);//调整到适合webview的大小，不过尽量不要用，有些手机有问题
        settings.setLoadWithOverviewMode(true);//设置WebView是否使用预览模式加载界面。
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);

        travelStrategyDetailsBinding.webView.setVerticalScrollBarEnabled(false);//不能垂直滑动
        travelStrategyDetailsBinding.webView.setHorizontalScrollBarEnabled(false);//不能水平滑动
        settings.setTextSize(WebSettings.TextSize.NORMAL);//通过设置WebSettings，改变HTML中文字的大小
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        //设置WebView属性，能够执行Javascript脚本
        travelStrategyDetailsBinding.webView.getSettings().setJavaScriptEnabled(true);//设置js可用

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        travelStrategyDetailsBinding.webView.setWebViewClient(webViewClient);
        travelStrategyDetailsBinding.webView.setWebChromeClient(new WebChromeClient());
        data = "</Div><head><style>body{font-size:16px}</style>" +
                "<style>img{ width:100% !important;margin-top:0.4em;margin-bottom:0.4em}</style>" +
                "<style>ul{ padding-left: 1em;margin-top:0em}</style>" +
                "<style>ol{ padding-left: 1.2em;margin-top:0em}</style>" +
                "</head>" + data;

        ArrayList<String> arrayList = RichUtils.returnImageUrlsFromHtml(data);
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (!arrayList.get(i).contains("http")) {
                    //如果不是http,那么就是本地绝对路径，要加上file
                    data = data.replace(arrayList.get(i), "file://" + arrayList.get(i));
                }
            }
        }

        travelStrategyDetailsBinding.webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
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
        return travelStrategyDetailsBinding.include.imgBack;
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
