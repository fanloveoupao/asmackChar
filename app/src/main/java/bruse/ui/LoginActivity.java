package bruse.ui;


import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import base.BaseActivity;
import bruse.com.bruseapp.MainActivity;
import bruse.com.bruseapp.R;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import user.MyUser;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_login;
    private Button btn_register;
    private EditText et_user;
    private EditText et_password;

    public static void launch(Activity activity) {

        activity.startActivity(new Intent(activity, LoginActivity.class));

    }

    @Override
    public void setContentViews() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initViews() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.login_btn_register);
        et_user = (EditText) findViewById(R.id.login_et_user);
        et_password = (EditText) findViewById(R.id.login_et_password);
    }

    @Override
    public void initListeners() {
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void initDatas() {

    }

    /**
     * 登录和注册
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                finish();
                break;
            case R.id.login_btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;
        }
    }

    public void login() {

        String username = et_user.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        //创建用户
        BmobUser user = new BmobUser();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(username);
        bmobUser.setPassword(password);
        bmobUser.login(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                MainActivity.launch(LoginActivity.this);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "登录失败/n" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 建立用户列表
     */
    public void saveMyUser() {
        BmobUser user = new BmobUser();
        MyUser myUser=new MyUser();
        myUser.setEmail(et_user.getText().toString().trim());
        myUser.save(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
