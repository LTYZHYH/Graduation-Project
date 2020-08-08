package com.example.travelapplication.activity.register;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.travelapplication.R;
import com.example.travelapplication.activity.base.BaseFormActivity;
import com.example.travelapplication.activity.login.LoginActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.service.business.RegisterBusinessService;
import com.example.travelapplication.service.business.listener.BaseHandler;

import retrofit2.Response;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;

public class RegisterActivity extends BaseFormActivity implements BaseView, LoadingView {
    private TextView emailTextView;
    private Dialog loadingDialog;
    private TextView passwordTextView;
    private TextView nickNameTextView;
    private TextView telephoneTextView;
    private TextView photo;
    private Button registerButton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        initView();
        setListener();
    }

    private void initView(){
        emailTextView = findViewById(R.id.EmailAddress);
        passwordTextView = findViewById(R.id.textPassword);
        nickNameTextView = findViewById(R.id.nickname);
        telephoneTextView = findViewById(R.id.telephone);
        registerButton = findViewById(R.id.register2);

    }

    private void setListener(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegisterActivity.super.isValidationPassed(getAnchorView())){
                    setLoadingDialog(getAnchorView().getContext());
                    RegisterBusinessService registerBusinessService = new RegisterBusinessService();
                    registerBusinessService.registerNewUser(new BaseHandler(getBaseView(),getLoadingView()) {
                        @Override
                        public void onSuccess(Object object) {
                            onRegisterSuccess(RegisterActivity.this);
                        }
                        @Override
                        protected void on401Error(Response response) {
                            onDefaultError(response);
                        }
                    },emailTextView.getText().toString(),nickNameTextView.getText().toString(),telephoneTextView.getText().toString(),passwordTextView.getText().toString());
                }
            }
        });
    }

    public void onRegisterSuccess(Context context){
        finishLoading();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void setLoadingDialog(Context context) {
        loadingDialog = createLoadingDialog(context);
    }

    @Override
    protected void doValidate() {

    }

    @Override
    public View getAnchorView() {
        return registerButton;
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
