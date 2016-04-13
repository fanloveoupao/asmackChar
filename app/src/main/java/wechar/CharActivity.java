package wechar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;


import base.Application;
import base.BaseActivity;
import bruse.com.bruseapp.MainActivity;
import bruse.com.bruseapp.R;
import service.MyService;
import wechar.adapter.MessageAdapter;
import wechar.model.MessageModel;


/**
 * Created by bruse on 16/4/11.
 */
public class CharActivity extends BaseActivity implements View.OnClickListener {
    private EditText message;
    private Button btnSend;
    public TextView textView;
    public ArrayList<MessageModel> data;
    public ListView lvMessage;
    public MessageAdapter messageAdapter;
    //目的是为了获取textView
    public static CharActivity instance;

    @Override
    public void setContentViews() {
        setContentView(R.layout.charactivity);
        setTitle("聊天");
        instance = getInstance();
    }

    @Override
    public void initViews() {
        message = (EditText) findViewById(R.id.message);
        btnSend = (Button) findViewById(R.id.send);
        lvMessage = (ListView) findViewById(R.id.lvNewsshow);
    }

    public CharActivity getInstance() {
        if (instance == null) {
            synchronized (CharActivity.class) {
                if (instance == null) {
                    instance = CharActivity.this;
                }
            }
        }
        return instance;
    }

    @Override
    public void initListeners() {
        btnSend.setOnClickListener(this);
    }

    @Override
    public void initDatas() {
        data = new ArrayList<>();
        messageAdapter = new MessageAdapter(CharActivity.this, data);
        messageAdapter = new MessageAdapter(CharActivity.this, data);
        lvMessage.setAdapter(messageAdapter);
    }

    @Override
    public void onClick(View v) {
        //把我说的话发送到服务器
        final String body = message.getText().toString();

        message.setText("");
        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    Message message = new Message();
                    //设置发送给谁
                    message.setTo("bruse@conference.125.65.82.216");
                    message.setFrom("bruse@125.65.82.216");
                    message.setType(Message.Type.groupchat);
                    Log.i("asmack", message.getFrom());
                    //设置消息的内容
                    message.setBody(body);

                    final MessageModel messageModel = new MessageModel();

                    //进行发送
                    MyService.multiUserChat.sendMessage(message);
                    //进行数据加入
                    messageModel.setFrom(message.getFrom());
                    messageModel.setMessage(message.getBody());
                    data.add(messageModel);
                    /**
                     * 不能在线程中进行listView的操作
                     * */
                    handler.sendEmptyMessage(0);

                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    //进行更新ListView

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    messageAdapter.notifyDataSetChanged();
                    lvMessage.setSelection(data.size());
                    break;
            }
        }
    };
}
