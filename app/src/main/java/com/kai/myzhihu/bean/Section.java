package com.kai.myzhihu.bean;

/**
 * Created by 凯 on 2016/3/30 16:20
 */
public class Section {
    private String thumbnail;
    private long id;
    private String name;

    @Override
    public String toString() {
        return "Section{" +
                "thumbnail='" + thumbnail + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
