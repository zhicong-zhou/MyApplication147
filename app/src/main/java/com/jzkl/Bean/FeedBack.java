package com.jzkl.Bean;

import org.json.JSONArray;

public class FeedBack {
    private String mFeedBackTitle;
    private String mFeedBackContent;
    private String mFeedBackStatus;
    private String mFeedBackTime;
    private String mFeedBackDescript;
    private JSONArray mList;

    public String getmFeedBackDescript() {
        return mFeedBackDescript;
    }

    public void setmFeedBackDescript(String mFeedBackDescript) {
        this.mFeedBackDescript = mFeedBackDescript;
    }

    public JSONArray getmList() {
        return mList;
    }

    public void setmList(JSONArray mList) {
        this.mList = mList;
    }

    public String getmFeedBackTitle() {
        return mFeedBackTitle;
    }

    public void setmFeedBackTitle(String mFeedBackTitle) {
        this.mFeedBackTitle = mFeedBackTitle;
    }

    public String getmFeedBackContent() {
        return mFeedBackContent;
    }

    public void setmFeedBackContent(String mFeedBackContent) {
        this.mFeedBackContent = mFeedBackContent;
    }

    public String getmFeedBackStatus() {
        return mFeedBackStatus;
    }

    public void setmFeedBackStatus(String mFeedBackStatus) {
        this.mFeedBackStatus = mFeedBackStatus;
    }

    public String getmFeedBackTime() {
        return mFeedBackTime;
    }

    public void setmFeedBackTime(String mFeedBackTime) {
        this.mFeedBackTime = mFeedBackTime;
    }

    @Override
    public String toString() {
        return "FeedBack{" +
                "mFeedBackTitle='" + mFeedBackTitle + '\'' +
                ", mFeedBackContent='" + mFeedBackContent + '\'' +
                ", mFeedBackStatus='" + mFeedBackStatus + '\'' +
                ", mFeedBackTime='" + mFeedBackTime + '\'' +
                ", mFeedBackDescript='" + mFeedBackDescript + '\'' +
                ", mList=" + mList +
                '}';
    }
}
