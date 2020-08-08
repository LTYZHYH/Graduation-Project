package com.example.travelapplication.activity.details;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SearchView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.travelapplication.R;
import com.example.travelapplication.activity.TravelStrategyActivity;
import com.example.travelapplication.activity.base.BaseActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.adapter.GridAdapter;
import com.example.travelapplication.adapter.LoopDetailsBackgroundAdapter;
import com.example.travelapplication.adapter.TravelStrategyRecycleViewAdapter;
import com.example.travelapplication.infrastructure.utils.DecideErrorUtils;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.model.TravelStrategy;
import com.example.travelapplication.service.business.TravelStrategyBusinessService;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.valueobject.BaseFilterCondition;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;

public class cityDetailsActivity extends BaseActivity implements LoadingView, BaseView {
    private Integer cityId;
    private String userName1;
    private String sort;

    private XRecyclerView listContainer;
    private Dialog loadingDialog;
    private List<TravelStrategy> travelStrategies = new ArrayList<>();
    private GridAdapter.GridViewHolder gridViewHolder;
    private TravelStrategyRecycleViewAdapter travelStrategyRecycleViewAdapter;
    private BaseFilterCondition travelStrategyFilterCondition = new BaseFilterCondition();
    private Integer totalPages;

    private ImageButton issueTravelStrategy;
    private SearchView searchTravelStrategy;
    private LinearLayout searchParentLayout;
    private RadioGroup radioGroup;

    private ViewPager viewPager;  //轮播图模块
    private int[] mImg;
    private int[] mImg_id;
    private String[] mDec;
    private ArrayList<ImageView> mImgList;
    private LinearLayout ll_dots_container;
    private int previousSelectedPosition = 0;//之前选择的位置
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);
        initView();
        initData();
        setListener();
        initLoopView();  //实现轮播图
        setFocus();
    }

    private void initView(){
        listContainer = findViewById(R.id.listContainer);
        issueTravelStrategy = findViewById(R.id.issue_ts);
        searchTravelStrategy = findViewById(R.id.search_ts);
        searchParentLayout = findViewById(R.id.search_ts_parent);
        radioGroup = findViewById(R.id.select_radio_group);
    }

    private void initData(){
        cityId = getIntent().getIntExtra("city_id",0);
        userName1 = getIntent().getStringExtra("user_Name");
        sort = "strategyId,desc";

        loadingDialog = createLoadingDialog(getAnchorView().getContext());
        TravelStrategyBusinessService travelStrategyBusinessService = new TravelStrategyBusinessService();
        travelStrategyBusinessService.getTravelStrategyByCityId(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<TravelStrategy> travelStrategyPage = (Page<TravelStrategy>) object;
                initListContainerAdapter(travelStrategyPage);
                totalPages = travelStrategyPage.getTotalPages();
                finishLoading();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        }, travelStrategyFilterCondition,cityId,sort);

        //1:回车
        //2:前往
        //3:搜索
        //4:发送
        //5:下一項
        //6:完成
        //设置输入法搜索选项字段，默认是搜索，可以是：下一页、发送、完成等
        searchTravelStrategy.setImeOptions(6);
        searchTravelStrategy.setIconifiedByDefault(false);
        setUnderLinetransparent(searchTravelStrategy);
    }

    private void setFocus(){
        searchTravelStrategy.clearFocus();
    }

    /**设置SearchView下划线透明**/
    private void setUnderLinetransparent(SearchView searchTravelStrategy) {
        try{
            Class<?> argClass = searchTravelStrategy.getClass();
            // searchParentLayout是searchTravelStrategy父布局的名字
            Field ownField = argClass.getDeclaredField("searchParentLayout");
            ownField.setAccessible(true);
            View view = (View) ownField.get(searchTravelStrategy);
            view.setBackgroundColor(Color.TRANSPARENT);
        } catch (NoSuchFieldException e){
            e.printStackTrace();
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    private void loadMoreData(){
        TravelStrategyBusinessService travelStrategyBusinessService = new TravelStrategyBusinessService();
        travelStrategyBusinessService.getTravelStrategyByCityId(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<TravelStrategy> travelStrategyPage = (Page<TravelStrategy>) object;
                travelStrategies.addAll(travelStrategyPage.getContent());
                travelStrategyRecycleViewAdapter.notifyDataSetChanged();
                totalPages = travelStrategyPage.getTotalPages();
                listContainer.loadMoreComplete();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },travelStrategyFilterCondition,cityId,sort);
    }

    private void refreshData(){
        TravelStrategyBusinessService travelStrategyBusinessService = new TravelStrategyBusinessService();
        travelStrategyBusinessService.getTravelStrategyByCityId(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<TravelStrategy> travelStrategyPage = (Page<TravelStrategy>) object;
                initListContainerAdapter(travelStrategyPage);
                totalPages = travelStrategyPage.getTotalPages();
                listContainer.refreshComplete();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },travelStrategyFilterCondition,cityId,sort);
    }

    private void SearchTravelStrategy(String like){
        TravelStrategyBusinessService travelStrategyBusinessService = new TravelStrategyBusinessService();
        travelStrategyBusinessService.searchTravelStrategyByLike(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<TravelStrategy> travelStrategyPage = (Page<TravelStrategy>) object;
                initListContainerAdapter(travelStrategyPage);
                totalPages = travelStrategyPage.getTotalPages();
                listContainer.refreshComplete();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },travelStrategyFilterCondition,cityId,like,sort);
    }

    private void initLoopView() {
        viewPager = findViewById(R.id.loopDetailsBackground);
//        ll_dots_container = findViewById(R.id.ll_dots_loop);

        // 图片资源id数组
        mImg = new int[]{
                R.drawable.panda,
                R.drawable.huoguo,
                R.drawable.jiuzaigou,


        };

        // 文本描述
//        mDec = new String[]{
//                "熊猫",
//                "火锅",
//                "九寨沟",
//        };

        mImg_id = new int[]{
                R.id.pager_img1,
                R.id.pager_img2,
                R.id.pager_img3,

        };

        // 初始化要展示的ImageView
        mImgList = new ArrayList<>();
        ImageView imageView;
//        View dotView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < mImg.length; i++) {
            //初始化要显示的图片对象
            imageView = new ImageView(this);
            imageView.setBackgroundResource(mImg[i]);
            imageView.setId(mImg_id[i]);
//            imageView.setOnClickListener(new pagerOnClickListener(getApplicationContext()));

            mImgList.add(imageView);
            //加引导点
//            dotView = new View(this);
//            dotView.setBackgroundResource(R.drawable.dot);
            layoutParams = new LinearLayout.LayoutParams(10, 10);
            if (i != 0) {
                layoutParams.leftMargin = 10;
            }
            //设置默认所有都不可用
//            dotView.setEnabled(false);
//            ll_dots_container.addView(dotView, layoutParams);
        }


