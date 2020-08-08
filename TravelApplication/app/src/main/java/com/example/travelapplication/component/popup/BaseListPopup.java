package com.example.travelapplication.component.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.travelapplication.R;
import com.example.travelapplication.component.popup.listener.PopupClickListener;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;


public class BaseListPopup extends BasePopupWindow {

    private ListView mListView;

    private Object chooseData;
    private PopupClickListener mPopupClickListener;

    private BaseListPopup(Context context) {
        super(context);
    }

    public BaseListPopup(Context context, Builder builder) {
        this(context);
        mListView = (ListView) findViewById(R.id.popup_list);
        setAdapter(context, builder);
    }

    public static class Builder {
        private List<Object> mItemEventList = new ArrayList<>();
        private Activity mContext;

        public Builder(Activity context) {
            mContext = context;
        }

        public Builder addItem(String itemTx) {
            mItemEventList.add(itemTx);
            return this;
        }

        public Builder addItem(Object clickTag, String itemTx) {
            mItemEventList.add(new clickItemEvent(clickTag, itemTx));
            return this;
        }

        public List<Object> getItemEventList() {
            return mItemEventList;
        }

        public BaseListPopup build() {
            return new BaseListPopup(mContext, this);
        }

    }

    public static class clickItemEvent {
        private Object clickTag;
        private String itemTx;
        private int color;

        public clickItemEvent(Object clickTag, String itemTx) {
            this.clickTag = clickTag;
            this.itemTx = itemTx;
        }

        public Object getClickTag() {
            return clickTag;
        }

        public void setClickTag(int clickTag) {
            this.clickTag = clickTag;
        }

        public String getItemTx() {
            return itemTx;
        }

        public void setItemTx(String itemTx) {
            this.itemTx = itemTx;
        }
    }

    //=============================================================adapter
    class ListPopupAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private List<Object> mItemList;

        public ListPopupAdapter(Context context, @NonNull List<Object> itemList) {
            mContext = context;
            mItemList = itemList;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public String getItem(int position) {
            if (mItemList.get(position) instanceof String) {
                return (String) mItemList.get(position);
            }
            if (mItemList.get(position) instanceof clickItemEvent) {
                return ((clickItemEvent) mItemList.get(position)).getItemTx();
            }
            return "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_popup_list, parent, false);
                vh.mTextView = (TextView) convertView.findViewById(R.id.item_tx);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.mTextView.setText(getItem(position));
            return convertView;
        }

        public List<Object> getItemList() {
            return this.mItemList;
        }


        class ViewHolder {
            public TextView mTextView;
        }
    }

    //=============================================================init
    private void setAdapter(Context context, Builder builder) {
        if (builder.getItemEventList() == null || builder.getItemEventList().size() == 0) return;
        final ListPopupAdapter adapter = new ListPopupAdapter(context, builder.getItemEventList());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPopupClickListener != null) {
                    Object clickObj = adapter.getItemList().get(position);
                    if (clickObj instanceof String) {
                        mPopupClickListener.click(position);
                        chooseData = clickObj;
                        dismiss();
                    }
                    if (clickObj instanceof clickItemEvent) {
                        Object what = ((clickItemEvent) clickObj).clickTag;
                        mPopupClickListener.click(what);
                        chooseData = what;
                        dismiss();
                    }
                }
            }
        });

    }

    //=============================================================super methods
    @Override
    protected Animation onCreateShowAnimation() {
        return null;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return null;
    }

    @Override
    public Animator onCreateShowAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(mDisplayAnimateView, "rotationX", 90f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(mDisplayAnimateView, "translationY", 250f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(mDisplayAnimateView, "alpha", 0f, 1f).setDuration(400 * 3 / 2));
        return set;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_list_layout);
    }


    @Override
    public Animator onCreateDismissAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(mDisplayAnimateView, "rotationX", 0f, 90f).setDuration(400),
                ObjectAnimator.ofFloat(mDisplayAnimateView, "translationY", 0f, 250f).setDuration(400),
                ObjectAnimator.ofFloat(mDisplayAnimateView, "alpha", 1f, 0f).setDuration(400 * 3 / 2));
        return set;
    }

    //=============================================================interface


    public Object getChooseData() {
        return chooseData;
    }

    public PopupClickListener getOnListPopupItemClickListener() {
        return mPopupClickListener;
    }

    public void setOnListPopupItemClickListener(PopupClickListener popupClickListener) {
        mPopupClickListener = popupClickListener;
    }
}