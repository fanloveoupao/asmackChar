package datamodel;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by bruse on 16/3/3.
 */
public class UserItem extends BmobObject{
    private BmobFile imgId;
    private String item;

    public BmobFile getImgId() {
        return imgId;
    }

    public void setImgId(BmobFile imgId) {
        this.imgId = imgId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
