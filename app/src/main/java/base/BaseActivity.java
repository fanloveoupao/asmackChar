package base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;

import cn.bmob.v3.listener.SaveListener;
import datamodel.UserItem;

/**
 * Created by bruse on 16/2/27.
 */
public abstract class BaseActivity extends Activity {
    private ProgressDialog mProgressDialog;
    protected int mScreenWidth;
    protected int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentViews();


//        ActivitiesHelper.getInstance().addActivity(this);
        initViews();
        initListeners();
        initDatas();
    }

    //一些抽象方法的实现

    /**
     * 设置布局文件
     */
    public abstract void setContentViews();

    /**
     * 初始化布局文件中的控件
     */
    public abstract void initViews();

    /**
     * 初始化布局控件的监听
     */
    public abstract void initListeners();

    /**
     * 初始化数据
     */
    public abstract void initDatas();
    /**
     * 显示进度框
     */
    public void showProgressDialog(String title){
        if(!isFinishing()){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(title);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }
    }

    /**
     * 隐藏进度框
     */
    public void dismissProgressDialog(){
        if (mProgressDialog != null&&!isFinishing()) {
            mProgressDialog.dismiss();
        }
    }


}
