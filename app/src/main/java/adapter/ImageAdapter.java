package adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import MyView.MyImageView;
import bruse.com.bruseapp.R;
import constant.Constants;
import util.ImageLoader;

/**
 * Created by bruse on 16/3/3.
 */
public class ImageAdapter extends BaseAdapter {
    private boolean isSelecter = false;


    public static Set<String> mSeletedImg = new HashSet<>();
    private String mDirPath;
    private List<String> mImgPath;
    private LayoutInflater mInflater;
    private int count = 0;

    public ImageAdapter(Context context, List<String> mDatas, String dirPath) {
        this.mDirPath = dirPath;
        this.mImgPath = mDatas;
        mInflater = LayoutInflater.from(context);
        mSeletedImg.clear();
    }

    public  Set<String> getmSeletedImg() {
        return mSeletedImg;
    }

    @Override
    public int getCount() {
        if (mImgPath != null)
            return mImgPath.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (position < 0 || position > mImgPath.size()) {
            return null;
        }
        return mImgPath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (parent.getChildCount() == position) {
        Log.i("position", position + "");
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.image_list_item, null);
            viewHolder.mImageIv = (MyImageView) convertView.findViewById(R.id.list_item_iv);
            viewHolder.mClickArea = convertView.findViewById(R.id.list_item_cb_click_area);
            viewHolder.mSelectedCb = (CheckBox) convertView.findViewById(R.id.list_item_cb);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //第一个为拍照
        if (position == -1) {
            viewHolder.mImageIv.setImageResource(R.mipmap.picturebar_camera_icon);
            viewHolder.mClickArea.setVisibility(View.GONE);
            viewHolder.mSelectedCb.setVisibility(View.GONE);

        } else {
            Log.w("logger", "getView");
            //重置状态
            viewHolder.mSelectedCb.setChecked(false);
            final String filepath = mDirPath + "/" + mImgPath.get(position);

            if (mSeletedImg.contains(filepath)) {
                viewHolder.mSelectedCb.setChecked(true);

            }
            /**
             * 头像的选择只能一个
             * */

            viewHolder.mClickArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w("logger", "you click the file" + filepath);
                    Log.i("TAG", Constants.CHOOSER);
                    Constants.HEAD_URL = filepath;
                    //已经被选择
                    if (mSeletedImg.contains(filepath)) {
                        mSeletedImg.remove(filepath);
                        viewHolder.mSelectedCb.setChecked(false);
                    } else {
                        if ("头像选择".equals(Constants.CHOOSER)) {
                            if (mSeletedImg.size() == 0) {
                                mSeletedImg.add(filepath);
                                viewHolder.mSelectedCb.setChecked(true);
                                //
                                Iterator iterator = mSeletedImg.iterator();
                                while (iterator.hasNext()) {
                                    Log.i("数组", (String) iterator.next());
                                }
//
                            }
                        } else {
                            mSeletedImg.add(filepath);
                            viewHolder.mSelectedCb.setChecked(true);

                        }
                    }
                }
            });


            ImageLoader.getInstance(3, ImageLoader.Type.FIFO).loadImage(mDirPath + "/" + mImgPath.get(position), viewHolder.mImageIv);
        }
//        }
        return convertView;
    }

    private class ViewHolder {
        public MyImageView mImageIv;

        public View mClickArea;

        public CheckBox mSelectedCb;
    }

}

