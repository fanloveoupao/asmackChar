package bruse.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.bmob.btp.callback.UploadListener;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import MyView.ListImageDirPopupWindow;
import adapter.ImageAdapter;
import base.BaseActivity;
import bruse.com.bruseapp.R;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import constant.Constants;
import datamodel.FolderBean;
import datamodel.ImagePost;
import user.MyUser;

public class ImageChooseActivity extends BaseActivity implements View.OnClickListener {

    private GridView mGridView;
    private ImageAdapter mImagaAdapter;
    private List<String> mImgs;

    private RelativeLayout mBottomLy;
    private TextView mDirName;
    private TextView mDirCount;

    private ProgressDialog mProgressDialog;
    private ListImageDirPopupWindow mDirPopupWindow;

    private static final int DATA_LOADED = 0x110;

    private File mCurrentDir;
    private int mMaxCount;
    /**
     * 多个上传的文件路径
     */
    private String[] files;

    private String[] url_download;

    private List<FolderBean> mFolderBeans = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DATA_LOADED) {
                mProgressDialog.dismiss();
                dataToView();
                initDirPopupWindow();
            }
        }
    };

    /**
     * 回调的activity
     */
    public static void launch(Activity activity, int requestCode) {

        Intent intent = new Intent(activity, ImageChooseActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, ImageChooseActivity.class);
        activity.startActivity(intent);
    }

    private void initDirPopupWindow() {
        mDirPopupWindow = new ListImageDirPopupWindow(this, mFolderBeans);
        mDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        mDirPopupWindow.setOnDirSelectedListener(new ListImageDirPopupWindow.onDirSelectedListener() {
            @Override
            public void onSelected(FolderBean folderBean) {
                mCurrentDir = new File(folderBean.getDir());
                mImgs = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg")
                                || filename.endsWith(".jpeg")
                                || filename.endsWith(".png")) {
                            return true;
                        }
                        return false;
                    }

                }));

                mImagaAdapter = new ImageAdapter(ImageChooseActivity.this, mImgs, mCurrentDir.getAbsolutePath());
                //
                mGridView.setAdapter(mImagaAdapter);

                mDirCount.setText(mImgs.size() + "");
                mDirName.setText(folderBean.getName());

                mDirPopupWindow.dismiss();

            }
        });

    }

    /**
     * 内容区域变量 透明度
     */
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    /**
     * 内容区域变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }


    //绑定数据到 VIew
    private void dataToView() {
        if (mCurrentDir == null) {
            Toast.makeText(this, "未扫描到如何图片", Toast.LENGTH_LONG).show();
            return;
        }
        mImgs = Arrays.asList(mCurrentDir.list());
        mImagaAdapter = new ImageAdapter(this, mImgs, mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mImagaAdapter);
        mDirCount.setText(mMaxCount + "");
        mDirName.setText(mCurrentDir.getName());

    }

    @Override
    public void setContentViews() {
        setContentView(R.layout.activity_image_choose);
    }

    @Override
    public void initViews() {
        mGridView = (GridView) findViewById(R.id.id_gridView);
        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_layout);
        mDirName = (TextView) findViewById(R.id.id_dir_name);
        mDirCount = (TextView) findViewById(R.id.id_dir_count);

        choose_cancel = (Button) findViewById(R.id.img_head_top_back);
        choose_ok = (Button) findViewById(R.id.img_head_top_ok);
    }

    @Override
    public void initListeners() {
        mBottomLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 所以文件夹的图片
                 * */
                mDirPopupWindow.setAnimationStyle(R.style.dir_popupwindow_anim);
                mDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
                lightOff();
            }
        });

        choose_cancel.setOnClickListener(this);
        choose_ok.setOnClickListener(this);
    }

    @Override
    public void initDatas() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前存储卡不可用", Toast.LENGTH_LONG).show();
        }
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread() {
            @Override
            public void run() {
                Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = ImageChooseActivity.this.getContentResolver();
                Cursor cursor = cr.query(mImgUri,
                        null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED
                );

                Set<String> mDirPaths = new HashSet<String>();
                if (cursor == null)
                    return;
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) continue;//->会存在这种情况

                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }
                    if (parentFile.list() == null)
                        continue;
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".jpeg")
                                    || filename.endsWith(".png"))
                                return true;
                            return false;
                        }
                    }).length;
                    folderBean.setCount(picSize);

                    mFolderBeans.add(folderBean);

                    if (picSize > mMaxCount) {
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }
                }

                cursor.close();
                //通知扫描完成
                mHandler.sendEmptyMessage(DATA_LOADED);
            }
        }.start();
    }

    /**
     * 提交
     * 返回
     */

    private Button choose_cancel;
    private Button choose_ok;
    private String filepath = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head_top_ok:
                if ("头像选择".equals(Constants.CHOOSER)) {
                    if ("".equals(filepath)) {
                        Toast.makeText(getApplicationContext(), "先选择图片", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadSingle();
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    if (mImagaAdapter.getmSeletedImg().size() == 0) {
                        Toast.makeText(getApplicationContext(), "先选择图片", Toast.LENGTH_SHORT).show();
                    } else {
                        Iterator iterator = mImagaAdapter.getmSeletedImg().iterator();
                        files = new String[mImagaAdapter.getmSeletedImg().size()];
                        int i = 0;
                        while (iterator.hasNext()) {
                            files[i] = iterator.next().toString();
                            i++;
                        }

                        finish();
                    }

                }
                break;
            case R.id.img_head_top_back:
                finish();
                break;
        }
    }

    /**
     * 单一上传
     */

    public void uploadSingle() {
        filepath = Constants.HEAD_URL;
        Log.i("上传", "url是" + filepath);
        if (filepath != null) {
            BTPFileResponse response = BmobProFile.getInstance(getApplicationContext()).upload(filepath, new UploadListener() {
                @Override
                public void onSuccess(String s, String s1, BmobFile bmobFile) {
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                    Log.i("上传", bmobFile.getUrl());
                    Constants.ICON_URL = bmobFile.getUrl();
                    updateUserHeader();
                    finish();

                }

                @Override
                public void onProgress(int i) {
//                    mProgressDialog.show();
//                    mProgressDialog.setProgress(i);

                }

                @Override
                public void onError(int i, String s) {
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "上传失败" + s, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "请选择头像", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 更新用户头像
     */

    public void updateUserHeader() {
        MyUser userInfo = BmobUser.getCurrentUser(getApplicationContext(), MyUser.class);
        userInfo.setUserHead(Constants.ICON_URL);
        userInfo.update(getApplicationContext(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), " 更新用户成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "更新用户失败" + s, Toast.LENGTH_SHORT).show();
            }
        });

    }




}
