package com.example.travelapplication.activity.register;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.travelapplication.R;
import com.example.travelapplication.activity.base.BaseFormActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.adapter.FavoriteRecycleViewAdapter;
import com.example.travelapplication.adapter.GlideApp;
import com.example.travelapplication.adapter.personalRecycleViewAdapter;
import com.example.travelapplication.infrastructure.utils.DecideErrorUtils;
import com.example.travelapplication.infrastructure.utils.Global_Variable;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.model.Favorite;
import com.example.travelapplication.model.Page;
import com.example.travelapplication.model.TravelStrategy;
import com.example.travelapplication.model.User;
import com.example.travelapplication.pictureselector.FileUtils;
import com.example.travelapplication.pictureselector.PictureBean;
import com.example.travelapplication.pictureselector.PictureSelector;
import com.example.travelapplication.service.business.UserBusinessService;
import com.example.travelapplication.service.business.listener.BaseHandler;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.example.travelapplication.service.valueobject.BaseFilterCondition;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;

public class PersonalCenterActivity extends BaseFormActivity implements LoadingView, BaseView {
    private ImageView userPhoto;
    private TextView userName;
    private TextView email;
    private TextView userNameG;
    private TextView phone;

    private ImageButton personlBtn;
    private ImageButton collectionBtn;
    private ImageButton myStrategyBtn;
    private Button uploadUser;

    private LinearLayout personalLayout;
    private LinearLayout collectionLayout;
    private LinearLayout myStrategyLayout;

    private Dialog loadingDialog;
    private List<User> users = new ArrayList<>();
    private List<TravelStrategy> travelStrategies = new ArrayList<>();
    private List<Favorite> favorites = new ArrayList<>();
    private Integer totalPages;
    private Integer totalPages2;
    private String picUrl;
    private com.example.travelapplication.adapter.personalRecycleViewAdapter personalRecycleViewAdapter;
    private FavoriteRecycleViewAdapter favoriteRecycleViewAdapter;
    private XRecyclerView listContainer;
    private XRecyclerView listContainer2;
    private BaseFilterCondition personalFilterCondition = new BaseFilterCondition();
    private BaseFilterCondition favoriteFilterCondition = new BaseFilterCondition();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        userPhoto = findViewById(R.id.userphoto);
        userName = findViewById(R.id.username);
        email = findViewById(R.id.e_name);
        userNameG = findViewById(R.id.username3);
        phone = findViewById(R.id.mobilephone2);

        personlBtn = findViewById(R.id.personalBtn);
        collectionBtn = findViewById(R.id.mycollect);
        myStrategyBtn = findViewById(R.id.send);
        uploadUser = findViewById(R.id.upload_user);

