package com.nadim.csedashboard.dataset;

/**
 * Created by d3stR0y3r on 1/22/2019.
 */
public class UserProfileSet {
    String name,id,session,regno,address,mobile,email,editconf,usertype,approvedby,signuptime, password, image;

    public UserProfileSet() {

    }


    public UserProfileSet(String name, String id, String session, String address, String mobile, String email, String editconf, String usertype, String approvedby, String signuptime, String password) {
        this.name = name;
        this.id = id;
        this.session = session;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
        this.editconf = editconf;
        this.usertype = usertype;
        this.approvedby = approvedby;
        this.signuptime = signuptime;
        this.password = password;
    }

    public UserProfileSet(String name, String id, String session, String address, String mobile, String email, String editconf, String usertype, String approvedby, String signuptime, String password, String image) {
        this.name = name;
        this.id = id;
        this.session = session;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
        this.editconf = editconf;
        this.usertype = usertype;
        this.approvedby = approvedby;
        this.signuptime = signuptime;
        this.password = password;
        this.image = image;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getEditconf() {
        return editconf;
    }

    public void setEditconf(String editconf) {
        this.editconf = editconf;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getApprovedby() {
        return approvedby;
    }

    public void setApprovedby(String approvedby) {
        this.approvedby = approvedby;
    }

    public String getSignuptime() {
        return signuptime;
    }

    public void setSignuptime(String signuptime) {
        this.signuptime = signuptime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}