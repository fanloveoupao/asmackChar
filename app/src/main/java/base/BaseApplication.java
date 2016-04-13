package base;

import android.app.*;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

import cn.bmob.v3.Bmob;
import util.ImageLoaderWrapper;

/**
 * Created by bruse on 16/3/9.
 */
public abstract class BaseApplication extends android.app.Application {

    public static BaseApplication app;
    public static Handler mainHandler;
    //上线前要关的
    public static final boolean DEBUG = true;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mainHandler = new Handler(Looper.getMainLooper());

        maybeSetupStrictMode();

        initImageLoader();// 初始化图片加载器


    }





    /**
     * 开发状态下打开严格模式
     */
    private void maybeSetupStrictMode() {
        if (!DEBUG)
            return;

        try {
            Class<?> strictMode = Class.forName("android.os.StrictMode");
            Method enableDefaults = strictMode.getMethod("enableDefaults");
            enableDefaults.invoke(strictMode);
        } catch (Exception e) {
            Log.e(BaseApplication.class.getSimpleName(),
                    "Failed to turn on strict mode", e);
        }
    }

    /**
     * 初始化图片加载器
     */
    private void initImageLoader() {
        // 初始化图片加载器模块
        File imageDiskCacheDir = getImageTmpDir();
        ImageLoaderWrapper.initDefault(this, imageDiskCacheDir,
                DEBUG);
    }


    public void logOut(Activity activity) {
        //-----注销登录
    }

    /**
     * 获取图片临时目录(网络图片缓存)
     *
     * @return
     */
    public File getImageTmpDir() {
        File dir = new File(getTmpDir(), "image_cache");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取SD卡上的裁剪图片临时目录
     *
     * @return
     */
    public File getCropTmpDirWithSdCard() {
        if (getTmpDir(true) == null) {
            return null;
        }

        File dir = new File(getTmpDir(true), "crop_image_cache");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取裁剪图片临时目录
     *
     * @return
     */
    public File getCropTmpDir() {
        File dir = new File(getTmpDir(), "crop_image_cache");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取日志文件临时目录
     *
     * @return
     */
    public File getLogTmpDir() {
        File dir = new File(getTmpDir(), "log_cache");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取临时目录
     *
     * @return
     */
    public File getTmpDir() {
        return getTmpDir(false);
    }

    /**
     * 获取临时目录
     *
     * @param isSdcard 是否只取sd卡上的目录
     * @return
     */
    public File getTmpDir(boolean isSdcard) {
        File tmpDir = null;
        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (isSdcard && !sdCardExist) {
            if (!sdCardExist) {
                return null;
            }
        }

        if (sdCardExist || isSdcard) {
            tmpDir = new File(Environment.getExternalStorageDirectory(),
                    getTmpDirName());
        } else {
            tmpDir = new File(getCacheDir(), getTmpDirName());
        }

        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        return tmpDir;
    }

    public abstract String getTmpDirName();
}
