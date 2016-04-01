package com.kai.myzhihu.bean;

import java.util.List;

/**
 * Created by 凯 on 2016/3/25 10:16
 */
public class Storie {
    private List<String> images;
    private int type;
    private long id;
    private boolean multipic;
    private String ga_prefix;
    private String title;
    private boolean isFirst=false;
    private String date;//-1代表是今天的

    public Storie(List<String> images, int type, long id, String ga_prefix, String title) {
        this.images = images;
        this.type = type;
        this.id = id;
        this.ga_prefix = ga_prefix;
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public boolean isMultipic() {
        return multipic;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Storie{" +
                "images=" + images +
                ", type=" + type +
                ", id=" + id +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", title='" + title + '\'' +
                ", isFirst=" + isFirst +
                '}';
    }
}
