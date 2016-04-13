package user;

import cn.bmob.v3.BmobUser;

/**
 * Created by bruse on 16/3/1.
 */
public class MyUser extends BmobUser {
    private boolean sex;
    private String age;
    private String icon;
    private String userHead;

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
