package bruse.com.wechar.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;

import java.util.ArrayList;
import java.util.List;

import bruse.com.wechar.Config.DemoContext;
import bruse.com.wechar.R;
import bruse.com.wechar.base.BaseApiActivity;
import bruse.com.wechar.database.UserInfos;
import bruse.com.wechar.model.Friends;
import bruse.com.wechar.model.Groups;
import bruse.com.wechar.model.User;
import bruse.com.wechar.myview.EditTextHolder;
import bruse.com.wechar.myview.LoadingDialog;
import bruse.com.wechar.util.Constants;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class LoginActivity extends BaseApiActivity implements View.OnClickListener, EditTextHolder.OnEditTextFocusChangeListener {

    private static final String TAG = LoginActivity.class.getSimpleName();  //获取类名

    private int REQUEST_CODE_REGISTER = 200;
    public static final String INTENT_IMAIL = "intent_email";
    public static final String INTENT_PASSWORD = "intent_password";
    private int HANDLER_LOGIN_SUCCESS = 1;
    private int HANDLER_LOGIN_FAILURE = 2;
    private int HANDLER_LOGIN_HAS_FOCUS = 3;
    private int HANDLER_LOGIN_HAS_NO_FOCUS = 4;
    private AbstractHttpRequest<User> loginHttpRequest;
    private AbstractHttpRequest<Friends> getUserInfoHttpRequest;
    private AbstractHttpRequest<Groups> mGetMyGroupsRequest;
    private LoadingDialog mDialog;
    private Handler mHandler;

    EditTextHolder mEditUserNameEt;
    EditTextHolder mEditPassWordEt;
    List<UserInfos> friendsList = new ArrayList<UserInfos>();

    String userName;
    private boolean isFirst = false;
    Drawable drawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i("TAG", TAG);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mLoginImg = (ImageView) findViewById(R.id.de_login_logo);
        mUserNameEt = (EditText) findViewById(R.id.app_username_et);
        mPassWordEt = (EditText) findViewById(R.id.app_password_et);
        mSignInBt = (Button) findViewById(R.id.app_sign_in_bt);
        mRegister = (TextView) findViewById(R.id.de_login_register);
        mFogotPassWord = (TextView) findViewById(R.id.de_login_forgot);
        mImgBackgroud = (ImageView) findViewById(R.id.de_img_backgroud);
        mFrUserNameDelete = (FrameLayout) findViewById(R.id.fr_username_delete);
        mFrPasswordDelete = (FrameLayout) findViewById(R.id.fr_pass_delete);
        mIsShowTitle = (RelativeLayout) findViewById(R.id.de_merge_rel);
        mLeftTitle = (TextView) findViewById(R.id.de_left);
        mRightTitle = (TextView) findViewById(R.id.de_right);

        mSignInBt.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mLeftTitle.setOnClickListener(this);
        mRightTitle.setOnClickListener(this);
        mUserNameEt.setOnClickListener(this);
        mPassWordEt.setOnClickListener(this);

        drawable = mImgBackgroud.getDrawable();

        //        下面的代码为 EditTextView 的展示以及背景动画
        mEditUserNameEt = new EditTextHolder(mUserNameEt, mFrUserNameDelete, null);
        mEditPassWordEt = new EditTextHolder(mPassWordEt, mFrPasswordDelete, null);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                mImgBackgroud.startAnimation(animation);
                mEditPassWordEt.setmOnEditTextFocusChangeListener(LoginActivity.this);
                mEditUserNameEt.setmOnEditTextFocusChangeListener(LoginActivity.this);
            }
        }, 200);

    }

    protected void initData() {
        mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mDialog = new LoadingDialog(this);

        if (DemoContext.getInstance() != null) {
            String email = DemoContext.getInstance().getSharedPreferences().getString(INTENT_IMAIL, "");
            String password = DemoContext.getInstance().getSharedPreferences().getString(INTENT_PASSWORD, "");
            mUserNameEt.setText(email);
            mPassWordEt.setText(password);

            String token = DemoContext.getInstance().getSharedPreferences().getString(Constants.APP_TOKEN, Constants.DEFAULT);
            String cookie = DemoContext.getInstance().getSharedPreferences().getString("DEMO_COOKIE", Constants.DEFAULT);
            //当应用第二次打开的时候，需要重新去获取一下这个用户所加入的群组。
            if (!token.equals(Constants.DEFAULT) && !cookie.equals(Constants.DEFAULT)) {

                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }

                httpGetTokenSuccess(token);
            }
        }
    }

    /**
     * 融云 第二步：connect 操作
     *
     * @param token
     */
    private void httpGetTokenSuccess(String token) {

        try {
            Log.i(TAG, "----connect token--"+token);
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                        @Override
                        public void onTokenIncorrect() {
                            Log.e(TAG, "----connect onTokenIncorrect--");
                        }

                        @Override
                        public void onSuccess(String userId) {

                            Log.d(TAG, "----connect onSuccess userId----:" + userId);

                            if (isFirst) {
                                getUserInfoHttpRequest = DemoContext.getInstance().getDemoApi().getFriends(LoginActivity.this);
                                DemoContext.getInstance().deleteUserInfos();
                            } else {
                                final List<UserInfos> list = DemoContext.getInstance().loadAllUserInfos();
                                if (list == null || list.size() == 0) {
                                    //请求网络
                                    getUserInfoHttpRequest = DemoContext.getInstance().getDemoApi().getFriends(LoginActivity.this);
                                }
                            }

                            SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
                            edit.putString(Constants.APP_USER_ID, userId);
                            edit.apply();

                            RongCloudEvent.getInstance().setOtherListener();

                            //请求 demo server 获得自己所加入得群组。
                            mGetMyGroupsRequest = DemoContext.getInstance().getDemoApi().getMyGroups(LoginActivity.this);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode e) {
                            mHandler.obtainMessage(HANDLER_LOGIN_FAILURE).sendToTarget();
                            Log.e(TAG, "----connect onError ErrorCode----:" + e);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 这两个方法是用来进行时时进行回调的
     */
    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {

    }

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * EdirText里面有内容时弹出删除按钮
     */
    @Override
    public void onEditTextFocusChange(View v, boolean hasFocus) {

    }

    /**
     * 用户账户
     */
    private EditText mUserNameEt;
    /**
     * 密码
     */
    private EditText mPassWordEt;
    /**
     * 登录button
     */
    private Button mSignInBt;
    /**
     * 忘记密码
     */
    private TextView mFogotPassWord;
    /**
     * 注册
     */
    private TextView mRegister;
    /**
     * 输入用户名删除按钮
     */
    private FrameLayout mFrUserNameDelete;
    /**
     * 输入密码删除按钮
     */
    private FrameLayout mFrPasswordDelete;
    /**
     * logo
     */
    private ImageView mLoginImg;
    /**
     * 软键盘的控制
     */
    private InputMethodManager mSoftManager;
    /**
     * 是否展示title
     */
    private RelativeLayout mIsShowTitle;
    /**
     * 左侧title
     */
    private TextView mLeftTitle;
    /**
     * 右侧title
     */
    private TextView mRightTitle;
    private ImageView mImgBackgroud;
}
