package com.kai.myzhihu.bean;

import java.util.List;

/**
 * Created by 凯 on 2016/3/25 10:17
 */
public class LatestNews {
    private String date;
    private List<Storie> stories;
    private List<TopStorie >top_stories;

    public LatestNews(String date, List<Storie> stories) {
        this.date = date;
        this.stories = stories;
    }

    public List<TopStorie> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStorie> top_stories) {
        this.top_stories = top_stories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Storie> getStories() {
        return stories;
    }

    public void setStories(List<Storie> stories) {
        this.stories = stories;
    }
}
