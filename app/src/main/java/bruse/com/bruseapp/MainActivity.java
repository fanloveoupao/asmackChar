package bruse.com.bruseapp;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import adapter.LoastAdapter;
import adapter.UserItemAdapter;
import base.BaseActivity;
import bruse.ui.AddActivity;
import bruse.ui.DataDetailActivity;
import bruse.ui.ImageChooseActivity;
import bruse.ui.LoginActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import constant.Constants;
import datamodel.Lost;
import datamodel.UserItem;
import de.greenrobot.event.EventBus;
import event.EventBusUtil;
import event.ImageEvent;
import event.MainRefresh;
import user.MyUser;
import util.Bimp;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button btn_add;
    private PullToRefreshListView lv_data;
    private ArrayList<Lost> data;
    private LoastAdapter loastAdapter;
    private View view;
    private ImageView img_head;

    //滑动的头像

    private ImageView sliding_header;

    public static void launch(Activity activity) {

        activity.startActivity(new Intent(activity, MainActivity.class));

    }

    @Override
    public void setContentViews() {
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        createSlidingMenu();

    }

    @Override
    public void initViews() {
        view = findViewById(R.id.include_top);
        btn_add = (Button) findViewById(R.id.top_add);
        lv_data = (PullToRefreshListView) findViewById(R.id.lv_data);
        data = new ArrayList<Lost>();
        loastAdapter = new LoastAdapter(MainActivity.this, data);
        lv_data.setAdapter(loastAdapter);

        img_head = (ImageView) findViewById(R.id.user_head_top);

        user_list = (ListView) findViewById(R.id.user_model_list);

        sliding_header = (ImageView) findViewById(R.id.user_img_header);
    }

    @Override
    public void initListeners() {
        btn_add.setOnClickListener(this);
        lv_data.setOnItemClickListener(this);

        img_head.setOnClickListener(this);

        userItemListener();
        sliding_header.setOnClickListener(this);
    }

    @Override
    public void initDatas() {
        queryData();
        refresh();
        //
        initUserItem();
        headDownload();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        /**
         * 回调这一点很重要
         * */
        switch (requestCode) {
            case Constants.REQUESTCODE_ADD:
                queryData();
                break;
            case Constants.REQUESTCODE_DETAIL:
                queryData();
                break;
            case Constants.REQUEST_CHOOSE:
                headDownload();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_add:
                startActivityForResult(new Intent(MainActivity.this, AddActivity.class), Constants.REQUESTCODE_ADD);
                break;
            case R.id.user_head_top:
                menu.showMenu();
                break;
            case R.id.user_img_header:
                ImageChooseActivity.launch(MainActivity.this, Constants.REQUEST_CHOOSE);
                Constants.CHOOSER = "头像选择";
                break;
        }

    }

    /**
     * 点击item进行展开
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String indexId = data.get(i).getObjectId();
        DataDetailActivity.launch(MainActivity.this, indexId, Constants.REQUESTCODE_DETAIL);

    }

    /**
     * 数据的查询
     */
    public void queryData() {
        headDownload();
        BmobQuery<Lost> query = new BmobQuery<>();
        query.order("-updatedAt");
        query.findObjects(getApplicationContext(), new FindListener<Lost>() {
            @Override
            public void onSuccess(List<Lost> list) {
                if (list == null || list.size() == 0) {
                    loastAdapter.notifyDataSetChanged();
                    return;
                }
                //把之前的清理掉
                data.clear();
                data.addAll(list);
                lv_data.setAdapter(loastAdapter);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 关于slidingmenu的问题
     */
    private SlidingMenu menu;

    public void createSlidingMenu() {
        menu = new SlidingMenu(getApplicationContext());
        /**
         * 设置slidingMenu的一些属性
         * */
        menu.setMode(SlidingMenu.LEFT);
        /**
         * 设置触摸的模式
         * */
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置滑动菜单距离右侧的距离
        menu.setBehindOffsetRes(R.dimen.sliding_menu_off_set);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //设置布局
        menu.setMenu(R.layout.user_sliding_menu);
        Animation animation = new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.bottom_detail_in);
        menu.setAnimation(animation);
    }

    /**
     * 有关下拉刷新
     */
    public void refresh() {
        lv_data.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                new GetDataTask(MainActivity.this).execute();

            }
        });
    }

    //选择完后回到主线程刷新UI
    public void onEventMainThread(MainRefresh event) {
        refresh();
    }

    private class GetDataTask extends AsyncTask<Void, Void, String> {
        private MainActivity activity;

        public GetDataTask(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String result) {
            // Call onRefreshComplete when the list has been refreshed.
            if (result.equals("success")) {
                activity.queryData();
                //刷新适配器

                loastAdapter.notifyDataSetChanged();
                //表示刷新完成
                lv_data.onRefreshComplete();

            }
            super.onPostExecute(result);
        }
    }

    /**
     * 侧滑菜单的选项
     *
     * @param 我的信息
     * @param 更改密码
     * @param 查询用户
     * @param 退出登录
     */
    private ListView user_list;
    private UserItemAdapter userItemAdapter;
    private ArrayList<UserItem> user_data;

    /**
     * 初始化
     */

    public void initUserItem() {
        user_data = new ArrayList<UserItem>();
        userItemAdapter = new UserItemAdapter(getApplicationContext(), user_data);
        initUserItenData();
        user_list.setAdapter(userItemAdapter);

    }

    public void userItemListener() {
        //用户列表的功能
        user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.user_item_text);
                switch (textView.getText().toString().trim()) {
                    case "退出登录":
                        LoginActivity.launch(MainActivity.this);
                        finish();
                        break;
                }
            }
        });
    }

    public void initUserItenData() {
        BmobQuery<UserItem> userItemBmobQuery = new BmobQuery<>();
        userItemBmobQuery.order("-updatedAt");
        userItemBmobQuery.findObjects(getApplicationContext(), new FindListener<UserItem>() {
            @Override
            public void onSuccess(List<UserItem> list) {
                if (list == null || list.size() == 0) {
                    userItemAdapter.notifyDataSetChanged();
                    return;
                }
                user_data.clear();
                user_data.addAll(list);
                userItemAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /**
     * 下载头像
     */
    public void headDownload() {
        MyUser userInfo = BmobUser.getCurrentUser(getApplicationContext(), MyUser.class);
        if (userInfo.getUserHead() != null) {
            Log.i("回调", "回调测试" + userInfo.getUserHead());
            BitmapUtils bitmapUtils = new BitmapUtils(getApplicationContext());
            //加载图片
            bitmapUtils.display(img_head, userInfo.getUserHead());
            bitmapUtils.display(sliding_header, userInfo.getUserHead());

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
