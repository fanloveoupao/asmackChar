package bruse.ui;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import bruse.com.bruseapp.MainActivity;
import bruse.com.bruseapp.R;
import cn.bmob.v3.BmobUser;
import service.MyService;
import wechar.CharActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 启动界面
 */
public class StartLaunchActivity extends Activity {
    private static final int START_UI = 0x1;
    private static final int LOGIN_UI = 0x2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_launch);

        /**
         * 缓存用户对象进行登录
         * */
        BmobUser bmobUser = BmobUser.getCurrentUser(getApplicationContext());
        if (bmobUser != null) {
            //允许登录
            handler.sendEmptyMessageDelayed(START_UI, 2000);
        } else {
            //缓存用户为空时可以进行注册界面的跳转
            handler.sendEmptyMessageDelayed(LOGIN_UI, 2000);
        }
        startService(new Intent(StartLaunchActivity.this, MyService.class));

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_UI:
//                    startActivity(new Intent(StartLaunchActivity.this, MainActivity.class));
                    //测试聊天
                    startActivity(new Intent(StartLaunchActivity.this, CharActivity.class));
                    finish();
                    break;
                case LOGIN_UI:
                    startActivity(new Intent(StartLaunchActivity.this, LoginActivity.class));
                    finish();
                    break;
            }
        }
    };
}
