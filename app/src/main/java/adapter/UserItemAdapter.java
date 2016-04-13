package adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bruse.com.bruseapp.R;
import datamodel.UserItem;

/**
 * Created by bruse on 16/3/3.
 */
public class UserItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UserItem> data;
    private LayoutInflater inflater;

    public UserItemAdapter(Context context, ArrayList<UserItem> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.user_item_listview, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.user_item_img);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.user_item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (data.get(position).getImgId()!=null){
//            viewHolder.imageView.setImageURI(Uri.fromFile(data.get(position).getImgId()));
        }

        viewHolder.textView.setText(data.get(position).getItem());
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
