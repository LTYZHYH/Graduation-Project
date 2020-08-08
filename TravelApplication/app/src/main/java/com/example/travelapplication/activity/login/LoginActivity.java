package com.example.travelapplication.activity.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.travelapplication.R;
import com.example.travelapplication.activity.IndexActivity;
import com.example.travelapplication.activity.base.BaseFormActivity;
import com.example.travelapplication.activity.register.RegisterActivity;
import com.example.travelapplication.activity.view.BaseView;
import com.example.travelapplication.activity.view.LoadingView;
import com.example.travelapplication.exception.ThrowableException;
import com.example.travelapplication.infrastructure.identity.IdentityContext;
import com.example.travelapplication.infrastructure.utils.JwtUtils;
import com.example.travelapplication.model.User;
import com.example.travelapplication.service.business.LoginBusinessService;
import com.example.travelapplication.service.business.listener.BaseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import retrofit2.Response;

import static com.example.travelapplication.component.popup.LoadingDialog.closeLoadingDialog;
import static com.example.travelapplication.component.popup.LoadingDialog.createLoadingDialog;

public class LoginActivity extends BaseFormActivity implements BaseView, LoadingView {
    private Dialog loadingDialog;
    private TextView loginNameTextView;
    private TextView passwordTextView;
    private Button loginButton;
    private Button registerButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        initView();
        setListener();
    }


    private void initView() {
        loginNameTextView = findViewById(R.id.loginName);
        passwordTextView = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
    }

    private void setListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.super.isValidationPassed(getAnchorView())) {
                    setLoadingDialog(getAnchorView().getContext());
                    LoginBusinessService loginBusinessService = new LoginBusinessService();
                    loginBusinessService.loginByPassword(new BaseHandler(getBaseView(), getLoadingView()) {
                        @Override
                        public void onSuccess(Object object) {
                            onLoginSuccess(object, LoginActivity.this);
                        }

                        @Override
                        protected void on401Error(Response response) {
                            onDefaultError(response);
                        }
                    }, loginNameTextView.getText().toString(), passwordTextView.getText().toString());
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    public void onLoginSuccess(Object object, Context context) {
        JwtUtils.setToken((String) object);
        try {
            IdentityContext.getInstance().setCurrentUser(new ObjectMapper().readValue(JwtUtils.convertPayloadToJsonNode().get("identity").traverse(), User.class));
        } catch (IOException e) {
            throw new ThrowableException(e);
        }
        finishLoading();
        Intent intent = new Intent(context, IndexActivity.class);
        intent.putExtra("name", loginNameTextView.getText().toString());
        startActivity(intent);
        finish();
    }

    public void setLoadingDialog(Context context) {
        loadingDialog = createLoadingDialog(context);
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
    public View getAnchorView() {
        return loginButton;
    }

    @Override
    protected void doValidate() {

    }
}
