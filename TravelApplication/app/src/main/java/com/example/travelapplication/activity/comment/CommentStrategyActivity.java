package com.example.travelapplication.activity.comment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapplication.R;
import com.example.travelapplication.activity.base.BaseActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.adapter.CommityRecycleViewAdapter;
import com.example.travelapplication.infrastructure.utils.DecideErrorUtils;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.model.Commity;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.service.business.CommentBusinessService;
import com.example.travelapplication.service.business.listener.BaseHandler;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.valueobject.BaseFilterCondition;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;

public class CommentStrategyActivity extends BaseActivity implements LoadingView, BaseView {
    private TextView commentText;
    private Button commentBtn;
    private Dialog loadingDialog;
    private Integer totalPages;
    private Integer strategyId;

    private List<Commity> commities = new ArrayList<>();
    private CommityRecycleViewAdapter commityRecycleViewAdapter;
    private BaseFilterCondition commityFilterCondition = new BaseFilterCondition();
    private XRecyclerView listContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //软键盘会覆盖在屏幕上面，而不会把布局顶上去
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_comment_strategy);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        commentBtn = findViewById(R.id.on_comment);
        commentText = findViewById(R.id.comment_TS);
        listContainer = findViewById(R.id.comment_list);
    }

    private void initData(){
        strategyId = getIntent().getIntExtra("strategy_id",0);

        loadingDialog = createLoadingDialog(getAnchorView().getContext());
        CommentBusinessService commentBusinessService = new CommentBusinessService();
        commentBusinessService.getComment(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<Commity> commityPage = (Page<Commity>) object;
                initListContainerAdapter(commityPage);
                totalPages = commityPage.getTotalPages();
                finishLoading();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },commityFilterCondition,strategyId);
    }

    private void refreshData(){
        CommentBusinessService commentBusinessService = new CommentBusinessService();
        commentBusinessService.getComment(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<Commity> commityPage = (Page<Commity>) object;
                initListContainerAdapter(commityPage);
                totalPages = commityPage.getTotalPages();
                listContainer.refreshComplete();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },commityFilterCondition,strategyId);
    }

    private void loadMoreData(){
        CommentBusinessService commentBusinessService = new CommentBusinessService();
        commentBusinessService.getComment(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<Commity> commityPage = (Page<Commity>) object;
                commities.addAll(commityPage.getContent());
                commityRecycleViewAdapter.notifyDataSetChanged();
                totalPages = commityPage.getTotalPages();
                listContainer.loadMoreComplete();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },commityFilterCondition,strategyId);
    }

    private void setListener(){
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentBusinessService commentBusinessService = new CommentBusinessService();
                commentBusinessService.issueComment(new BaseHandler(getBaseView(),getLoadingView()) {
                    @Override
                    public void onSuccess(Object object) {
                        commityFilterCondition.setPageNum(0);
                        refreshData();
                    }

                    @Override
                    public void on401Error(Response response) {
                        onDefaultError(response);
                    }
                },commentText.getText().toString(),strategyId);
            }
        });

        //加载更多
        listContainer.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                commityFilterCondition.setPageNum(0);
                refreshData();
            }

            @Override
            public void onLoadMore() {
                if (commityFilterCondition.getPageNum().equals(totalPages)){
                    listContainer.loadMoreComplete();
                    SnackBarUtils.showWarningMessage(getAnchorView(),"没有更多数据啦");
                    return;
                }
                commityFilterCondition.setPageNum(commityFilterCondition.getPageNum() + 1);
                loadMoreData();
            }
        });
    }

    private void initListContainerAdapter(Page<Commity> commityPage){
        commities = commityPage.getContent();
        commityRecycleViewAdapter = new CommityRecycleViewAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.comment_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);


        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(commityRecycleViewAdapter);

        commityRecycleViewAdapter.setGridDataList(commities);
    }

    @Override
    public View getAnchorView() {
        return commentBtn;
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
