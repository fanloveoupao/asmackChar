package bruse.com.wechar.base;

import android.os.Bundle;

import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;
import com.sea_monster.network.ApiCallback;

import bruse.com.wechar.R;

/**
 * 网络的回调
 */
public abstract class BaseApiActivity extends BaseActivity implements ApiCallback {

    //这个方法很好
    public abstract void onCallApiSuccess(AbstractHttpRequest request, Object obj);

    public abstract void onCallApiFailure(AbstractHttpRequest request, BaseException e);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_api);
    }

    /**
     * 网络请求的回调
     * 成功
     */
    @Override
    public void onComplete(final AbstractHttpRequest abstractHttpRequest, final Object o) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onCallApiSuccess(abstractHttpRequest, o);
            }
        });
    }

    /**
     * 网络请求的回调
     * 失败
     */
    @Override
    public void onFailure(final AbstractHttpRequest abstractHttpRequest, final BaseException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onCallApiSuccess(abstractHttpRequest, e);
            }
        });
    }
}
