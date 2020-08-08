package com.example.travelapplication.activity.comment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapplication.R;
import com.example.travelapplication.activity.base.BaseActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.adapter.ReplyRecycleViewAdapter;
import com.example.travelapplication.infrastructure.utils.DecideErrorUtils;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.model.Commity;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.model.Reply;
import com.example.travelapplication.service.business.ReplyBusinessService;
import com.example.travelapplication.service.business.listener.BaseHandler;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.valueobject.BaseFilterCondition;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;

public class ReplyActivity extends BaseActivity implements LoadingView, BaseView {
    private TextView commentText;
    private TextView replyEdit;
    private Button replyBtn;
    private Integer totalPages;
    private Dialog loadingDialog;
    private Integer commentId;

    private XRecyclerView listContainer;
    private ReplyRecycleViewAdapter replyRecycleViewAdapter;
    private BaseFilterCondition replyFilterCondition = new BaseFilterCondition();
    private List<Reply> replies = new ArrayList<>();
    ReplyBusinessService replyBusinessService = new ReplyBusinessService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_reply);
        initView();
        initData();
        setListener();

    }

    private void initView(){
        commentText = findViewById(R.id.comment_content);
        replyEdit = findViewById(R.id.reply);
        replyBtn = findViewById(R.id.reply_btn);
        listContainer = findViewById(R.id.reply_list);

    }

    private void initData(){
        commentId = getIntent().getIntExtra("commentId", 0);

        loadingDialog = createLoadingDialog(getAnchorView().getContext());
        replyBusinessService.getReply(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<Reply> replyPage = (Page<Reply>) object;
                initListContainerAdapter(replyPage);
                totalPages = replyPage.getTotalPages();
                finishLoading();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },commentId,replyFilterCondition);

        replyBusinessService.getOneComment(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Commity commity = (Commity) object;
                commentText.setText(commity.getCommityContent());
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },commentId);
    }

    private void setListener(){
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyBusinessService.sendReply(new BaseHandler(getBaseView(),getLoadingView()) {
                    @Override
                    public void onSuccess(Object object) {
                        replyFilterCondition.setPageNum(0);
                        refreshData();
                    }

                    @Override
                    public void onError(Object object) {
                        super.onError(object);
                    }
                },commentId,replyEdit.getText().toString());
            }
        });

        //加载更多
        listContainer.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                replyFilterCondition.setPageNum(0);
                refreshData();
            }

            @Override
            public void onLoadMore() {
                if (replyFilterCondition.getPageNum().equals(totalPages)){
                    listContainer.loadMoreComplete();
                    SnackBarUtils.showWarningMessage(getAnchorView(),"没有更多数据啦");
                    return;
                }
                replyFilterCondition.setPageNum(replyFilterCondition.getPageNum() + 1);
                loadMoreData();
            }
        });
    }

    private void refreshData(){
        replyBusinessService.getReply(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<Reply> replyPage = (Page<Reply>) object;
                initListContainerAdapter(replyPage);
                totalPages = replyPage.getTotalPages();
                listContainer.refreshComplete();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        },commentId,replyFilterCondition);
    }

    private void loadMoreData(){
        replyBusinessService.getReply(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<Reply> replyPage = (Page<Reply>) object;
                replies.addAll(replyPage.getContent());
                replyRecycleViewAdapter.notifyDataSetChanged();
                totalPages = replyPage.getTotalPages();
                listContainer.loadMoreComplete();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        }, commentId,replyFilterCondition);
    }

    private void initListContainerAdapter(Page<Reply> replyPage){
        replies = replyPage.getContent();
        replyRecycleViewAdapter = new ReplyRecycleViewAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.reply_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);


        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(replyRecycleViewAdapter);

        replyRecycleViewAdapter.setGridDataList(replies);
    }

    @Override
    public View getAnchorView() {
        return replyBtn;
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
