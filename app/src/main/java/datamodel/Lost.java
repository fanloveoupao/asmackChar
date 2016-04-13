package datamodel;

import cn.bmob.v3.BmobObject;

/**
 * Created by bruse on 16/2/27.
 */
public class Lost extends BmobObject {
    //表字段的定义
    private String title;   //标题
    private String describe;  //描述
    private String phone;    //电话
    private String head_url;
    private String user;
    private String time;
    private String imageone;
    private String imagetwo;
    private String imagethree;

    public String getImageone() {
        return imageone;
    }

    public void setImageone(String imageone) {
        this.imageone = imageone;
    }

    public String getImagetwo() {
        return imagetwo;
    }

    public void setImagetwo(String imagetwo) {
        this.imagetwo = imagetwo;
    }

    public String getImagethree() {
        return imagethree;
    }

    public void setImagethree(String imagethree) {
        this.imagethree = imagethree;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
