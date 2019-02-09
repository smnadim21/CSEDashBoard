package com.nadim.csedashboard.dataset;

/**
 * Created by d3stR0y3r on 10/14/2018.
 */
public class NoticeData {
    String noticetitle , categories;
    String session;
    String notice_picture;
    Long timestamp;

    NoticeData()
    {

    }

    public NoticeData(String noticetitle, String session, String notice_picture, Long timestamp,String categories) {
        this.noticetitle = noticetitle;
        this.session = session;
        this.notice_picture = notice_picture;
        this.timestamp = timestamp;
        this.categories = categories;
    }

    public String getNoticetitle() {
        return noticetitle;
    }

    public void setNoticetitle(String noticetitle) {
        this.noticetitle = noticetitle;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getNotice_picture() {
        return notice_picture;
    }

    public void setNotice_picture(String notice_picture) {
        this.notice_picture = notice_picture;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
