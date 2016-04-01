package com.kai.myzhihu.bean;

import java.util.List;

/**
 * Created by 凯 on 2016/3/27 19:51
 */
public class ThemeContent {
    private List<Storie> stories;
    private String description;
    private String background;
    private long color;
    private String name;
    private String image;
    private List<Editor> editors;
    private String image_source;

    public List<Storie> getStories() {
        return stories;
    }

    public void setStories(List<Storie> stories) {
        this.stories = stories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public long getColor() {
        return color;
    }

    public void setColor(long color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Editor> getEditors() {
        return editors;
    }

    public void setEditors(List<Editor> editors) {
        this.editors = editors;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }
}
