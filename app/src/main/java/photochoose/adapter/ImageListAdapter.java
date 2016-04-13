package photochoose.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;

import MyView.MyImageView;
import bruse.com.bruseapp.R;
import util.Bimp;
import util.ImageLoader;
import util.ImageLoaderWrapper;
import util.ImageUrlUtils;

/**
 * Created by bruse on 16/3/6.
 */
public class ImageListAdapter extends BaseAdapter {
    //上下文对象
    private Activity mContext = null;
    //图片列表
    private ArrayList<String> mDataList = new ArrayList<String>();
    //容器
    private View mContainer = null;
    private ImageLoaderWrapper.DisplayConfig mConfig = new ImageLoaderWrapper.DisplayConfig.Builder().build();

    //
    private BitmapUtils bitmapUtils;

    public ImageListAdapter(Activity context, ArrayList<String> list, View container) {
        mDataList = list;
        mContext = context;
        mContainer = container;
        mConfig.stubImageRes = R.drawable.pic_thumb;

        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public String getItem(int position) {
        if (position < 0 || position > mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
//            if(position==0){
//                view = LayoutInflater.from(mContext).inflate(R.layout.image_list_take_photo_item, null);
//                holder.mImageIv = (MyImageView) view.findViewById(R.id.list_item_iv);
//                view.setTag(holder);
//            }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.image_list_item, null);
            holder.mImageIv = (MyImageView) view.findViewById(R.id.list_item_iv);
            holder.mClickArea = view.findViewById(R.id.list_item_cb_click_area);
            holder.mSelectedCb = (CheckBox) view.findViewById(R.id.list_item_cb);
            view.setTag(holder);
//            }
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (position == 0) {
            holder.mImageIv.setImageResource(R.mipmap.picturebar_camera_icon);
            holder.mClickArea.setVisibility(View.GONE);
            holder.mSelectedCb.setVisibility(View.GONE);
//            holder.mImageIv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EventBusUtil.getInstance().getCommonEventBus().post(new TakePhotoEvent());
//                    mContext.finish();
//                }
//            });
        } else {
            final String path = getItem(position);
            // 加载图片
            try {
                ImageLoaderWrapper.getDefault().displayImage(ImageUrlUtils.getDisplayUrl(path), holder.mImageIv, mConfig);
//                bitmapUtils.display(holder.mImageIv, path);

            } catch (Exception e) {
                LogUtils.e("加载图片OOM");
            }
            holder.mSelectedCb.setChecked(false);
            // 该图片是否选中
            for (String selected : Bimp.mSelectedList) {
                if (selected.equals(path)) {
                    holder.mSelectedCb.setChecked(true);
                }
            }
            //如果是单选的话就不显示选择框
            if (Bimp.max == 1) {
                holder.mSelectedCb.setVisibility(View.GONE);
            } else {
                holder.mSelectedCb.setVisibility(View.VISIBLE);
            }

            // 可点区域单击事件
            holder.mClickArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = holder.mSelectedCb.isChecked();
                    if (!checked) {
                        if (Bimp.mSelectedList.size() >= Bimp.max) {
                            Toast.makeText(mContext, " 您最多可以选择" + Bimp.max + "张图片 ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        addImage(path);
                    } else {
                        deleteImage(path);
                    }
                    holder.mSelectedCb.setChecked(!checked);
                    System.out.println("现在图片： " + Bimp.max + "/" + Bimp.mSelectedList.size());
                }
            });
        }
        return view;
    }
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode == mContext.RESULT_OK) {
//            switch (requestCode) {
//                case Crop.REQUEST_CAMERA: //拍照
//                    Uri uri = CropWrapper.onActivityResult(mContext, BaseApplication.app.getCropTmpDir(), requestCode, resultCode, data);
//                    if(uri!=null){
//                        Bimp.mSelectedList.add(uri + "");
//                        ToastUtils.show(mContext,"图片地址："+uri);
//                    }
//
//                    break;
//            }
//        }
//
//    }

    /**
     * 将图片地址添加到已选择列表中
     *
     * @param path
     */
    private void addImage(String path) {
        if (Bimp.mSelectedList.contains(path)) {
            return;
        }
        Bimp.mSelectedList.add(path);
    }

    /**
     * 将图片地址从已选择列表中删除
     *
     * @param path
     */
    private void deleteImage(String path) {
        Bimp.mSelectedList.remove(path);
    }

    /**
     * 获取已选中的图片列表
     *
     * @return
     */
    public ArrayList<String> getSelectedImgs() {
        return Bimp.mSelectedList;
    }

    static class ViewHolder {
        public MyImageView mImageIv;

        public View mClickArea;

        public CheckBox mSelectedCb;

    }

}
