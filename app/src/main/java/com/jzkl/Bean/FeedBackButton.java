package com.jzkl.Bean;

public class FeedBackButton {
    private String feedbackBId;
    private String feedbackBConten;
    private String feedbackBName;

    public String getFeedbackBId() {
        return feedbackBId;
    }

    public void setFeedbackBId(String feedbackBId) {
        this.feedbackBId = feedbackBId;
    }

    public String getFeedbackBName() {
        return feedbackBName;
    }

    public void setFeedbackBName(String feedbackBName) {
        this.feedbackBName = feedbackBName;
    }

    public String getFeedbackBConten() {
        return feedbackBConten;
    }

    public void setFeedbackBConten(String feedbackBConten) {
        this.feedbackBConten = feedbackBConten;
    }

    @Override
    public String toString() {
        return "FeedBackButton{" +
                "feedbackBId='" + feedbackBId + '\'' +
                ", feedbackBConten='" + feedbackBConten + '\'' +
                ", feedbackBName='" + feedbackBName + '\'' +
                '}';
    }
}
