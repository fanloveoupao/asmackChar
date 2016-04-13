package bruse.ui;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import base.BaseActivity;
import bruse.com.bruseapp.R;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import datamodel.Lost;

import static android.view.Gravity.*;

public class DataDetailActivity extends BaseActivity implements View.OnClickListener {


    public static void launch(Activity activity, String extra, int requestCode) {
        Intent intent = new Intent(activity, DataDetailActivity.class);
        intent.putExtra("objectId", extra);
        activity.startActivityForResult(intent, requestCode);

    }

    private EditText dt_title;
    private EditText dt_describe;
    private TextView dt_time;
    private EditText dt_phone;
    private TextView tv_phone;
    private String objectId;
    private RelativeLayout layout_action;

    /**
     * 底部弹出的定义
     */
    private Button pop_detail;
    private Button pop_delete;
    private Button pop_cancel;

    /**
     * 底部菜单的按钮
     */
    private Button btn_edit_cancel;
    private Button btn_edit_ok;

    private RelativeLayout detail_bottom_layout;
    /**
     * 导航栏想按钮
     */

    private Button btn_detail_back;
    private Button btn_detail;

    @Override
    public void setContentViews() {
        setContentView(R.layout.activity_data_detail);
    }

    @Override
    public void initViews() {
        dt_title = (EditText) findViewById(R.id.tv_detail_title);
        dt_describe = (EditText) findViewById(R.id.tv_detail_des);
        dt_phone = (EditText) findViewById(R.id.tv_detail_phone);
        dt_phone.setVisibility(View.GONE);
        tv_phone = (TextView) findViewById(R.id.text_phone);
        tv_phone.setVisibility(View.VISIBLE);
        dt_time = (TextView) findViewById(R.id.tv_detail_time);
        btn_detail_back = (Button) findViewById(R.id.detail_top_back);
        btn_detail = (Button) findViewById(R.id.detail_top_edit);

        layout_action = (RelativeLayout) findViewById(R.id.edit_layout);

        inflater = LayoutInflater.from(getApplicationContext());
        pop_view = inflater.inflate(R.layout.edit_pop_view, null);

        pop_detail = (Button) pop_view.findViewById(R.id.pop_edit);

        pop_delete = (Button) pop_view.findViewById(R.id.pop_delete);

        pop_cancel = (Button) pop_view.findViewById(R.id.pop_cancel);

        detail_bottom_layout = (RelativeLayout) findViewById(R.id.detail_bottom_layout);

        btn_edit_cancel = (Button) findViewById(R.id.edit_cancel);

        btn_edit_ok = (Button) findViewById(R.id.edit_ok);
    }

    @Override
    public void initListeners() {
        btn_detail_back.setOnClickListener(this);
        btn_detail.setOnClickListener(this);

        pop_detail.setOnClickListener(this);
        pop_delete.setOnClickListener(this);
        pop_cancel.setOnClickListener(this);

        btn_edit_cancel.setOnClickListener(this);

        btn_edit_ok.setOnClickListener(this);

    }

    @Override
    public void initDatas() {
        objectId = getIntent().getStringExtra("objectId");
        Log.i("id", objectId);
        queryObject();
    }

    /**
     * 导航栏的点击事件
     */

    private String event = "";

    /**
     * 是删除事件还是编辑事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_top_back:
                finish();
                break;
            case R.id.detail_top_edit:
                getPopWindows();
                break;
            case R.id.pop_cancel:
                closePopWindow();
                break;
            case R.id.pop_delete:
                closePopWindow();
                /**
                 * 显示底部的菜单
                 * */
                showBottom();
                event = "删除";
                break;
            case R.id.pop_edit:
                closePopWindow();
                showBottom();
                event = "编辑";
                tv_phone.setVisibility(View.GONE);
                dt_phone.setVisibility(View.VISIBLE);
                editTextAble();
                break;
            /**
             * 底部菜单的点击事件
             * */
            case R.id.edit_cancel:
                event = "";
                cancelEdit();
                break;
            case R.id.edit_ok:
                setResult(RESULT_OK);
                eventListener(event);
                break;
        }
    }

    /**
     * 查找一个指定数据
     */
    public void queryObject() {
        //查找表lost的指定一个id的数值
        BmobQuery<Lost> query = new BmobQuery<>();
        query.getObject(getApplicationContext(), objectId, new GetListener<Lost>() {
            @Override
            public void onSuccess(Lost lost) {
                dt_title.setText(lost.getTitle().trim());
                dt_describe.setText(lost.getDescribe().trim());
                dt_time.setText(lost.getCreatedAt().trim());
                dt_phone.setText(lost.getPhone().trim());
                tv_phone.setText(lost.getPhone().trim());
//                editTextEnable();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "查询错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 弹出菜单
     */
    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View pop_view;

    /**
     * 创建popupWindow
     */

    public void initPopupWindow() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        popupWindow = new PopupWindow(pop_view, mScreenWidth, 600, true);
        //设置动画效果
        popupWindow.setAnimationStyle(R.style.PopAnim);
        //背景变暗
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        popupWindow.showAsDropDown(layout_action, 0, -600);
        //点击其它地方消失
        pop_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                closePopWindow();
                return false;
            }
        });

    }

    /**
     * 获取popWindow的实例
     */

    public void getPopWindows() {
        if (null != popupWindow && popupWindow.isShowing()) {
            closePopWindow();
            return;
        }
        initPopupWindow();
    }

    public void closePopWindow() {

        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1.0f;
            getWindow().setAttributes(params);
        }
    }

    /**
     * 关于popWindow中的点击事件处理
     */
    public void showBottom() {
        Animation animation = new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.bottom_detail_in);
        detail_bottom_layout.startAnimation(animation);
        detail_bottom_layout.setVisibility(View.VISIBLE);
        btn_detail.setEnabled(false);
    }

    public void closeBottom() {
        Animation animation = new AnimationUtils().loadAnimation(getApplicationContext(), R.anim.bootom_detail_out);
        detail_bottom_layout.startAnimation(animation);
        detail_bottom_layout.setVisibility(View.GONE);
        btn_detail.setEnabled(true);
    }

    /**
     * 底部菜单的点击事件
     */
    public void cancelEdit() {
        closeBottom();
    }

    /**
     * 删除和编辑事件的处理
     */

    public void eventListener(String event) {
        if ("删除".equals(event)) {
            delete(objectId);
            closeBottom();
        }
        if ("编辑".equals(event)) {

            edit(objectId);
            closeBottom();
            editTextNoenable();
        }
    }

    /**
     * 编辑框
     */
    public void editTextAble() {
        dt_title.setFocusable(true);
        dt_describe.setFocusable(true);
        dt_title.setEnabled(true);
        dt_describe.setEnabled(true);

    }

    public void editTextNoenable() {
        dt_describe.setFocusable(false);
        dt_title.setFocusable(false);

    }

    /**
     * 删除
     */
    public void delete(String objectId) {
        Lost lost = new Lost();
        lost.setObjectId(objectId);
        lost.delete(getApplicationContext(), new DeleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "删除失败" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 编辑
     */
    public void edit(String objectId) {

        Lost lost = new Lost();
        lost.setPhone(dt_phone.getText().toString().trim());
        lost.setTitle(dt_title.getText().toString().trim());
        lost.setDescribe(dt_describe.getText().toString().trim());
        lost.update(getApplicationContext(), objectId, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "编辑成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "编辑失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
