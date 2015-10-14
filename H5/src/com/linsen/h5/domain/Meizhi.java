package com.linsen.h5.domain;

import java.io.Serializable;

/**
 * Created by drakeet on 6/20/15.
 */
public class Meizhi  implements Serializable {
	private static final long serialVersionUID = 3310299497242268579L;
	private String mid;
    private String url;
    private int thumbWidth;
    private int thumbHeight;

    public Meizhi(String id, String url) {
        this.mid = id;
        this.url = url;
    }

    public Meizhi(String mid, String url, int thumbWidth, int thumbHeight) {
        this.mid = mid;
        this.url = url;
        this.thumbWidth = thumbWidth;
        this.thumbHeight = thumbHeight;
    }

    public Meizhi() {
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public void setThumbWidth(int thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }

    public void setThumbHeight(int thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String id) {
        this.mid = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