//        ll_dots_container.getChildAt(0).setEnabled(true);
        previousSelectedPosition = 0;
        //设置适配器
        viewPager.setAdapter(new LoopDetailsBackgroundAdapter(cityDetailsActivity.this,mImgList));
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImgList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        viewPager.setCurrentItem(currentPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int newPosition = i % mImgList.size();
//                ll_dots_container.getChildAt(previousSelectedPosition).setEnabled(false);
//                ll_dots_container.getChildAt(newPosition).setEnabled(true);
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

    private void setListener(){
        issueTravelStrategy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cityDetailsActivity.this, TravelStrategyActivity.class);
                intent.putExtra("user_Name", userName1);
                intent.putExtra("city_id", cityId);
                startActivity(intent);
            }
        });

        //加载更多
        listContainer.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                travelStrategyFilterCondition.setPageNum(0);
                refreshData();
            }

            @Override
            public void onLoadMore() {
                if (travelStrategyFilterCondition.getPageNum().equals(totalPages)){
                    listContainer.loadMoreComplete();
                    SnackBarUtils.showWarningMessage(getAnchorView(),"没有更多数据啦");
                    return;
                }
                travelStrategyFilterCondition.setPageNum(travelStrategyFilterCondition.getPageNum() + 1);
                loadMoreData();
            }
        });

        // 设置搜索文本监听
        searchTravelStrategy.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null){
                    refreshData();
                }else {
                    SearchTravelStrategy(query);
                }
                return false;
            }
            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.less_time:
                        sort = "travelDays,asc";
                        refreshData();
                        break;
                    case R.id.less_line:
                        sort = "scenicNumber,asc";
                        refreshData();
                        break;
                    case R.id.synthesize:
                        sort = "strategyId,desc";
                        refreshData();
                        break;
                }
            }
        });
    }

    private void initListContainerAdapter(Page<TravelStrategy> travelStrategyPage){
        travelStrategies = travelStrategyPage.getContent();
        travelStrategyRecycleViewAdapter = new TravelStrategyRecycleViewAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.listContainer);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);


        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(travelStrategyRecycleViewAdapter);

        travelStrategyRecycleViewAdapter.setGridDataList(travelStrategies);
    }

    @Override
    public View getAnchorView() {
        return issueTravelStrategy;
    }

    @Override
    public void finishLoading() {
        closeLoadingDialog(loadingDialog);
    }

    @Override
    public void finishRefresh() {

    }
    @Override
    protected void onResume() {
        super.onResume();
        searchParentLayout.setFocusable(true);
        searchParentLayout.setFocusableInTouchMode(true);
        searchParentLayout.requestFocus();
    }
}
