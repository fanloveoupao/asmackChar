package photochoose;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import MyView.LoadingLayout;
import MyView.ToastUtils;
import base.BaseActivity;
import bruse.com.bruseapp.R;
import event.EventBusUtil;
import event.RefreshImageListEvent;
import event.TakePhotoEvent;
import photochoose.adapter.ImageGroupAdapter;
import photochoose.bean.ImageGroupBean;
import photochoose.controller.Listener;
import photochoose.controller.LocalController;
import util.Bimp;
import util.SDcardUtil;

/**
 * Created by bruse on 16/3/6.
 */
public class PhotoAllActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, PhotoAllActivity.class);
        activity.startActivity(intent);
    }


    public final static String EXTRA_FROM_CHAT = "extra_from_chat";

    public static void launchFromChat(Activity activity) {
        Bimp.clear();
        Intent intent = new Intent(activity, PhotoAllActivity.class);
        intent.putExtra(EXTRA_FROM_CHAT, true);
        activity.startActivity(intent);
    }

    //loading布局
    private LoadingLayout mLoadingLayout = null;
    //图片组GridView
    private GridView mGroupImagesGv = null;
    //适配器
    private ImageGroupAdapter mGroupAdapter = null;
    //存放图片<文件夹,该文件夹下的图片列表>键值对
    private ArrayList<ImageGroupBean> mGruopList = new ArrayList<ImageGroupBean>();
    private TextView mTitle;

    @Override
    public void setContentViews() {
        setContentView(R.layout.activity_all_photos);
        ImageListActivity.launch(PhotoAllActivity.this, "最近图片",
                getIntent().getBooleanExtra(EXTRA_FROM_CHAT, false), null, ImageListActivity.LaunchEnum.loadLastLaunch);
        EventBusUtil.getInstance().getCommonEventBus().register(PhotoAllActivity.this);
        EventBusUtil.getInstance().getMessageEventBus().register(PhotoAllActivity.this);
    }

    public void onEventMainThread(TakePhotoEvent event) {
        finish();
    }

    /**
     * 初始化界面元素
     */
    @Override
    public void initViews() {
        mLoadingLayout = (LoadingLayout) findViewById(R.id.loading_layout);
        mGroupImagesGv = (GridView) findViewById(R.id.images_gv);
    }

    /**
     * 获取最近图片的
     */
    private void getAllImage() {
        LocalController.getInstance().getLocalImage(new Listener<ArrayList<ImageGroupBean>>() {
            @Override
            public void onStart(Object... params) {
                showProgressDialog("请稍候...");
            }

            @Override
            public void onComplete(ArrayList<ImageGroupBean> result, Object... params) {
                dismissProgressDialog();
                setImageAdapter(result);
            }

            @Override
            public void onFail(String msg, Object... params) {
                dismissProgressDialog();
                ToastUtils.show(PhotoAllActivity.this, getString(R.string.loaded_fail));
            }
        });
    }

    @Override
    public void initListeners() {
        mTitle = (TextView) findViewById(R.id.center_title);
        mTitle.setText("相册");
        findViewById(R.id.left_back_image_botton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initDatas() {
        if (!SDcardUtil.hasExternalStorage()) {
            mLoadingLayout.showEmpty(getString(R.string.donot_has_sdcard));
            return;
        }
        //获取所有图片
        getAllImage();
    }

    /**
     * 构建GridView的适配器
     *
     * @param data
     */
    private void setImageAdapter(ArrayList<ImageGroupBean> data) {
        if (data == null || data.size() == 0) {
            mLoadingLayout.showEmpty(getString(R.string.no_images));
        }
        mGroupAdapter = new ImageGroupAdapter(this, data, mGroupImagesGv);
        mGroupImagesGv.setAdapter(mGroupAdapter);
        mGroupImagesGv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageGroupBean imageGroup = mGroupAdapter.getItem(position);
        if (imageGroup == null) {
            return;
        }
        ArrayList<String> childList = imageGroup.getImages();
        boolean isFromChat = getIntent().getBooleanExtra(EXTRA_FROM_CHAT, false);
        ImageListActivity.launch(PhotoAllActivity.this, imageGroup.getDirName(),
                isFromChat, childList, ImageListActivity.LaunchEnum.clickLaunch);
    }

    public void onEventMainThread(final RefreshImageListEvent event) {
        finish();
    }
//
//    public void onEventMainThread(final ChatGetSendPhotoSuccess event) {
//        finish();
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.getInstance().getCommonEventBus().unregister(PhotoAllActivity.this);
        EventBusUtil.getInstance().getMessageEventBus().unregister(PhotoAllActivity.this);
    }
}


