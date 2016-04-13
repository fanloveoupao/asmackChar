package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import bruse.com.bruseapp.R;
import datamodel.ImagePost;
import util.ImageLoaderWrapper;

/**
 * Created by bruse on 16/3/13.
 */
public class GrideImageAdapter extends BaseAdapter {
    private BitmapUtils bitmapUtils;
    private List<ImagePost> data;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoaderWrapper.DisplayConfig mConfig = new ImageLoaderWrapper.DisplayConfig.Builder().build();

    public GrideImageAdapter(Context context, List<ImagePost> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        bitmapUtils = new BitmapUtils(context);
        Log.i("dataurl", data.size() + "测试");
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.gride_image_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.id_image_gride);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (data.size() > 0) {

            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        //显示图片
//        bitmapUtils.display(holder.imageView, data.get(position).getUrl());

        Log.i("imageView", data.get(position).getUrl());
        ImageLoaderWrapper.getDefault().displayImage(data.get(position).getUrl(), holder.imageView, mConfig);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
