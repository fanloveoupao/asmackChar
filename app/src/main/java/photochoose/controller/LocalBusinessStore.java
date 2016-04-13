package photochoose.controller;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

import base.Application;
import photochoose.bean.ImageGroupBean;

/**
 * Created by bruse on 16/3/6.
 */
public class LocalBusinessStore {


    public LocalBusinessStore() {

    }

    /**
     * 获取最近的100张图片
     * @return
     * @throws Exception
     */
    public ArrayList<ImageGroupBean> getLastLocalImageList() throws Exception {
        ArrayList<ImageGroupBean> mGruopList = new ArrayList<ImageGroupBean>();
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = Application.app.getContentResolver();
        // 构建查询条件，且只查询jpeg和png的图片
        StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?");
        Cursor mCursor = null;
        try {
            // 初始化游标
            mCursor = mContentResolver.query(mImageUri, null, selection.toString(), new String[]{
                    "image/jpeg", "image/png", "image/jpg"
            }, MediaStore.Images.Media.DATE_TAKEN + " DESC" + " limit 100");
            // 遍历结果
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                //构建一个所有图片的文件夹
                ImageGroupBean allItem = new ImageGroupBean();
                allItem.setDirName("最近图片");
                // 是否包含所有图片文件夹,包含侧返回下标
                int hasAllItemDirectory = mGruopList.indexOf(allItem);
                if (hasAllItemDirectory >= 0) {
                    // 如果是，该组的图片数量+1
                    ImageGroupBean imageGroup = mGruopList.get(hasAllItemDirectory);
                    if (imageGroup.getImageCount() < 100) {
                        imageGroup.addImage(path);
                    }
                } else {
                    // 否则，将该对象加入到groupList中
                    allItem.addImage(path);
                    mGruopList.add(allItem);
                }
            }
            return mGruopList;
        } catch (Exception e) {
            throw new Exception("");
        } finally {
            // 关闭游标
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        }
    }


    /**
     * 获取最近的图片
     * @return
     * @throws Exception
     */
    public ArrayList<ImageGroupBean> getLocalImageList() throws Exception {
        ArrayList<ImageGroupBean> mGruopList = new ArrayList<ImageGroupBean>();
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = Application.app.getContentResolver();
        // 构建查询条件，且只查询jpeg和png的图片
        StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("=?");
        Cursor mCursor = null;
        try {
            // 初始化游标
            mCursor = mContentResolver.query(mImageUri, null, selection.toString(), new String[]{
                    "image/jpeg", "image/png", "image/jpg"
            }, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            // 遍历结果
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                // 获取该图片的所在文件夹的路径
                File file = new File(path);
                String parentName = "";
                if (file.getParentFile() != null) {
                    parentName = file.getParentFile().getName();
                } else {
                    parentName = file.getName();
                }
                //构建一个所有图片的文件夹
                ImageGroupBean allItem = new ImageGroupBean();
                allItem.setDirName("最近图片");
                // 是否包含所有图片文件夹,包含侧返回下标
                int hasAllItemDirectory = mGruopList.indexOf(allItem);
                if (hasAllItemDirectory >= 0) {
                    // 如果是，该组的图片数量+1
                    ImageGroupBean imageGroup = mGruopList.get(hasAllItemDirectory);
                    if (imageGroup.getImageCount() < 100) {
                        imageGroup.addImage(path);
                    }
                } else {
                    // 否则，将该对象加入到groupList中
                    allItem.addImage(path);
                    mGruopList.add(allItem);
                }
                // 构建一个imageGroup对象
                ImageGroupBean item = new ImageGroupBean();
                // 设置imageGroup的文件夹名称
                item.setDirName(parentName);
                // 寻找该imageGroup是否是其所在的文件夹中的第一张图片
                int searchIdx = mGruopList.indexOf(item);
                if (searchIdx >= 0) {
                    // 如果是，该组的图片数量+1
                    ImageGroupBean imageGroup = mGruopList.get(searchIdx);
                    imageGroup.addImage(path);
                } else {
                    // 否则，将该对象加入到groupList中
                    item.addImage(path);
                    mGruopList.add(item);
                }
            }
            return mGruopList;
        } catch (Exception e) {
            throw new Exception("");
        } finally {
            // 关闭游标
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        }
    }
}
