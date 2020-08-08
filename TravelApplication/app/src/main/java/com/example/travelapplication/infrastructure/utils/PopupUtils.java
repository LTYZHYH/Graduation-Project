package com.example.travelapplication.infrastructure.utils;

import android.content.Context;

import com.example.travelapplication.component.popup.BaseListPopup;
import com.example.travelapplication.component.popup.listener.PopupClickListener;
import com.example.travelapplication.service.valueobject.SelectionClearance;

public class PopupUtils {

    public static BaseListPopup getListPopup(Context context, BaseListPopup.Builder builder, PopupClickListener popupClickListener) {
        return getListPopup(context, builder, popupClickListener, true);
    }

    public static BaseListPopup getListPopup(Context context, BaseListPopup.Builder builder, PopupClickListener popupClickListener, boolean forceSelect) {
        if (!forceSelect){
            builder.getItemEventList().add(0, new BaseListPopup.clickItemEvent(new SelectionClearance(), "清除选择"));
        }

        BaseListPopup baseListPopup = new BaseListPopup(context, builder);
        baseListPopup.setOnListPopupItemClickListener(popupClickListener);
        return baseListPopup;
    }
}
