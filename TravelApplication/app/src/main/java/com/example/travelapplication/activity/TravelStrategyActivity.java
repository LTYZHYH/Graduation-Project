package com.example.travelapplication.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.request.RequestOptions;
import com.example.travelapplication.BuildConfig;
import com.example.travelapplication.R;
import com.example.travelapplication.activity.base.BaseFormActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.adapter.GlideApp;
import com.example.travelapplication.component.popup.BaseListPopup;
import com.example.travelapplication.component.popup.listener.PopupClickListener;
import com.example.travelapplication.databinding.ActivityTravelStrategyBinding;
import com.example.travelapplication.infrastructure.utils.DecideErrorUtils;
import com.example.travelapplication.infrastructure.utils.PopupUtils;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;
import com.example.travelapplication.model.Area;
import com.example.travelapplication.model.TravelStrategy;
import com.example.travelapplication.model.User;
import com.example.travelapplication.pictureselector.FileUtils;
import com.example.travelapplication.pictureselector.PictureBean;
import com.example.travelapplication.pictureselector.PictureSelector;
import com.example.travelapplication.richtext.utils.KeyBoardUtils;
import com.example.travelapplication.richtext.utils.RichUtils;
import com.example.travelapplication.richtext.utils.popup.CommonPopupWindow;
import com.example.travelapplication.richtext.view.RichEditor;
import com.example.travelapplication.service.business.AreaBusinessService;
import com.example.travelapplication.service.business.FoodBusinessService;
import com.example.travelapplication.service.business.IssueTravelStrategyBusinessService;
import com.example.travelapplication.service.business.UserBusinessService;
import com.example.travelapplication.service.business.listener.BaseHandler;
import com.example.travelapplication.service.business.listener.OnResultListener;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.yalantis.ucrop.UCropActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Response;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;
import static com.yalantis.ucrop.UCrop.EXTRA_OUTPUT_URI;

import androidx.core.app.ActivityCompat;

public class TravelStrategyActivity extends BaseFormActivity implements BaseView, LoadingView {

