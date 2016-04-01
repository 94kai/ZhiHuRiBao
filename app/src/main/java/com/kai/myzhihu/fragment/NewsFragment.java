package com.kai.myzhihu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.kai.myzhihu.Constants;
import com.kai.myzhihu.activity.MainActivity;
import com.kai.myzhihu.R;
import com.kai.myzhihu.activity.NewsContentActivity;
import com.kai.myzhihu.adapter.NewsRcAdapter;
import com.kai.myzhihu.bean.BeforeNews;
import com.kai.myzhihu.bean.LatestNews;
import com.kai.myzhihu.bean.Storie;
import com.kai.myzhihu.bean.ThemeContent;
import com.kai.myzhihu.bean.Themes;
import com.kai.myzhihu.http.BaseCallBack;
import com.kai.myzhihu.http.OKHttpHelper;
import com.kai.myzhihu.http.SpotsCallBack;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 凯 on 2016/3/25 16:15
 */
public class NewsFragment extends Fragment {
    private RecyclerView rc_home;
    private MaterialRefreshLayout refresh;


    private Gson gson = new Gson();
    private OKHttpHelper oKHttpHelper;
    private NewsRcAdapter rcAdapter;
    private LatestNews latesNews;
    private BeforeNews beforeNews;
    private long currentThemeId = -1;//-1代表是首页
    private int beforeDates = 0;
    private Calendar calendar;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    private ThemeContent themeContent;
    private BeforeNews themeBeforeNews;
    private List<String> ids;

    private final int STORIE_TYPE_NORMAL=0;
    private final int STORIE_TYPE_TOP=1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        rc_home = (RecyclerView) view.findViewById(R.id.rc_home);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        initRc();

        initRefresh();

