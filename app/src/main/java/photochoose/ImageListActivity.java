package photochoose;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import MyView.ToastUtils;
import base.BaseActivity;
import bruse.com.bruseapp.R;
import bruse.ui.AddActivity;
import constant.Constants;
import de.greenrobot.event.EventBus;
import event.BimpSyncEvent;
import event.EventBusUtil;
import event.ImageEvent;
import event.RefreshImageListEvent;
import event.TakePhotoEvent;
import photochoose.adapter.ImageListAdapter;
import photochoose.bean.ImageGroupBean;
import photochoose.controller.Listener;
import photochoose.controller.LocalController;
import util.Bimp;
import util.Util;

/**
 * Created by bruse on 16/3/6.
 */


public class ImageListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public enum LaunchEnum {
        clickLaunch,//普通点击
        loadLastLaunch,//需要加载最近图片
    }

    //title
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_IMAGES_DATAS = "extra_images";
    public static final String EXTRA_FROM_CHAT = "extra_from_chat";
    public static final String EXTRA_LAUNCH_ENUM = "extra_launch_enum";

    //title
    //图片列表GridView
    private GridView mImagesGv = null;
    //图片地址数据源
    private ArrayList<String> mImages = new ArrayList<String>();
    //适配器
    private ImageListAdapter mImageAdapter = null;

    //是否从聊天页面的图片
    private boolean isFromChat;
    private String mTitle;
    private TextView mTitleTv;

    private LaunchEnum mLaunch = LaunchEnum.clickLaunch;

    public static void launch(Activity activity, String title, boolean from_chat, ArrayList<String> imgs, LaunchEnum launchEnum) {
        Intent mIntent = new Intent(activity, ImageListActivity.class);
        mIntent.putExtra(ImageListActivity.EXTRA_TITLE, title);
        mIntent.putExtra(EXTRA_FROM_CHAT, from_chat);
        mIntent.putStringArrayListExtra(ImageListActivity.EXTRA_IMAGES_DATAS, imgs);
        mIntent.putExtra(EXTRA_LAUNCH_ENUM, launchEnum);
        activity.startActivity(mIntent);
    }

    @Override
    public void setContentViews() {
        setContentView(R.layout.activity_image_list);
        EventBusUtil.getInstance().getCommonEventBus().register(this);
    }

    @Override
    public void initViews() {
        mImagesGv = (GridView) findViewById(R.id.images_gv);
    }

    @Override
    public void initListeners() {
        initTitleBar("最近图片");
    }

    @Override
    public void initDatas() {
        isFromChat = getIntent().getBooleanExtra(EXTRA_FROM_CHAT, false);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        mLaunch = (LaunchEnum) getIntent().getSerializableExtra(EXTRA_LAUNCH_ENUM);
        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        }
        if (mLaunch == LaunchEnum.clickLaunch) {
            ArrayList<String> imgsList = getIntent().getStringArrayListExtra(EXTRA_IMAGES_DATAS);
            if (imgsList != null && imgsList.size() > 0) {
                mImages = getIntent().getStringArrayListExtra(EXTRA_IMAGES_DATAS);
                setAdapter(mImages);
            }
        } else if (mLaunch == LaunchEnum.loadLastLaunch) {
            getLastImage();
        }
    }

    private void initTitleBar(String title) {
        mTitleTv = (TextView) findViewById(R.id.center_title);
        mTitleTv.setText(title);
        findViewById(R.id.left_back_image_botton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.right_text_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ImageEvent());
                EventBus.getDefault().post(new TakePhotoEvent());
                finish();
            }
        });
    }


    /**
     * 获取最近图片的
     */
    private void getLastImage() {
        LocalController.getInstance().getLastLocalImage(new Listener<ArrayList<ImageGroupBean>>() {
            @Override
            public void onStart(Object... params) {
//                showProgr essDialog("请稍候...");
            }

            @Override
            public void onComplete(ArrayList<ImageGroupBean> result, Object... params) {
                dismissProgressDialog();
                // 如果加载成功
                if (result == null || result.size() <= 0 || result.get(0).getImages() == null
                        || result.get(0).getImages().size() <= 0) {
                    ToastUtils.show(ImageListActivity.this, getString(R.string.no_images));
                    return;
                }
                ImageGroupBean imageGroup = result.get(0);
                mImages = imageGroup.getImages();
                setAdapter(mImages);
            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastUtils.show(ImageListActivity.this, getString(R.string.loaded_fail));
            }
        });
    }

    /**
     * 构建并初始化适配器
     *
     * @param datas
     */
    private void setAdapter(ArrayList<String> datas) {
        mImageAdapter = new ImageListAdapter(this, datas, mImagesGv);
        mImagesGv.setAdapter(mImageAdapter);
        mImagesGv.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (Bimp.max == 1) {
            Bimp.clear();
            Bimp.mSelectedList.add(mImages.get(position));
            EventBus.getDefault().post(new ImageEvent());
            finish();
        } else {
            //ImageBrowseActivity.launch(ImageListActivity.this, mImages, position, ImageBrowseActivity.LaunchEnum.selected);
            if (position == 0) {
                EventBus.getDefault().post(new TakePhotoEvent());
                finish();
            } else {
                ImageBrowseActivity.launch(ImageListActivity.this, mImages, position, ImageBrowseActivity.LaunchEnum.selected);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mImageAdapter != null) {
            Util.saveSelectedImags(this, mImageAdapter.getSelectedImgs());
        }
        super.onBackPressed();
    }

    public void onEventMainThread(BimpSyncEvent e) {
        if (mImageAdapter != null) {
            mImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.getInstance().getCommonEventBus().unregister(this);
    }

}
