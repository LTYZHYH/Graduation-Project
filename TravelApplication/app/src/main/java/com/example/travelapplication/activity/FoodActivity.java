package com.example.travelapplication.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.travelapplication.R;
import com.example.travelapplication.activity.base.BaseActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.adapter.WaterfallAdapter;
import com.example.travelapplication.infrastructure.utils.DecideErrorUtils;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.model.Food;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.service.business.FoodBusinessService;
import com.example.travelapplication.service.business.listener.BaseHandler;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.valueobject.BaseFilterCondition;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;

public class FoodActivity extends BaseActivity implements BaseView, LoadingView {
    private SearchView searchAreaName;
    private SearchView searchFoodName;
    private Dialog loadingDialog;
    private List<Food> foodList = new ArrayList<>();
    private WaterfallAdapter waterfallAdapter;
    private BaseFilterCondition foodFilterCondition = new BaseFilterCondition();
    private XRecyclerView listContainer;
    private LinearLayout searchParentLayout;
    private LinearLayout changeFoodSrFocus;
    private Integer totalPages;
    private TextView srAreaN;
    private TextView srFoodN;
    private ImageButton searchF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        initView();
        initData();
//        setFocus();
        setListener();
    }

    private void initView(){
        srAreaN = findViewById(R.id.sr_areaN);
        srFoodN = findViewById(R.id.sr_foodN);
        searchF = findViewById(R.id.searchF);
        listContainer = findViewById(R.id.food_recycler_view);
        searchParentLayout= findViewById(R.id.food_recycler_view_parent);
        changeFoodSrFocus = findViewById(R.id.change_foodSr_focus);
    }

    private void initData(){
        loadingDialog = createLoadingDialog(getAnchorView().getContext());
        FoodBusinessService foodBusinessService = new FoodBusinessService();
        foodBusinessService.getFood(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<Food> foodPage = (Page<Food>) object;
                initListContainerAdapter(foodPage);
                totalPages = foodPage.getTotalPages();
                finishLoading();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },foodFilterCondition);

//        searchAreaName.setImeOptions(6);
//        searchFoodName.setImeOptions(6);
//        searchAreaName.setIconifiedByDefault(false);
//        searchFoodName.setIconifiedByDefault(false);
//        setUnderLinetransparent(searchAreaName);
//        setUnderLinetransparent(searchFoodName);
    }

//    /**设置SearchView下划线透明**/
//    private void setUnderLinetransparent(SearchView searchTravelStrategy) {
//        try{
//            Class<?> argClass = searchTravelStrategy.getClass();
//            // searchParentLayout是searchTravelStrategy父布局的名字
//            Field ownField = argClass.getDeclaredField("searchParentLayout");
//            ownField.setAccessible(true);
//            View view = (View) ownField.get(searchTravelStrategy);
//            view.setBackgroundColor(Color.TRANSPARENT);
//        } catch (NoSuchFieldException e){
//            e.printStackTrace();
//        } catch (IllegalAccessException e){
//            e.printStackTrace();
//        }
//    }

//    private void setFocus(){
//        searchAreaName.clearFocus();
//        searchFoodName.clearFocus();
//    }

    private void refreshData(){
        FoodBusinessService foodBusinessService = new FoodBusinessService();
        foodBusinessService.getFood(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<Food> foodPage = (Page<Food>) object;
                initListContainerAdapter(foodPage);
                totalPages = foodPage.getTotalPages();
                listContainer.refreshComplete();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },foodFilterCondition);
    }

    private void loadMoreData(){
        FoodBusinessService foodBusinessService = new FoodBusinessService();
        foodBusinessService.getFood(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<Food> foodPage = (Page<Food>) object;
                foodList.addAll(foodPage.getContent());
                waterfallAdapter.notifyDataSetChanged();
                totalPages = foodPage.getTotalPages();
                listContainer.loadMoreComplete();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },foodFilterCondition);
    }

    private void setListener(){
        listContainer.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                foodFilterCondition.setPageNum(0);
                refreshData();
            }

            @Override
            public void onLoadMore() {
                if (foodFilterCondition.getPageNum().equals(totalPages)){
                    listContainer.loadMoreComplete();
                    SnackBarUtils.showWarningMessage(getAnchorView(),"没有更多数据啦");
                    return;
                }
                foodFilterCondition.setPageNum(foodFilterCondition.getPageNum() + 1);
                loadMoreData();
            }
        });

        searchF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog = createLoadingDialog(getAnchorView().getContext());
                FoodBusinessService foodBusinessService = new FoodBusinessService();
                foodBusinessService.searchFood(new BaseHandler(getBaseView(),getLoadingView()) {
                    @Override
                    public void onSuccess(Object object) {
                        Page<Food> foodPage = (Page<Food>) object;
                        initListContainerAdapter(foodPage);
                        totalPages = foodPage.getTotalPages();
                        listContainer.refreshComplete();
                        finishLoading();
                    }

                    @Override
                    public void onError(Object object) {
                        DecideErrorUtils.showErrorMessage(getAnchorView(),object);
                    }
                },foodFilterCondition,srFoodN.getText().toString(),srAreaN.getText().toString());
            }
        });

//        searchFoodName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (query == null){
//                    refreshData();
//                } else {
//                    loadingDialog = createLoadingDialog(searchFoodName.getContext());
//                    FoodBusinessService foodBusinessService = new FoodBusinessService();
//                    foodBusinessService.searchFoodByName(new OnResultListener() {
//                        @Override
//                        public void onSuccess(Object object) {
//                            Page<Food> foodPage = (Page<Food>) object;
//                            initListContainerAdapter(foodPage);
//                            totalPages = foodPage.getTotalPages();
//                            listContainer.refreshComplete();
//                            finishLoading();
//                        }
//
//                        @Override
//                        public void onError(Object object) {
//                            DecideErrorUtils.showErrorMessage(getAnchorView(), object);
//                        }
//                    },foodFilterCondition,query);
//                }
//                setFocus();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        searchAreaName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (query == null){
//                    refreshData();
//                } else {
//                    loadingDialog = createLoadingDialog(searchAreaName.getContext());
//                    FoodBusinessService foodBusinessService = new FoodBusinessService();
//                    foodBusinessService.searchFoodByArea(new OnResultListener() {
//                        @Override
//                        public void onSuccess(Object object) {
//                            Page<Food> foodPage = (Page<Food>) object;
//                            initListContainerAdapter(foodPage);
//                            totalPages = foodPage.getTotalPages();
//                            listContainer.refreshComplete();
//                            finishLoading();
//                        }
//
//                        @Override
//                        public void onError(Object object) {
//                            DecideErrorUtils.showErrorMessage(getAnchorView(), object);
//                        }
//                    },foodFilterCondition,query);
//                }
//                setFocus();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
    }

    private void initListContainerAdapter(Page<Food> foodPage){
        foodList = foodPage.getContent();
        waterfallAdapter = new WaterfallAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.food_recycler_view);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(waterfallAdapter);
        waterfallAdapter.setWaterfallData(foodList);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        changeFoodSrFocus.setFocusable(true);
//        changeFoodSrFocus.setFocusableInTouchMode(true);
//        changeFoodSrFocus.requestFocus();
//    }

    @Override
    public View getAnchorView() {
        return searchF;
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
