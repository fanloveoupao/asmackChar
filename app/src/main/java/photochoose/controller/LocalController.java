package photochoose.controller;

import java.util.ArrayList;

import event.ErrorUtil;
import photochoose.bean.ImageGroupBean;

/**
 * Created by bruse on 16/3/6.
 */
public class LocalController {
    private LocalBusinessStore mLocalBusinessStore;
    private LocalController() {
        if (mLocalBusinessStore == null) {
            mLocalBusinessStore = new LocalBusinessStore();
        }
    }

    private static class SingletonHolder {
        public static final LocalController INSTANCE = new LocalController();
    }

    public static LocalController getInstance() {

        return SingletonHolder.INSTANCE;
    }


    /**
     * 获取本地的所有图片
     *
     * @param listener
     */
    public void getLocalImage( final Listener<ArrayList<ImageGroupBean>> listener) {

        if (listener != null) {
            listener.onStart();
        }
        try {

            ArrayList<ImageGroupBean> result=new LocalBusinessStore().getLocalImageList();
            if (listener != null) {
                listener.onComplete(result);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }

    }

    /**
     * 获取本地最近的100张图片
     *
     * @param listener
     */
    public void getLastLocalImage(final Listener<ArrayList<ImageGroupBean>> listener) {
        if (listener != null) {
            listener.onStart();
        }
        try {

            ArrayList<ImageGroupBean> result=new LocalBusinessStore().getLastLocalImageList();
            if (listener != null) {
                listener.onComplete(result);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onFail(ErrorUtil.getNetworkError());
            }
        }

    }
}
