package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import MyView.MyImageView;
import bruse.com.bruseapp.R;
import bruse.ui.ImageChooseActivity;
import datamodel.ImagePost;

/**
 * Created by bruse on 16/3/5.
 */
public class GridItemAdapter extends BaseAdapter {
    private Context context;
    ArrayList<ImagePost> data;
    private LayoutInflater inflater;
    private BitmapUtils bitmapUtils;
    private static int MAX = 6;

    public GridItemAdapter(Context context, ArrayList<ImagePost> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        bitmapUtils = new BitmapUtils(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_img_item, null);
            viewHolder.myImageView = (MyImageView) convertView.findViewById(R.id.grid_item_img);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.del_img_chek);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addImag();
            }
        });
        bitmapUtils.display(viewHolder.myImageView, data.get(position).getImgPath());
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImg(position);
            }
        });
        return convertView;
    }

    public void deleteImg(int position) {

    }

    public void addImag() {

    }

    class ViewHolder {
        MyImageView myImageView;
        CheckBox checkBox;
    }
}
