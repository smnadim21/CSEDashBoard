package com.nadim.csedashboard.dataset;

/**
 * Created by d3stR0y3r on 11/5/2018.
 */
public class TeacherData {

    private String _id;
    private String Teacher;
    private String Post;
    private String Phone;
    private String Email;
    private String Mobile;
    private String Head;
    private String Interest;
    private String Publications;
    private String Dept;
    private String uid;
    private String image;


    public TeacherData() {
    }

    public TeacherData(String _id, String teacher, String post, String phone, String email, String mobile, String head, String interest, String publications, String dept, String uid) {
        this._id = _id;
        Teacher = teacher;
        Post = post;
        Phone = phone;
        Email = email;
        Mobile = mobile;
        Head = head;
        Interest = interest;
        Publications = publications;
        Dept = dept;
        this.uid = uid;
    }

    public TeacherData(String _id, String teacher, String post, String phone, String email, String mobile, String head, String interest, String publications, String dept, String uid, String image) {
        this._id = _id;
        Teacher = teacher;
        Post = post;
        Phone = phone;
        Email = email;
        Mobile = mobile;
        Head = head;
        Interest = interest;
        Publications = publications;
        Dept = dept;
        this.uid = uid;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }

    public String getPost() {
        return Post;
    }

    public void setPost(String post) {
        Post = post;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getHead() {
        return Head;
    }

    public void setHead(String head) {
        Head = head;
    }

    public String getInterest() {
        return Interest;
    }

    public void setInterest(String interest) {
        Interest = interest;
    }

    public String getPublications() {
        return Publications;
    }

    public void setPublications(String publications) {
        Publications = publications;
    }

    public String getDept() {
        return Dept;
    }

    public void setDept(String dept) {
        Dept = dept;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