    public static final String    TAG = "PictureSelector";
    private static final int EDIT_PICTURE_REQUEST_CODE = 11;
    private static final int CHOOSE_PICTURE_REQUEST_CODE = 104;
    ActivityTravelStrategyBinding travelStrategyBinding;
    private Dialog loadingDialog;
    private Context mContext;
    private Date curDate;
    private ArrayList<ImageItem> selectImages = new ArrayList<>();
    private CommonPopupWindow popupWindow; //编辑图片的pop
    private String currentUrl = "";
    private ArrayList<String> needUploadPicList = new ArrayList<>();
    private ArrayList<String> UploadedPicList = new ArrayList<>();
    private int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        travelStrategyBinding = ActivityTravelStrategyBinding.inflate(getLayoutInflater());
        setContentView(travelStrategyBinding.getRoot());
        mContext = this;
        initData();
        initPop();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserBusinessService userBusinessService = new UserBusinessService();
        userBusinessService.getUserInformation(new OnResultListener() {
            @Override
            public void onSuccess(Object object) {
                User user = (User) object;
                travelStrategyBinding.tvUsername.setText(user.getUserName());
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    private void initPop() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.newapp_pop_picture, null);
        view.findViewById(R.id.linear_cancle).setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        view.findViewById(R.id.linear_editor).setOnClickListener(v -> {
            //编辑图片
            if(isGranted()){
                Intent intent = new Intent(mContext, UCropActivity.class);
                intent.putExtra("filePath", currentUrl);
                String destDir = getFilesDir().getAbsolutePath().toString();
                String fileName = "SampleCropImage" + System.currentTimeMillis() + ".jpg";
                intent.putExtra("outPath", destDir + fileName);
                startActivityForResult(intent, EDIT_PICTURE_REQUEST_CODE);
                popupWindow.dismiss();
            } else {
                Toast.makeText(mContext, "相册需要此权限", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.linear_delete_pic).setOnClickListener(v -> {
            //删除图片
            needUploadPicList.remove(currentUrl);
            String removeUrl = "<img src=\"" + currentUrl + "\" alt=\"dachshund\" width=\"100%\"><br>";

            String newUrl = travelStrategyBinding.richEditor.getHtml().replace(removeUrl, "");
            currentUrl = "";
            travelStrategyBinding.richEditor.setHtml(newUrl);
            if (RichUtils.isEmpty(travelStrategyBinding.richEditor.getHtml())) {
                travelStrategyBinding.richEditor.setHtml("");
            }
            popupWindow.dismiss();
        });
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(view)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOutsideTouchable(true)//在外不可用手指取消
                .setAnimationStyle(R.style.pop_animation)//设置popWindow的出场动画
                .create();


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                travelStrategyBinding.richEditor.setInputEnabled(true);
            }
        });
    }

    private void setListener(){
        initRichText();
        travelStrategyBinding.include.imgUpload.setVisibility(View.VISIBLE                                                                                                                                                                                                                                                                             );
        travelStrategyBinding.include.imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TravelStrategyActivity.super.isValidationPassed(getAnchorView())){
                    if (TextUtils.isEmpty(travelStrategyBinding.etTitle.getText()) || TextUtils.isEmpty(travelStrategyBinding.richEditor.getHtml())){
                        SnackBarUtils.showWarningMessage(travelStrategyBinding.include.imgUpload,"请填写必要信息！");
                    } else {
                        //todo 需要先遍历上传文件
                        uploadStrategyPic();
                    }
                }
            }
        });

        travelStrategyBinding.include.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // TODO: 2022/12/9 遍历上传
    private void uploadStrategyPic(){
        int listSize = needUploadPicList.size();
        Log.d(TAG, "uploadStrategyPic: needUploadSize=" + listSize);
        if (flag < listSize && listSize != 0){
            IssueTravelStrategyBusinessService issueTravelStrategyBusinessService = new IssueTravelStrategyBusinessService();
            issueTravelStrategyBusinessService.uploadStrategyPic(new BaseHandler(getBaseView(), getLoadingView()) {
                @Override
                public void onSuccess(Object object) {
                    Log.d(TAG, "onSuccess: path" + object.toString());
                    Log.d(TAG, "onSuccess: flag = " + flag);
                    flag = flag + 1;
                    String picServicePath = BuildConfig.BASE_URL + "/travelstrategy/picture/" + (String) object.toString();
                    UploadedPicList.add(picServicePath);
                    Log.d(TAG, "onSuccess: 已经上传" + flag + "次");
                    uploadStrategyPic();
                }
                @Override
                public void onError(Object object) {
                    super.onError(object);
                }

                @Override
                protected void onDefaultError(Response response) {
                    Log.d(TAG, "onDefaultError: 失败:" + response.toString());
                    super.onDefaultError(response);
                }
            }, needUploadPicList.get(flag));
        } else {
            if (listSize != 0){
                // TODO: 2022/12/14 上传完成后将服务器返回的图片地址替换正文中的文件路径
                convertPicPath();
            }
            IssueTravelStrategyBusinessService issueTravelStrategyBusinessService = new IssueTravelStrategyBusinessService();
            setLoadingDialog(getAnchorView().getContext());
            TravelStrategy travelStrategy = new TravelStrategy();
            travelStrategy.setArea("");
            travelStrategy.setTheme(travelStrategyBinding.etTitle.getText().toString());
            travelStrategy.setStrategyContent(travelStrategyBinding.richEditor.getHtml());
            travelStrategy.setIssueTime(curDate);
            if (!needUploadPicList.isEmpty()){
                travelStrategy.setStrategyPicture1(needUploadPicList.get(0));
            }
            issueTravelStrategyBusinessService.IssueTravelStrategy(new BaseHandler(getBaseView(),getLoadingView()) {
                @Override
                public void onSuccess(Object object) {
                    onIssueTravelStrategy();
                }

                @Override
                protected void on401Error(Response response) {
                    onDefaultError(response);
                }
            },travelStrategy, getIntent().getIntExtra("city_id",10));
        }
    }

    private void convertPicPath(){
        String newHtml = "";
        if (UploadedPicList.size() == 0){
            Log.d(TAG, "convertPicPath: 没有图片");
            return;
        }
        for (int i = 0; i < UploadedPicList.size(); i++) {
            newHtml = travelStrategyBinding.richEditor.getHtml().replace(needUploadPicList.get(i), UploadedPicList.get(i));
            travelStrategyBinding.richEditor.setHtml(newHtml);
        }
        Log.d(TAG, "convertPicPath: " + newHtml);
    }

    private void initRichText(){
        //输入框显示字体的大小
        travelStrategyBinding.richEditor.setEditorFontSize(18);
        //输入框显示字体的颜色
        travelStrategyBinding.richEditor.setEditorFontColor(getResources().getColor(R.color.black));
        //输入框背景设置
        travelStrategyBinding.richEditor.setEditorBackgroundColor(Color.WHITE);
        //输入框文本padding
        travelStrategyBinding.richEditor.setPadding(10, 10, 10, 10);
        //输入提示文本
        travelStrategyBinding.richEditor.setPlaceholder("请开始你的创作！~");

        //文本输入框监听事件
        travelStrategyBinding.richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.e("富文本文字变动", text);
            }
        });

        travelStrategyBinding.richEditor.setOnDecorationChangeListener(new RichEditor.OnDecorationStateListener() {
            @Override
            public void onStateChangeListener(String text, List<RichEditor.Type> types) {
                ArrayList<String> flagArr = new ArrayList<>();
                for (int i = 0; i < types.size(); i++) {
                    flagArr.add(types.get(i).name());
                }

                if (flagArr.contains("BOLD")) {
                    travelStrategyBinding.buttonBold.setImageResource(R.drawable.bold_);
                } else {
                    travelStrategyBinding.buttonBold.setImageResource(R.drawable.bold);
                }

                if (flagArr.contains("UNDERLINE")) {
                    travelStrategyBinding.buttonUnderline.setImageResource(R.drawable.underline_);
                } else {
                    travelStrategyBinding.buttonUnderline.setImageResource(R.drawable.underline);
                }


                if (flagArr.contains("ORDEREDLIST")) {
                    travelStrategyBinding.buttonListUl.setImageResource(R.drawable.list_ul);
                    travelStrategyBinding.buttonListOl.setImageResource(R.drawable.list_ol_);
                } else {
                    travelStrategyBinding.buttonListOl.setImageResource(R.drawable.list_ol);
                }

                if (flagArr.contains("UNORDEREDLIST")) {
                    travelStrategyBinding.buttonListOl.setImageResource(R.drawable.list_ol);
                    travelStrategyBinding.buttonListUl.setImageResource(R.drawable.list_ul_);
                } else {
                    travelStrategyBinding.buttonListUl.setImageResource(R.drawable.list_ul);
                }

            }
        });


        travelStrategyBinding.richEditor.setImageClickListener(new RichEditor.ImageClickListener() {
            @Override
            public void onImageClick(String imageUrl) {
                currentUrl = imageUrl;
                popupWindow.showBottom(travelStrategyBinding.getRoot(), 0.5f);
            }
        });

        travelStrategyBinding.buttonBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                againEdit();
                travelStrategyBinding.richEditor.setBold();
            }
        });

        travelStrategyBinding.buttonRichDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travelStrategyBinding.richEditor.redo();
            }
        });

        travelStrategyBinding.buttonRichUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travelStrategyBinding.richEditor.undo();
            }
        });

        travelStrategyBinding.buttonUnderline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                againEdit();
                travelStrategyBinding.richEditor.setUnderline();
            }
        });

        travelStrategyBinding.buttonListUl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                againEdit();
                travelStrategyBinding.richEditor.setBullets();
            }
        });

        travelStrategyBinding.buttonListOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                againEdit();
                travelStrategyBinding.richEditor.setNumbers();
            }
        });

        travelStrategyBinding.buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(travelStrategyBinding.richEditor.getHtml())) {
                    ArrayList<String> arrayList = RichUtils.returnImageUrlsFromHtml(travelStrategyBinding.richEditor.getHtml());
                    if (arrayList != null && arrayList.size() >= 9) {
                        Toast.makeText(mContext, "最多添加9张照片~", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                if (isGranted()){
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        selectImage(CHOOSE_PICTURE_REQUEST_CODE, selectImages);
                        KeyBoardUtils.closeKeybord(travelStrategyBinding.etTitle, mContext);
                    }
                } else {
                    Toast.makeText(mContext, "相册需要此权限~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData(){
        travelStrategyBinding.tvUsername.setText(getIntent().getStringExtra("user_Name"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        curDate = new Date(System.currentTimeMillis());
        travelStrategyBinding.tvIssueTime.setText(formatter.format(curDate));
    }

    public void onIssueTravelStrategy(){
        //实际开发中将图片上传到服务器成功后需要删除全部缓存图片（即裁剪后的无用图片）
        //FileUtils.deleteAllCacheImage(this);
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
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (requestCode == CHOOSE_PICTURE_REQUEST_CODE) {
                selectImages.clear();
                ArrayList<ImageItem> selects = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                selectImages.addAll(selects);

                againEdit();
                needUploadPicList.add(selectImages.get(0).path);
                travelStrategyBinding.richEditor.insertImage(selectImages.get(0).path, "dachshund");

                KeyBoardUtils.openKeybord(travelStrategyBinding.etTitle, mContext);
                travelStrategyBinding.richEditor.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (travelStrategyBinding.richEditor != null) {
                            travelStrategyBinding.richEditor.scrollToBottom();
                        }
                    }
                }, 200);
            }
        } else if (resultCode == -1) {
            if (requestCode == EDIT_PICTURE_REQUEST_CODE) {
                String outPath = data.getStringExtra(EXTRA_OUTPUT_URI);
                if (!TextUtils.isEmpty(outPath)) {
                    for (int i = 0; i < needUploadPicList.size(); i++) {
                        if (currentUrl.equals(needUploadPicList.get(i))){
                            needUploadPicList.set(i, outPath);
                        }
                    }
                    String newHtml = travelStrategyBinding.richEditor.getHtml().replace(currentUrl, outPath);
                    travelStrategyBinding.richEditor.setHtml(newHtml);
                    currentUrl = "";
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
        return travelStrategyBinding.include.imgUpload;
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

    private void againEdit() {
        //如果第一次点击例如加粗，没有焦点时，获取焦点并弹出软键盘
        travelStrategyBinding.richEditor.focusEditor();
        KeyBoardUtils.openKeybord(travelStrategyBinding.etTitle, mContext);
    }

    public void selectImage(int requestCode, ArrayList<ImageItem> imageItems) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setCrop(false);
        imagePicker.setMultiMode(false);
        imagePicker.setShowCamera(true);
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, imageItems);
        startActivityForResult(intent, requestCode);
    }
}
