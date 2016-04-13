package wechar.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import MyView.CircleImageView;
import bruse.com.bruseapp.R;
import wechar.model.MessageModel;

/**
 * Created by bruse on 16/4/11.
 */
public class MessageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MessageModel> data;
    private LayoutInflater inflater;

    public MessageAdapter(Context context, ArrayList<MessageModel> data) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.news_items_activity, null);
            holder.iv_left = (CircleImageView) convertView.findViewById(R.id.ivLeft);
            holder.iv_right = (CircleImageView) convertView.findViewById(R.id.ivRight);
            holder.tv_left = (TextView) convertView.findViewById(R.id.leftMessage);
            holder.tv_right = (TextView) convertView.findViewById(R.id.rightMessage);
            holder.left = (ViewGroup) convertView.findViewById(R.id.left);
            holder.right = (ViewGroup) convertView.findViewById(R.id.right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (data.get(position).getFrom().equals("bruse@125.65.82.216")) {
            holder.right.setVisibility(View.VISIBLE);
            holder.tv_right.setText(data.get(position).getMessage());
            holder.left.setVisibility(View.GONE);
        } else {
            Log.i("asmack", data.get(position).getFrom());
            holder.left.setVisibility(View.VISIBLE);
            holder.tv_left.setText(data.get(position).getMessage());
            holder.right.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        CircleImageView iv_left;
        CircleImageView iv_right;
        TextView tv_left;
        TextView tv_right;
        ViewGroup left;
        ViewGroup right;
    }
}
