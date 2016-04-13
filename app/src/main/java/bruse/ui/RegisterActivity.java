package bruse.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_register;
    private EditText et_user;
    private EditText et_password;

    @Override
    public void setContentViews() {
        setContentView(R.layout.activity_register);
    }

    @Override
    public void initViews() {
        btn_register = (Button) findViewById(R.id.btn_register);
        et_user = (EditText) findViewById(R.id.id_et_user);
        et_password = (EditText) findViewById(R.id.et_password);
    }

    @Override
    public void initListeners() {
        btn_register.setOnClickListener(this);
    }

    @Override
    public void initDatas() {

    }

    /**
     * 进行注册事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                register();
                break;
        }
    }

    /**
     * 注册
     */

    public void register() {
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
        user.setEmail(username);
        user.setUsername(username);
        user.setPassword(password);

        user.signUp(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                MainActivity.launch(RegisterActivity.this);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "注册失败/n" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
