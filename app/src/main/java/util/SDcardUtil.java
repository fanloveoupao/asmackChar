package util;

import android.os.Environment;

/**
 * Created by bruse on 16/3/6.
 */
public class SDcardUtil {
    /**
     * 是否有SD卡
     *
     * @return
     */
    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
