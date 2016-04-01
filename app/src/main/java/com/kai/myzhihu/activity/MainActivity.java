package com.kai.myzhihu.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.kai.myzhihu.Constants;
import com.kai.myzhihu.R;
import com.kai.myzhihu.adapter.LeftMenuRcAdapter;
import com.kai.myzhihu.bean.Theme;
import com.kai.myzhihu.bean.Themes;
import com.kai.myzhihu.fragment.FragmentFactory;
import com.kai.myzhihu.fragment.NewsFragment;
import com.kai.myzhihu.http.BaseCallBack;
import com.kai.myzhihu.http.OKHttpHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 凯 on 2016/3/24 21:28
 */
public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawerLayout;
    @ViewInject(R.id.rc_left_menu)
    private RecyclerView rc_left_menu;

    private Gson gson;
    private OKHttpHelper okHttpHelper;
    private ActionBarDrawerToggle mDrawerToggle;
    private LeftMenuRcAdapter leftMenuRcAdapter;
    private Themes themes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        x.view().inject(this);

        gson = new Gson();

        initToolBar();

        initLeftMenu();

        showFragment(NewsFragment.class);
    }

    /**
     * 初始化侧滑菜单
     * create at 2016/3/27 15:17 by 凯
     */
    private void initLeftMenu() {
        rc_left_menu.setLayoutManager(new LinearLayoutManager(this));
        leftMenuRcAdapter = new LeftMenuRcAdapter(MainActivity.this);
        rc_left_menu.setAdapter(leftMenuRcAdapter);

        requestLeftMenuThemesData();

        leftMenuRcAdapter.setOnLeftMenuRcItemClickListener(new LeftMenuRcAdapter.OnLeftMenuRcItemClickListener() {
            @Override
            public void onLeftMenuRcItemClick(List<Theme> others, int position) {
                drawerLayout.closeDrawers();



                //调用NewsFragment中的方法,用来更新这个fragment中recycle的数据
                NewsFragment newsFragment = (NewsFragment) FragmentFactory.getFragment(NewsFragment.class);
                newsFragment.updataRcData(themes, position);
            }
        });
    }

    private void requestLeftMenuThemesData() {
        okHttpHelper = OKHttpHelper.getInstance();
        okHttpHelper.doGet(Constants.URI_THEMES_LIST, new BaseCallBack() {

            @Override
            public void onSuccess(Call call, Response response) {
                super.onSuccess(call, response);
                try {
                    String json = response.body().string();
                    themes = gson.fromJson(json, Themes.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setDataToLeftMenuThemes(themes);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                Snackbar.make(MainActivity.this.findViewById(R.id.app_bar_main), "主题列表加载失败。。。", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setDataToLeftMenuThemes(Themes themes) {
        leftMenuRcAdapter.setThemes(themes);
    }

    private void showFragment(Class<? extends Fragment> clazz) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_main, FragmentFactory.getFragment(clazz));
        fragmentTransaction.commit();
    }


    private void initToolBar() {
        toolbar.inflateMenu(R.menu.activity_home_toolbar);

        toolbar.setTitle("首页");
        setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_mode:
                        Log.e("MainActivity", "onMenuItemClick: mode");
                        break;
                    case R.id.action_setting:
                        Log.e("MainActivity", "onMenuItemClick: setting");

                        break;
                }

                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_toolbar, menu);
        return true;
    }


    public void changeToolBarTitle(String title) {
        toolbar.setTitle(title);
    }
}
