package com.kai.myzhihu.bean;

/**
 * Created by 凯 on 2016/3/27 19:54
 */
public class Editor {
    private int id;
    private String avatar;
    private String name;

    public int getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Editor{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
