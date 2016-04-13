package datamodel;

import cn.bmob.v3.BmobObject;

/**
 * Created by bruse on 16/3/5.
 */
public class ImagePost extends BmobObject {
    private String imgPath;
    private String user;
    private String url;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
