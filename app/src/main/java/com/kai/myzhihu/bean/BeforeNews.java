package com.kai.myzhihu.bean;

import java.util.List;

/**
 * Created by 凯 on 2016/3/25 10:17
 */
public class BeforeNews {
    private String date;
    private List<Storie> stories;

    public BeforeNews(String date, List<Storie> stories) {
        this.date = date;
        this.stories = stories;
    }


    public String getDate() {
        return date;
    }


    public List<Storie> getStories() {
        return stories;
    }

    @Override
    public String toString() {
        return "BeforeNews{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                '}';
    }
}
