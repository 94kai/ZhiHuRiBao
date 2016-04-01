package com.kai.myzhihu.bean;

import java.util.List;

/**
 * Created by 凯 on 2016/3/27 15:08
 */
public class Themes {
    private int limit;
    private List<Integer> subscribed;
    private List<Theme> others;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Integer> getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(List<Integer> subscribed) {
        this.subscribed = subscribed;
    }

    public List<Theme> getOthers() {
        return others;
    }

    public void setOthers(List<Theme> others) {
        this.others = others;
    }
}
