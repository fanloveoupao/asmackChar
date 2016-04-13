package bruse.com.wechar.base;

import android.app.Activity;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import bruse.com.wechar.R;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        getSupportActionBar().setLogo(R.mipmap.de_bar_logo);  //设置Actionbar的logo


    }
}
