package com.kai.myzhihu.bean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 凯 on 2016/3/30 16:10
 */
public class NewsContent {
    private String body;
    private String image_source;
    private String title;
    private String image;
    private String share_url;
    private String[] js;
    private List<Avatar> recommenders;
    private long ga_prefix;
    private Section section;
    private int type;
    private long id;
    private String[] css;
    private String theme_name;
    private String editor_name;
    private long theme_id;

    @Override
    public String toString() {
        return "NewsContent{" +
                "body='" + body + '\'' +
                ", image_source='" + image_source + '\'' +
                ", title='" + title + '\'' +
                ", imgae='" + image + '\'' +
                ", share_url='" + share_url + '\'' +
                ", js=" + Arrays.toString(js) +
                ", recommenders=" + recommenders +
                ", ga_prefix=" + ga_prefix +
                ", section=" + section +
                ", type=" + type +
                ", id=" + id +
                ", css=" + Arrays.toString(css) +
                ", theme_name='" + theme_name + '\'' +
                ", editor_name='" + editor_name + '\'' +
                ", theme_id=" + theme_id +
                '}';
    }

    public String toString2() {
        return "NewsContent{" +
                ", image_source='" + image_source + '\'' +
                ", title='" + title + '\'' +
                ", imgae='" + image + '\'' +
                ", share_url='" + share_url + '\'' +
                ", js=" + Arrays.toString(js) +
                ", recommenders=" + recommenders +
                ", ga_prefix=" + ga_prefix +
                ", section=" + section +
                ", type=" + type +
                ", theme_name='" + theme_name + '\'' +
                ", editor_name='" + editor_name + '\'' +
                ", theme_id=" + theme_id +
                '}';
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String[] getJs() {
        return js;
    }

    public void setJs(String[] js) {
        this.js = js;
    }

    public List<Avatar> getRecommenders() {
        return recommenders;
    }

    public void setRecommenders(List<Avatar> recommenders) {
        this.recommenders = recommenders;
    }

    public long getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(long ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
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

    public String[] getCss() {
        return css;
    }

    public void setCss(String[] css) {
        this.css = css;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    public String getEditor_name() {
        return editor_name;
    }

    public void setEditor_name(String editor_name) {
        this.editor_name = editor_name;
    }

    public long getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(long theme_id) {
        this.theme_id = theme_id;
    }
}
