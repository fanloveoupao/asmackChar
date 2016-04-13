package datamodel;

import cn.bmob.v3.BmobObject;

/**
 * Created by bruse on 16/2/27.
 */
public class Found extends BmobObject {
    //表字段的定义
    private String title;   //标题
    private String describe;  //描述
    private String phone;    //电话

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
