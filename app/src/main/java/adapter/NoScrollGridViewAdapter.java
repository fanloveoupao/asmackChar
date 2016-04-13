package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


import bruse.com.bruseapp.R;
import datamodel.ImagePost;
import photochoose.ImageBrowseActivity;
import tools.Constant;
import util.ImageLoaderWrapper;
import view.ImageCircleView;

/**
 * Created by bruse on 16/3/22.
 */
public class NoScrollGridViewAdapter extends BaseAdapter {

    private List<ImagePost> images;
    private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
    private Activity mActivity;
    private boolean mIs_add = false;
    private boolean mCanClick = true;
    private String mImg_w_h; // 缩略图的尺寸
    private ArrayList<String> images2;

    public void setContext(Activity context) {
        this.mActivity = context;
    }

    private BitmapUtils bitmapUtils;

    public NoScrollGridViewAdapter(Activity context) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.setContext(context);
        bitmapUtils = new BitmapUtils(context);
    }

    public NoScrollGridViewAdapter(Activity context, List<ImagePost> items, boolean is_add, String img_w_h) {
        super();
//        mImageConfig.loadFailImageRes = R.drawable.icon_pic_errow;
//        mImageConfig.stubImageRes = R.drawable.icon_pic_loding;
        this.mInflater = LayoutInflater.from(context);
        this.setContext(context);
        this.images = items;
        this.mIs_add = is_add;
        this.mImg_w_h = img_w_h;
        bitmapUtils = new BitmapUtils(context);
    }

    public NoScrollGridViewAdapter(Activity context, List<ImagePost> items, boolean is_add, boolean canClick, String img_w_h) {
        super();
        this.mCanClick = canClick;
        this.mInflater = LayoutInflater.from(context);
        this.setContext(context);
        this.images = items;
        this.mIs_add = is_add;
        this.mImg_w_h = img_w_h;
        for (int i = 0; i < items.size(); i++) {
            images2.add(items.get(i).getUrl());
        }
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public String getItem(int position) {
        return images.get(position).getUrl();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String imageUrl = images.get(position).getUrl();
        convertView = mInflater.inflate(
                R.layout.item_image, null);
        ImageCircleView imageView = (ImageCircleView) convertView.findViewById(R.id.item_image);
        if (mIs_add) {
            if (imageUrl.startsWith("file://")) {
                imageUrl = imageUrl.replace("file://", "");
            }
            bitmapUtils.display(imageView, imageUrl);
        } else {
            imageView.setImageURL(Constant.FOOTPRINT_IMGSBaseURL + imageUrl + mImg_w_h, R.color.background_gray_hand_in);
        }
        if (mCanClick) {
            //加上事件监听
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageBrowseActivity.launch(mActivity, images2, position);
                }
            });
        }
        return convertView;
    }

}