        personalLayout = findViewById(R.id.personal_layout);
        collectionLayout = findViewById(R.id.collection_layout);
        myStrategyLayout = findViewById(R.id.my_strategy_layout);
        listContainer = findViewById(R.id.my_strategy_list);
        listContainer2 = findViewById(R.id.collection_list);
    }

    private void initData(){
        loadingDialog = createLoadingDialog(getAnchorView().getContext());
        UserBusinessService userBusinessService = new UserBusinessService();
        userBusinessService.getUserInformation(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                User user = (User) object;
                userName.setText(user.getUserName());
                userNameG.setText(user.getUserName());
                email.setText(user.getUserEmail());
                phone.setText(user.getUserTelenumber());
                if (user.getUserPhoto() != null){
                    picUrl = user.getUserPhoto();
                    GlideApp.with(PersonalCenterActivity.this).load(Global_Variable.IP + "/user/userPicture/" + user.getUserPhoto()).into(userPhoto);
                } else {
                    GlideApp.with(PersonalCenterActivity.this).load(R.drawable.loading).into(userPhoto);
                }
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(), object);
            }
        });

        userBusinessService.getMyStrategy(new OnResultListener() {
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
        },personalFilterCondition);

        userBusinessService.getMyFavorite(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                Page<Favorite> favoritePage = (Page<Favorite>) object;
                initListContainerAdapter2(favoritePage);
                totalPages2 = favoritePage.getTotalPages();
                finishLoading();
            }

            @Override
            public void onError(Object object) {
                DecideErrorUtils.showErrorMessage(getAnchorView(),object);
            }
        },favoriteFilterCondition);


    }

    public void refreshData(int key){
        UserBusinessService userBusinessService = new UserBusinessService();
        if (key == 1){
            userBusinessService.getMyStrategy(new OnResultListener() {
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
            },personalFilterCondition);
        } else {
            userBusinessService.getMyFavorite(new OnResultListener() {
                @Override
                public void onSuccess(Object object) {
                    Page<Favorite> favoritePage = (Page<Favorite>) object;
                    initListContainerAdapter2(favoritePage);
                    totalPages2 = favoritePage.getTotalPages();
                    listContainer2.refreshComplete();
                }

                @Override
                public void onError(Object object) {
                    DecideErrorUtils.showErrorMessage(getAnchorView(), object);
                }
            },favoriteFilterCondition);
        }

    }

    private void loadMoreData(int key){
        UserBusinessService userBusinessService = new UserBusinessService();
        if (key == 1){
            userBusinessService.getMyStrategy(new OnResultListener() {
                @Override
                public void onSuccess(Object object) {
                    Page<TravelStrategy> travelStrategyPage = (Page<TravelStrategy>) object;
                    travelStrategies.addAll(travelStrategyPage.getContent());
                    personalRecycleViewAdapter.notifyDataSetChanged();
                    totalPages = travelStrategyPage.getTotalPages();
                    listContainer.loadMoreComplete();

                }

                @Override
                public void onError(Object object) {
                    DecideErrorUtils.showErrorMessage(getAnchorView(), object);
                }
            },personalFilterCondition);
        } else {
            userBusinessService.getMyFavorite(new OnResultListener() {
                @Override
                public void onSuccess(Object object) {
                    Page<Favorite> favoritePage = (Page<Favorite>) object;
                    favorites.addAll(favoritePage.getContent());
                    favoriteRecycleViewAdapter.notifyDataSetChanged();
                    totalPages2 = favoritePage.getTotalPages();
                    listContainer2.loadMoreComplete();
                }

                @Override
                public void onError(Object object) {
                    DecideErrorUtils.showErrorMessage(getAnchorView(), object);
                }
            },favoriteFilterCondition);
        }

    }

    private void setListener(){
        personlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalLayout.setVisibility(View.VISIBLE);
                collectionLayout.setVisibility(View.GONE);
                myStrategyLayout.setVisibility(View.GONE);
            }
        });

        collectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalLayout.setVisibility(View.GONE);
                collectionLayout.setVisibility(View.VISIBLE);
                myStrategyLayout.setVisibility(View.GONE);
            }
        });

        myStrategyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalLayout.setVisibility(View.GONE);
                collectionLayout.setVisibility(View.GONE);
                myStrategyLayout.setVisibility(View.VISIBLE);
            }
        });

        uploadUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog = createLoadingDialog(getAnchorView().getContext());
                UserBusinessService userBusinessService = new UserBusinessService();
                userBusinessService.upData(new BaseHandler(getBaseView(),getLoadingView()) {
                    @Override
                    public void onSuccess(Object object) {
                        SnackBarUtils.showSuccessMessage(uploadUser,"更新资料成功");
                        finishLoading();
                    }

                    @Override
                    public void onError(Object object) {
                        DecideErrorUtils.showErrorMessage(getAnchorView(), object);
                    }
                },userNameG.getText().toString(),email.getText().toString(),phone.getText().toString());
            }
        });

        //加载更多
        listContainer.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                personalFilterCondition.setPageNum(0);
                refreshData(1);
            }

            @Override
            public void onLoadMore() {
                if (personalFilterCondition.getPageNum().equals(totalPages)){
                    listContainer.loadMoreComplete();
                    SnackBarUtils.showWarningMessage(getAnchorView(),"没有更多数据啦");
                    return;
                }
                personalFilterCondition.setPageNum(personalFilterCondition.getPageNum() + 1);
                loadMoreData(1);
            }
        });

        listContainer2.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                favoriteFilterCondition.setPageNum(0);
                refreshData(2);
            }

            @Override
            public void onLoadMore() {
                if (favoriteFilterCondition.getPageNum().equals(totalPages2)){
                    listContainer2.loadMoreComplete();
                    SnackBarUtils.showWarningMessage(getAnchorView(),"没有更多数据啦");
                    return;
                }
                favoriteFilterCondition.setPageNum(favoriteFilterCondition.getPageNum() + 1);
                loadMoreData(2);
            }
        });
    }

    private void initListContainerAdapter(Page<TravelStrategy> travelStrategyPage){
        travelStrategies = travelStrategyPage.getContent();
        personalRecycleViewAdapter = new personalRecycleViewAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.my_strategy_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(personalRecycleViewAdapter);
        personalRecycleViewAdapter.setGridDataList(travelStrategies);
    }

    private void initListContainerAdapter2(Page<Favorite> favoritePage){
        favorites = favoritePage.getContent();
        favoriteRecycleViewAdapter = new FavoriteRecycleViewAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.collection_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(favoriteRecycleViewAdapter);
        favoriteRecycleViewAdapter.setGridDataList(favorites);
    }

    public void selectUserPicture(View view){
        PictureSelector
                .create(PersonalCenterActivity.this, PictureSelector.SELECT_REQUEST_CODE1)
                .selectPicture(true , 100, 100, 1, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE1) {
            if (data != null) {
                String result = PictureSelector.PICTURE_RESULT;
                if (result != null){
                    PictureBean pictureBean = data.getParcelableExtra(result);
                    //使用 Glide 加载图片
                    GlideApp.with(this)
                            .load(pictureBean.isCut() ? pictureBean.getPath() : pictureBean.getUri())
                            .apply(RequestOptions.centerCropTransform()).into(userPhoto);
                    picUrl = pictureBean.getPath();
                    //选完新头像后立即上传服务器
                    UserBusinessService userBusinessService = new UserBusinessService();
                    userBusinessService.addUserPic(new BaseHandler(this,this) {
                        @Override
                        public void onSuccess(Object object) {
                            FileUtils.deleteAllCacheImage(PersonalCenterActivity.this);
                        }

                        @Override
                        protected void on401Error(Response response) {
                            onDefaultError(response);
                        }
                    },picUrl);
                }
            }
        }
    }

    @Override
    public View getAnchorView() {
        return userName;
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

    @Override
    protected void doValidate() {

    }
}
