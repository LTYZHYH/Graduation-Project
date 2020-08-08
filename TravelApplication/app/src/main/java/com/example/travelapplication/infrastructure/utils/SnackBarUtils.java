package com.example.travelapplication.infrastructure.utils;

import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;


public class SnackBarUtils {
    public static Snackbar createSuccessBar(View view, String message) {
        return createSnackBar(view, message, Color.rgb(114, 213, 114));
    }

    public static Snackbar createWarningBar(View view, String message) {
        return createSnackBar(view, message, Color.rgb(255, 171, 0));
    }

    public static Snackbar createErrorBar(View view, String message) {
        return createSnackBar(view, message, Color.rgb(232, 78, 64));
    }

    public static void showSuccessMessage(View view, String message) {
        createSuccessBar(view, message).show();
    }

    public static void showWarningMessage(View view, String message) {
        createWarningBar(view, message).show();
    }

    public static void showErrorMessage(View view, String message) {
        createErrorBar(view, message).show();
    }

    private static Snackbar createSnackBar(View view, String message, int backGroundColor) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(backGroundColor);
        return snackbar;
    }
}
