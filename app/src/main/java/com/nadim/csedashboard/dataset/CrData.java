package com.nadim.csedashboard.dataset;

/**
 * Created by d3stR0y3r on 11/24/2018.
 */
public class CrData {

    String name , batch , session , mobile , email ,image;

    public CrData() {
    }

    public CrData(String name, String batch, String session, String mobile, String email) {
        this.name = name;
        this.batch = batch;
        this.session = session;
        this.mobile = mobile;
        this.email = email;
    }

    public CrData(String name, String image, String email, String mobile, String session, String batch) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.mobile = mobile;
        this.session = session;
        this.batch = batch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
