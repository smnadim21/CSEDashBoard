package com.nadim.csedashboard.dataset;

/**
 * Created by d3stR0y3r on 11/8/2018.
 */
public class Users {

    private String Name;
    private String Session;
    private String id;
    private String regno;
    private String email;
    private String uid,type,mobile,address,login_email;



    public Users() {
    }



    public Users(String name, String session, String id, String regno, String email , String uid , String type, String mobile, String address,String login_email) {
        Name = name;
        Session = session;
        this.id = id;
        this.regno = regno;
        this.email = email;
        this.uid = uid;
        this.type = type;
        this.mobile = mobile;
        this.address = address;
        this.login_email = login_email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSession() {
        return Session;
    }

    public void setSession(String session) {
        Session = session;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogin_email() {
        return login_email;
    }

    public void setLogin_email(String login_email) {
        this.login_email = login_email;
    }
}
