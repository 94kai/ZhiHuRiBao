package com.kai.myzhihu.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.kai.myzhihu.Constants;
import com.kai.myzhihu.R;
import com.kai.myzhihu.adapter.NewsContentVpAdapter;
import com.kai.myzhihu.bean.NewsContent;
import com.kai.myzhihu.http.BaseCallBack;
import com.kai.myzhihu.http.OKHttpHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 凯 on 2016/3/30 17:24
 */
public class NewsContentActivity extends AppCompatActivity {
    @ViewInject(R.id.vp_news_content)
    private ViewPager vp_news_content;
    private OKHttpHelper okHttpHelper;
    private int position;
    private Gson gson = new Gson();
    private NewsContent newsContent;
    private ArrayList<String> ids;
    private View currentVpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        x.view().inject(this);
        position = getIntent().getIntExtra("position", -1);
        ids = getIntent().getStringArrayListExtra("ids");

        initViewPager();

    }


    private void initViewPager() {
        final NewsContentVpAdapter newsContentVpAdapter = new NewsContentVpAdapter(NewsContentActivity.this, ids);
        vp_news_content.setAdapter(newsContentVpAdapter);
        vp_news_content.setCurrentItem(position);

        newsContentVpAdapter.setCurrentPosition(position);


        newsContentVpAdapter.setOnCurrentPageShowListener(new NewsContentVpAdapter.OnCurrentPageShowListener() {
            @Override
            public void onCurrentPageShow() {
                currentVpView = vp_news_content.findViewById(position);
                requestNewsDataById(ids.get(position), currentVpView);
            }
        });
        vp_news_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentVpView = vp_news_content.findViewById(position);
                requestNewsDataById(ids.get(position), currentVpView);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void requestNewsDataById(String id, final View view) {
        okHttpHelper = OKHttpHelper.getInstance();
        okHttpHelper.doGet(Constants.URI_NEWS_CONTENT + id, new BaseCallBack() {
            @Override
            public void onSuccess(Call call, Response response) {
                super.onSuccess(call, response);
                try {
                    String json = response.body().string();
                    newsContent = gson.fromJson(json, NewsContent.class);
                    

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WebView webView = (WebView) view.findViewById(R.id.wv_news_content);
                            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.dv_news_content);
                            TextView tv_news_content_title = (TextView) view.findViewById(R.id.tv_news_content_title);
                            View rl_news_content_img_root = view.findViewById(R.id.rl_news_content_img_root);
                            TextView tv_news_content_image_source = (TextView) view.findViewById(R.id.tv_news_content_image_source);
                            LinearLayout ll_recommenders= (LinearLayout) view.findViewById(R.id.ll_recommenders);
                            //这里初始化东西
                            initRecommenders(ll_recommenders,newsContent);
                            initImageView(tv_news_content_image_source, rl_news_content_img_root, tv_news_content_title, simpleDraweeView, newsContent);
                            initWebView(webView, newsContent);
                            

                            ((NestedScrollView) view).smoothScrollTo(0, 0);

                            view.setVisibility(View.VISIBLE);

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initRecommenders(LinearLayout ll_auatar, NewsContent newsContent) {

        if(newsContent.getRecommenders()!=null){
            ll_auatar.setVisibility(View.VISIBLE);

            Log.e("NewsContentActivity","initRecommenders: "+newsContent.getRecommenders());
        }else {
            ll_auatar.setVisibility(View.GONE);
        }
    }


    private void initImageView(TextView tv_news_content_image_source, View rl_news_content_img_root, TextView tv_news_content_title, SimpleDraweeView simpleDraweeView, NewsContent newsContent) {
        if (newsContent.getImage() != null) {
            rl_news_content_img_root.setVisibility(View.VISIBLE);
            tv_news_content_title.setText(newsContent.getTitle());
            simpleDraweeView.setImageURI(Uri.parse(newsContent.getImage()));
            tv_news_content_image_source.setText(newsContent.getImage_source());
        } else {
            rl_news_content_img_root.setVisibility(View.GONE);
        }
    }


    private void initWebView(WebView webView, NewsContent newsContent) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + newsContent.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }


}