        requestLatestNewsData(true);
        return view;
    }

    /**
     * 获取几天前的时间 用 年月日表示
     *
     * @return 年月日
     * create at 2016/3/25 23:23 by 凯
     */
    private String getBeforeDay(int day) {
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 0 - day);
        return sf.format(calendar.getTime());
    }


    private void initRefresh() {
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                if (currentThemeId == -1) {
                    requestLatestNewsData(false);
                    beforeDates = 0;
                } else {
                    requestThemeData(currentThemeId);
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (currentThemeId == -1) {
                    requestBeforeNewsData();
                } else {
                    if (rcAdapter != null) {
                        requestBeforeThemeData(currentThemeId, rcAdapter.getLastNewsId());
                    }
                }
            }
        });
    }


    private void initRc() {
        ids=new ArrayList<String>();
        rc_home.setLayoutManager(new LinearLayoutManager(getContext()));
        rcAdapter = new NewsRcAdapter(getContext());
        rc_home.setAdapter(rcAdapter);
        rcAdapter.setOnItemClickListener(new NewsRcAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Storie storie,int position) {
                Log.e("NewsFragment","onItemClick: "+storie.getId());// TODO: 2016/3/31 10:52 删除
                ids = rcAdapter.getStorieIds(ids, STORIE_TYPE_NORMAL);
                Intent intent=new Intent(getMActivity(), NewsContentActivity.class);

                intent.putStringArrayListExtra("ids", (ArrayList<String>) ids);
                intent.putExtra("position",position);
                // TODO: 2016/3/31 9:25 传递一个当前请求的内容的id的集合
                getMActivity().startActivity(intent);

            }
        });
        rcAdapter.setOnSliderLayoutItemClickListener(new NewsRcAdapter.OnSliderLayoutItemClickListener() {
            @Override
            public void onSliderLayoutItemClick(int id,int topPosition) {

                Log.e("NewsFragment","onSliderLayoutItemClick: "+id); // TODO: 2016/3/31 10:52 删除
                ids = rcAdapter.getStorieIds(ids, STORIE_TYPE_TOP);
                Intent intent=new Intent(getMActivity(), NewsContentActivity.class);

                intent.putStringArrayListExtra("ids", (ArrayList<String>) ids);
                intent.putExtra("position",topPosition);
                // TODO: 2016/3/31 9:25 传递一个当前请求的内容的id的集合
                getMActivity().startActivity(intent);

            }
        });

        rc_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (currentThemeId == -1) {
                    int position = rc_home.getChildAdapterPosition(rc_home.getChildAt(0));
                    position--;
                    String smartTitleFromDate = rcAdapter.getSmartTitleFromDate(position);
                    if (smartTitleFromDate != null) {
                        if (smartTitleFromDate.equals("今日热闻")) {
                            getMActivity().changeToolBarTitle("首页");
                        } else {
                            getMActivity().changeToolBarTitle(smartTitleFromDate);
                        }
                    }
                }

            }
        });
    }

    /**
     * 请求最新的新闻数据（首页数据）
     *
     * @return create at 2016/3/25 10:32
     * @author 凯
     */
    private void requestLatestNewsData(boolean hasSpots) {
        oKHttpHelper = OKHttpHelper.getInstance();
        if (hasSpots) {
            oKHttpHelper.doGet(Constants.URI_LATEST_NEWS, new SpotsCallBack(getContext(), "正在加载中。。。") {
                @Override
                public void onFailure(Call call, IOException e) {
                    super.onFailure(call, e);
                    Snackbar.make(getView(), "数据加载失败。。。", Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(Call call, Response response) {
                    super.onSuccess(call, response);
                    try {
                        String json = response.body().string();
                        latesNews = gson.fromJson(json, LatestNews.class);
                        if(getActivity()!=null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showLatestNews(latesNews);
                                }
                            });
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            oKHttpHelper.doGet(Constants.URI_LATEST_NEWS, new BaseCallBack() {
                @Override
                public void onError(Call call, Response response, int errorCode) {
                    super.onError(call, response, errorCode);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (refresh != null) {
                                refresh.finishRefresh();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    super.onFailure(call, e);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (refresh != null) {
                                refresh.finishRefresh();
                            }
                        }
                    });
                    Snackbar.make(getView(), "数据加载失败。。。", Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(Call call, Response response) {
                    super.onSuccess(call, response);
                    try {
                        String json = response.body().string();
                        latesNews = gson.fromJson(json, LatestNews.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (refresh != null) {
                                    refresh.finishRefresh();
                                }
                                showLatestNews(latesNews);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    }

    private void showLatestNews(LatestNews latesNews) {
        rcAdapter.setLatestNews(latesNews);
        rcAdapter.notifyDataSetChanged();

    }


    private void requestBeforeNewsData() {
        oKHttpHelper = OKHttpHelper.getInstance();
        oKHttpHelper.doGet(Constants.URI_BEFORE_NEWS + getBeforeDay(beforeDates), new BaseCallBack() {
            @Override
            public void onError(Call call, Response response, int errorCode) {
                super.onError(call, response, errorCode);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (refresh != null) {
                            refresh.finishRefreshLoadMore();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                Log.e("MainActivity", "onFailure ");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (refresh != null) {
                            refresh.finishRefreshLoadMore();
                        }
                    }
                });
                Snackbar.make(getView(), "数据加载失败。。。", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Call call, Response response) {
                super.onSuccess(call, response);
                try {
                    String json = response.body().string();
                    beforeNews = gson.fromJson(json, BeforeNews.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (refresh != null) {
                                refresh.finishRefreshLoadMore();
                            }
                            beforeDates++;
                            showBeforeHomeNews(beforeNews);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showBeforeHomeNews(BeforeNews beforeNews) {
        if (rcAdapter != null) {
            int size = rcAdapter.getStories().size();
            rcAdapter.setBeforeNews(beforeNews);
            rcAdapter.notifyItemRangeInserted(size + 1, beforeNews.getStories().size());
            rc_home.scrollToPosition(size + 1);
        }
    }


    private void requestThemeData(long themeId) {
        oKHttpHelper = OKHttpHelper.getInstance();
        oKHttpHelper.doGet(Constants.URI_THEME_LIST + themeId, new BaseCallBack() {
            @Override
            public void onError(Call call, Response response, int errorCode) {
                super.onError(call, response, errorCode);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (refresh != null) {
                            refresh.finishRefresh();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (refresh != null) {
                            refresh.finishRefresh();
                        }
                        Snackbar.make(getView(), "数据加载失败。。。", Snackbar.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onSuccess(Call call, Response response) {
                super.onSuccess(call, response);
                try {
                    String json = response.body().string();
                    themeContent = gson.fromJson(json, ThemeContent.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (refresh != null) {
                                refresh.finishRefresh();
                            }
                            showThemeList(themeContent);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 显示theme的内容
     *
     * @return create at 2016/3/27 17:47 by 凯
     */
    private void showThemeList(ThemeContent themeContent) {
        rcAdapter.setThemeContent(themeContent);
    }

    private void requestBeforeThemeData(long currentThemeId, long lastNewsId) {
        oKHttpHelper.doGet(Constants.URI_THEME_LIST + currentThemeId + "/before/" + lastNewsId, new BaseCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (refresh != null) {
                            refresh.finishRefreshLoadMore();
                        }
                        Snackbar.make(getView(), "数据加载失败。。。", Snackbar.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onSuccess(Call call, Response response) {
                super.onSuccess(call, response);
                try {
                    String json = response.body().string();
                    themeBeforeNews = gson.fromJson(json, BeforeNews.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (refresh != null) {
                            refresh.finishRefreshLoadMore();
                        }

                        showBeforeThemeList(themeBeforeNews);
                    }
                });
            }

            @Override
            public void onError(Call call, Response response, int errorCode) {
                super.onError(call, response, errorCode);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (refresh != null) {
                            refresh.finishRefreshLoadMore();
                        }
                    }
                });
            }
        });

    }

    private void showBeforeThemeList(BeforeNews themeBeforeNews) {
        if (rcAdapter != null) {
            int size = rcAdapter.getStories().size();
            rcAdapter.setBeforeNews(themeBeforeNews);
            rcAdapter.notifyItemRangeInserted(size + 1, beforeNews.getStories().size());
            rc_home.scrollToPosition(size + 1);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beforeDates = 0;

    }

    /**
     * 如果position=0，将rc中的数据设置为首页 否则从themes（position-1）中取东西
     * create at 2016/3/27 17:33 by 凯
     */
    public void updataRcData(Themes themes, int position) {
        if (getActivity() != null) {
            if (position == 0) {
                getMActivity().changeToolBarTitle("首页");
            } else {
                getMActivity().changeToolBarTitle(themes.getOthers().get(position - 1).getName());
            }
        }


        if (rcAdapter != null) {
            rcAdapter.clearStories();
        }
        if (position == 0) {
            currentThemeId = -1;
            requestLatestNewsData(false);
        } else {
            position--;
            currentThemeId = themes.getOthers().get(position).getId();
            requestThemeData(themes.getOthers().get(position).getId());
        }
    }

    private MainActivity getMActivity() {
        return (MainActivity) getActivity();
    }
}
