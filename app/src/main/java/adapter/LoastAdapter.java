package adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import MyView.NoScrollGridView;
import bruse.com.bruseapp.R;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import constant.Constants;
import datamodel.ImagePost;
import datamodel.Lost;
import util.ImageLoaderWrapper;

/**
 * Created by bruse on 16/2/27.
 */
public class LoastAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<Lost> data;
    private LayoutInflater inflater;
    private BitmapUtils bitmapUtils;

    //关于grideview的适配器
    private List<ImagePost> imagePostArrayList;
    private GrideImageAdapter grideImageAdapter;
    //
    private ImageLoaderWrapper.DisplayConfig mConfig = new ImageLoaderWrapper.DisplayConfig.Builder().build();


    public LoastAdapter(Activity context, ArrayList<Lost> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        bitmapUtils = new BitmapUtils(context);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        imagePostArrayList = new ArrayList<>();

        if (view == null) {
            view = inflater.inflate(R.layout.lv_data_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.lv_item_title);
            viewHolder.describe = (TextView) view.findViewById(R.id.lv_item_des);
            viewHolder.phone = (TextView) view.findViewById(R.id.lv_item_phone);
            viewHolder.time = (TextView) view.findViewById(R.id.lv_item_time);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.item_head);
            viewHolder.gridView = (NoScrollGridView) view.findViewById(R.id.lv_item_imgs);
            //
            viewHolder.layoutlayout= (ViewGroup) view.findViewById(R.id.image_lay);
            viewHolder.imageView1= (ImageView) view.findViewById(R.id.image_item_one);
            viewHolder.imageView2= (ImageView) view.findViewById(R.id.image_item_two);
            viewHolder.imageView3= (ImageView) view.findViewById(R.id.image_item_three);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        viewHolder.title.setText(data.get(position).getTitle());
        viewHolder.describe.setText(data.get(position).getDescribe());
        viewHolder.phone.setText(data.get(position).getPhone());
        viewHolder.time.setText(data.get(position).getCreatedAt());
        if (data.get(position).getImageone()==null){
            viewHolder.layoutlayout.setVisibility(View.GONE);
        }else {
            viewHolder.layoutlayout.setVisibility(View.VISIBLE);
        }

        ImageLoaderWrapper.getDefault().displayImage(data.get(position).getImageone(),viewHolder.imageView1, mConfig);
        ImageLoaderWrapper.getDefault().displayImage(data.get(position).getImagetwo(),viewHolder.imageView2, mConfig);
        ImageLoaderWrapper.getDefault().displayImage(data.get(position).getImagethree(),viewHolder.imageView3, mConfig);
        /**
         *显示图像
         * */
        imagePostArrayList = searchImage(data.get(position).getUser(), data.get(position).getTime());
        Log.i("position", position + "行" + data.get(position).getTime());
        viewHolder.gridView.setAdapter(grideImageAdapter);
        viewHolder.gridView.setVisibility(View.GONE);
        bitmapUtils.display(viewHolder.imageView, data.get(position).getHead_url());

        return view;
    }

    class ViewHolder {
        TextView title;
        TextView describe;
        TextView phone;
        TextView time;
        ImageView imageView;
        NoScrollGridView gridView;
        //下面的图片展示
        ViewGroup layoutlayout;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;

    }

    private boolean found = true;

    /**
     * 在这里进行查询上传的图片
     * 用户名和时间
     */
    public List<ImagePost> searchImage(String require1, String require2) {
        final List<ImagePost> datalist = new ArrayList<>();
        BmobQuery<ImagePost> bmobQuery = new BmobQuery<ImagePost>();

        //加上条件
        bmobQuery.addWhereEqualTo("time", require2);
        //设置返回数据默认是十条,提交多少图片返回多少
        bmobQuery.setLimit(3);
        //执行查找
        bmobQuery.findObjects(context, new FindListener<ImagePost>() {
            @Override
            public void onSuccess(List<ImagePost> list) {
                if (list == null && list.size() == 0) {
                    found = false;
                    return;
                }

                if (list != null && list.size() != 0) {
                    imagePostArrayList.clear();
                    imagePostArrayList = list;
                    Log.i("position", +list.size() + "");
                }

                Log.i("数据URL", imagePostArrayList.size() + "");
                grideImageAdapter = new GrideImageAdapter(context, imagePostArrayList);

            }

            @Override
            public void onError(int i, String s) {
                Log.i("数据", "失败");
            }
        });
        return imagePostArrayList;

    }

}
