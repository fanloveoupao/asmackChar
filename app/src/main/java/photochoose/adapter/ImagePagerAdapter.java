package photochoose.adapter;

/**
 * Created by bruse on 16/3/6.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import bruse.com.bruseapp.R;
import event.BimpSyncEvent;
import event.EventBusUtil;
import photochoose.ImageBrowseActivity;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import util.Bimp;
import util.ImageLoaderWrapper;
import util.ImageUrlUtils;

/**
 * 查看大图的ViewPager适配器
 *
 * @author likebamboo
 */
public class ImagePagerAdapter extends PagerAdapter {
    /**
     * 数据源
     */
    private List<String> mDatas = new ArrayList<String>();

    private ImageBrowseActivity.LaunchEnum mLaunchEnum = ImageBrowseActivity.LaunchEnum.noSelected;

    /**
     * UIL的ImageLoader
     */
    private ImageLoader mImageLoader = ImageLoader.getInstance();

    private ImageLoaderWrapper.DisplayConfig mConfig = new ImageLoaderWrapper.DisplayConfig.Builder().build();

//    /**
//     * 显示参数
//     */
//    private DisplayImageOptions mOptions = null;

    private Activity mActivity;

    private BitmapUtils bitmapUtils;
//    private BusinessHandlerActivity mBusinessHandlerActivity;

    public ImagePagerAdapter(Activity activity, ArrayList<String> dataList, ImageBrowseActivity.LaunchEnum launchEnum) {
        mDatas = dataList;
        mActivity = activity;
        mLaunchEnum = launchEnum;
        mConfig.stubImageRes = R.drawable.pic_thumb;
        mConfig.loadFailImageRes = R.mipmap.icon_pic_errow;
//        mBusinessHandlerActivity = new BusinessHandlerActivity(activity);
        bitmapUtils = new BitmapUtils(activity);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public View instantiateItem(final ViewGroup container, int position) {
        String imgPath = (String) getItem(position);
        //格式化一下路径
        final String transFormPath = ImageUrlUtils.getDisplayUrl(imgPath);
        if (ImageUrlUtils.isHttpPath(transFormPath)) {
            return handleHttpImage(transFormPath, container);
        } else {
            return handleLocalImage(imgPath, transFormPath, container);
        }

    }

    private View handleLocalImage(final String imgPath, final String transFormPath, final ViewGroup container) {
        final RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager_photoview, null);
        final PhotoView photoView = (PhotoView) relativeLayout.findViewById(R.id.pv_big_image);
        final CheckBox checkBox = (CheckBox) relativeLayout.findViewById(R.id.cb_image);

        photoView.setBackgroundColor(container.getContext().getResources().getColor(android.R.color.black));
        bitmapUtils.display(photoView, transFormPath);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                mActivity.finish();
            }
        });
        // Now just add PhotoView to ViewPager and return it
        container.addView(relativeLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return relativeLayout;
    }

    private View handleHttpImage(final String transFormPath, final ViewGroup container) {
        final RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager, null);
        final RelativeLayout smallImageLayout = (RelativeLayout) relativeLayout.findViewById(R.id.rv_small_image_layout);
        final ImageView smallImage = (ImageView) relativeLayout.findViewById(R.id.iv_samll_image);
        final PhotoView photoView = (PhotoView) relativeLayout.findViewById(R.id.pv_big_image);

        //大图的listener
        final ImageLoadingListener bigLoadingListener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                loadFailed(photoView, smallImageLayout);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                smallImageLayout.setVisibility(View.GONE);
                photoView.setVisibility(View.VISIBLE);
                photoView.setImageBitmap(loadedImage);
//                if (!loadedImage.isRecycled()) {
//                    setLongClickListener(photoView, loadedImage);
//                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        };
        //小图的listener
        final ImageLoadingListener smallLoadingListener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                loadFailed(photoView, smallImage);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                smallImage.setImageBitmap(loadedImage);
                mImageLoader.loadImage(ImageUrlUtils.getBigImageUrl(transFormPath), bigLoadingListener);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                loadFailed(photoView, smallImage);
            }
        };
        ImageLoaderWrapper.getDefault().displayImage(ImageUrlUtils.getSmallImageUrl(transFormPath), smallImage);
        ImageLoaderWrapper.getDefault().loadImage(ImageUrlUtils.getBigImageUrl(transFormPath) + "@480w_800h_100Q", bigLoadingListener);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                mActivity.finish();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        // Now just add PhotoView to ViewPager and return it
        container.addView(relativeLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return relativeLayout;
    }

