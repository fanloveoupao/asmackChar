package bruse.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.GetAccessUrlListener;
import com.bmob.btp.callback.UploadBatchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import MyView.Crop;
import MyView.CropWrapper;
import MyView.MyImageView;
import MyView.PictureBar;
import MyView.ToastUtils;
import adapter.GridItemAdapter;
import base.BaseActivity;
import base.BaseApplication;
import bruse.com.bruseapp.R;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import constant.Constants;
import datamodel.ImagePost;
import datamodel.Lost;
import de.greenrobot.event.EventBus;
import event.EventBusUtil;
import event.ImageEvent;
import event.MainRefresh;
import event.RefreshImageListEvent;
import event.TakePhotoEvent;
import photochoose.PhotoAllActivity;
import ui.ItemBean;
import user.MyUser;
import util.Bimp;

/**
 * Created by bruse on 16/2/27.
 */
public class AddActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_title;
    private EditText et_des;
    private EditText et_phone;
    private Button btn_add;
    private Button btn_back;
    //关于回调
    private EventBus eventBus;

    private static PictureBar mPictureBar;  //图片的选择

    private ProgressDialog progressDialog;
    private ArrayList<ItemBean> imageUrl;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, AddActivity.class);
        activity.startActivity(intent);

    }

    @Override
    public void setContentViews() {
        setContentView(R.layout.activity_add);

        eventBus = new EventBus();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void initViews() {
        et_title = (EditText) findViewById(R.id.ed_title);
        et_des = (EditText) findViewById(R.id.ed_des);
        et_phone = (EditText) findViewById(R.id.ed_phone);
        btn_add = (Button) findViewById(R.id.add_top_ok);
        btn_back = (Button) findViewById(R.id.add_top_back);
        progressDialog = new ProgressDialog(AddActivity.this);

        imageUrl = new ArrayList<ItemBean>();
        mPictureBar = (PictureBar) findViewById(R.id.picture_bar);
        //设置最多照片
        Bimp.setMax(3);
        mPictureBar.initData(AddActivity.this);

    }

    @Override
    public void initListeners() {
        btn_add.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        mPictureBar.setListener(AddActivity.this);
    }

    @Override
    public void initDatas() {


    }

    //选择完后回到主线程刷新UI
    public void onEventMainThread(ImageEvent event) {

        mPictureBar.refreshList();
        Log.i("eventbus", "eventsbus的回调" + Bimp.mSelectedList.get(0).toString());

    }

    //进行拍照
    public void onEventMainThread(TakePhotoEvent event) {

        Log.i("eventbus", "eventsbus的回调");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_top_back:
                finish();
                break;
            case R.id.add_top_ok:
                setResult(RESULT_OK);
                progressDialog.setTitle("开始上传");
//                uploadImages();
                uploadNewImages();
                Log.i("提交", "开始提交");

                break;

        }

    }

    /**
     * 数据的提交
     */
    String title = "";
    String describe = "";
    String phone = "";

    public void addLost(List<String> imageUrl) {
        title = et_title.getText().toString().trim();
        describe = et_des.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        final Lost lost = new Lost();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(describe)) {
            Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        MyUser userInfo = BmobUser.getCurrentUser(getApplicationContext(), MyUser.class);
        lost.setTitle(title);
        lost.setDescribe(describe);
        lost.setPhone(phone);
        lost.setUser(userInfo.getUsername());
        if (imageUrl.size() == 3) {
            lost.setImageone(imageUrl.get(0).toString());
            lost.setImagetwo(imageUrl.get(1).toString());
            lost.setImagethree(imageUrl.get(2).toString());

        } else if (imageUrl.size() == 2) {
            lost.setImageone(imageUrl.get(0).toString());
            lost.setImagetwo(imageUrl.get(1).toString());

        } else if (imageUrl.size() == 1) {
            lost.setImageone(imageUrl.get(0).toString());
        }
        Constants.UUINSTR = UUID.randomUUID().toString();
        lost.setTime(Constants.UUINSTR);
        if (userInfo.getUserHead() != null) {
            lost.setHead_url(userInfo.getUserHead());
        } else {
            lost.setHead_url("http://file.bmob.cn/M03/C6/B6/oYYBAFbXoeuAEMuEAABUNRxaB1g054.jpg");
        }
        lost.save(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                /**
                 * 在这里进行图片的提交
                 * */
                ToastUtils.show(getApplicationContext(), "提交成功");
            }
            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "提交失败" + s, Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * 进行回调
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUESTCODE_ADDIMG:
                break;
            case Crop.REQUEST_CAMERA: //拍照
                mPictureBar.onCameraResultHandle(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 批量上传图片
     */
    public void uploadNewImages() {
        final String[] files = new String[Bimp.mSelectedList.size()];
        for (int i = 0; i < Bimp.mSelectedList.size(); i++) {
            files[i] = Bimp.mSelectedList.get(i);
        }
        Bmob.uploadBatch(getApplicationContext(), files, new cn.bmob.v3.listener.UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {

                if (list1.size()==files.length){
                    addLost(list1);
                    EventBus.getDefault().post(new MainRefresh());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setIcon(R.drawable.add_photo_default);
                progressDialog.setMessage("Loading...共" + total + "个");
                progressDialog.setCancelable(false);
                progressDialog.setMax(100);
                progressDialog.setProgress(curPercent);
                if (!progressDialog.isShowing()) {
//                    progressDialog.show();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
        finish();

    }


}
