package com.example.travelapplication.activity.base;

import android.view.View;

import com.example.travelapplication.exception.ValidateFailedException;
import com.example.travelapplication.infrastructure.utils.SnackBarUtils;

public abstract class BaseFormActivity extends BaseActivity{
    protected boolean isValidationPassed(View view) {
        boolean isValidationPassed = true;
        try {
            doValidate();
        } catch (ValidateFailedException e) {
            SnackBarUtils.showErrorMessage(view, e.getMessage());
            isValidationPassed = false;
        }
        return isValidationPassed;
    }

    protected abstract void doValidate();
}