//    private void setLongClickListener(PhotoView photoView, final Bitmap loadedImage) {
//        photoView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (loadedImage != null) {
//                    Result result = ZxingUtil.decodeFromBitmap(loadedImage, false);
//                    if (loadedImage.isRecycled()) {
//                        ToastUtils.show(mActivity, "isRecycled");
//                        return false;
//                    }
//                    if (result != null) { //图片包含二维码图片
//                        showQRImageOpDialog(loadedImage, result);
//                    } else {
//                        showImageOpDialog(loadedImage);
//                    }
//                }
//                return false;
//            }
//        });
//    }
//
//    /**
//     * 弹出图片是二维码操作对话框
//     */
//    private void showQRImageOpDialog(final Bitmap bitmap, final Result result) {
//        String[] mQRImageOp = {"发送给好友", "保存图片", "识别图中的二维码"};
//        DialogUtils.showList(mActivity, null, mQRImageOp, new MaterialDialog.ListCallback() {
//            @Override
//            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
//                switch (i) {
//                    case 0: //发送给好友
//                        if (bitmap != null) {
//                            MessageModel messageModel = new MessageModel();
//                            messageModel.isSend = MessageModel.ME_TO_OTHER;
//                            messageModel.status = messageModel.STATUE_SEND_FAIL;
//                            messageModel.type = ModelFileds.MESSAGE_TYPE_IMAGE;
//                            messageModel.msg = BitmapUtils.saveBitmap2file(bitmap).getAbsolutePath();
//                            SingleChooseFriendActivity.launch(mActivity, messageModel);
//                            mActivity.finish();
//                        } else {
//                            ToastUtils.show(mActivity, "发送失败...");
//                        }
//                        break;
//                    case 1: //保存图片
//                        try {
//                            QRCodeFileUtils.saveImageToGallery(mActivity, bitmap);
//                        } catch (Exception e) {
//                            ToastUtils.show(mActivity, "保存失败...");
//                            e.printStackTrace();
//                        }
//                        break;
//                    case 2: //识别图中的二维码
//                        mBusinessHandlerActivity.businessHandlerMethod(result.getText());
//                        if (!mBusinessHandlerActivity.isTgnetURL()) {
//                            if (mBusinessHandlerActivity.isURL()) {//判断是否是URL
//                                showUrlDialog(result.getText());
//                            } else {
//                                showDialog(result.getText());
//                            }
//                        }
//                        break;
//                }
//            }
//        });
//
//    }
//
//    /**
//     * 扫描结果对话框
//     *
//     * @param msg
//     */
//    private void showDialog(final String msg) {
//        DialogUtils.show(mActivity, "扫描结果", "非天工网业务圈的二维码\n内容：" + msg, "复制", "返回", new MaterialDialog.ButtonCallback() {
//            @Override
//            public void onPositive(MaterialDialog dialog) {
//                super.onPositive(dialog);
//                // 获取剪贴板管理服务
//                ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
//                // 将文本数据复制到剪贴板
//                cm.setText(msg);
//                ToastUtils.show(mActivity, "复制成功");
//            }
//
//            @Override
//            public void onNegative(MaterialDialog dialog) {
//                super.onNegative(dialog);
//                DialogUtils.dismiss();
//            }
//        });
//    }
//
//
//    private void showUrlDialog(final String url) {
//        DialogUtils.show(mActivity, "扫描结果", "非业务圈的网址:\n" + url, "打开", "返回", new MaterialDialog.ButtonCallback() {
//            @Override
//            public void onPositive(MaterialDialog dialog) {
//                super.onPositive(dialog);
//                WebViewActivity.launch(mActivity, url, null);
//                mActivity.finish();
//            }
//
//            @Override
//            public void onNegative(MaterialDialog dialog) {
//                super.onNegative(dialog);
//                DialogUtils.dismiss();
//            }
//        });
//
//    }
//
//    /**
//     * 弹出图片不是二维码操作对话框
//     */
//    private void showImageOpDialog(final Bitmap bitmap) {
//        String[] mQRImageOp = {"发送给好友", "保存图片"};
//        DialogUtils.showList(mActivity, null, mQRImageOp, new MaterialDialog.ListCallback() {
//            @Override
//            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
//                switch (i) {
//                    case 0: //发送给好友
//                        if (bitmap != null) {
//                            MessageModel messageModel = new MessageModel();
//                            messageModel.isSend = MessageModel.ME_TO_OTHER;
//                            messageModel.status = messageModel.STATUE_SEND_FAIL;
//                            messageModel.type = ModelFileds.MESSAGE_TYPE_IMAGE;
//                            messageModel.msg = BitmapUtils.saveBitmap2file(bitmap).getAbsolutePath();
//                            SingleChooseFriendActivity.launch(mActivity, messageModel);
//                            mActivity.finish();
//                        } else {
//                            ToastUtils.show(mActivity, "发送失败...");
//                        }
//                        break;
//                    case 1: //保存图片
//                        try {
//                            QRCodeFileUtils.saveImageToGallery(mActivity, bitmap);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            ToastUtils.show(mActivity, "保存失败...");
//                        }
//                        break;
//                }
//            }
//        });
//    }


    private void loadFailed(ImageView big, View small) {
        small.setVisibility(View.GONE);
        big.setVisibility(View.VISIBLE);
        big.setImageResource(R.mipmap.icon_pic_errow);
    }

    public Object getItem(int position) {
        if (position < mDatas.size()) {
            return mDatas.get(position);
        } else {
            return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
