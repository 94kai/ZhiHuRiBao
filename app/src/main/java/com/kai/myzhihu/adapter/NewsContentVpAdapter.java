package com.kai.myzhihu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kai.myzhihu.Constants;
import com.kai.myzhihu.R;
import com.kai.myzhihu.activity.NewsContentActivity;
import com.kai.myzhihu.bean.NewsContent;
import com.kai.myzhihu.http.BaseCallBack;
import com.kai.myzhihu.http.OKHttpHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 凯 on 2016/3/30 21:31
 */
public class NewsContentVpAdapter extends PagerAdapter {
    private List<String> ids;
    private Context context;
    private List<View> views;
    private View currentView;
    private int currentPosition = -1;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public NewsContentVpAdapter(Context context, List<String> ids) {
        this.context = context;
        this.ids = ids;
        views = new ArrayList<View>();
    }

    @Override
    public int getCount() {
        return ids.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        if (views != null && views.size() > 0) {
            view = views.get(0);
            views.remove(0);
        } else {
            view = View.inflate(context, R.layout.item_vp_news_content, null);
        }
        container.addView(view);
        view.setId(position);

        if (currentPosition == position) {
            if (onCurrentPageShowListener != null) {
                onCurrentPageShowListener.onCurrentPageShow();
            }
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((View) object).setVisibility(View.INVISIBLE);
        views.add((View) object);

        container.removeView((View) object);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    private OnCurrentPageShowListener onCurrentPageShowListener;

    public interface OnCurrentPageShowListener {
        public void onCurrentPageShow();
    }

    public void setOnCurrentPageShowListener(OnCurrentPageShowListener onCurrentPageShowListener) {
        this.onCurrentPageShowListener = onCurrentPageShowListener;
    }
}


