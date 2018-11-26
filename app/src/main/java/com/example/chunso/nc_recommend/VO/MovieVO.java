package com.example.chunso.nc_recommend.VO;

import java.util.Queue;

public class MovieVO {
    private String title;
    private String imgUrl;
    private String runningTime;
    private String productYear;
    private String summary;
    private Queue<String> mode;

    public String getTitle() {
        return title;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public String getProductYear() {
        return productYear;
    }

    public String getSummary() {
        return summary;
    }

    public Queue<String> getMode() {
        return mode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }

    public void setProductYear(String productYear) {
        this.productYear = productYear;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setMode(Queue<String> mode) {
        this.mode = mode;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
