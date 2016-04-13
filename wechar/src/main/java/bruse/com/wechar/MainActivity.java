package bruse.com.wechar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
    public static final String ACTION_DMEO_RECEIVE_MESSAGE = "action_demo_receive_message";
    public static final String ACTION_DMEO_AGREE_REQUEST = "action_demo_agree_request";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
