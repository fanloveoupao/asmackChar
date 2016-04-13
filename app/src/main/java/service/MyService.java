package service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;


import wechar.CharActivity;
import wechar.model.MessageModel;

public class MyService extends Service {

    public static MultiUserChat multiUserChat;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wechar();
    }

    private void wechar() {
        //联网操作在工作线程中
        new Thread() {
            @Override
            public void run() {
                super.run();
                /**
                 * 1、连接服务器
                 * 2、登录
                 * 3、加入群聊天
                 * */
                ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration("125.65.82.216", 5222, "125.65.82.216");
                XMPPConnection xmppConnection = new XMPPConnection(connectionConfiguration);
                try {
                    //链接服务器
                    connectionConfiguration.setReconnectionAllowed(true);
                    xmppConnection.connect();
                    if (xmppConnection.isConnected()) {
                        Log.i("LOGIN", "加入成功");
                        //登录
                        xmppConnection.login("bruse", "123");
                        //加入群聊
                        multiUserChat = new MultiUserChat(xmppConnection, "bruse@conference.125.65.82.216");
                        //加入后的昵称
                        multiUserChat.join("bruse");

                        /**
                         *这里进行接收消息
                         * */
                        xmppConnection.addPacketListener(new PacketListener() {
                            //如果服务器发消息给客户端则执行下面的方法
                            @Override
                            public void processPacket(Packet packet) {
                                if (packet instanceof Message) {
                                    //强制转换判断类型
                                    final Message message = (Message) packet;
                                    Message.Type type = message.getType();
                                    final MessageModel messageModel = new MessageModel();
                                    if (type == Message.Type.groupchat) {
                                        /**
                                         * 工作线程里面是不能动UI的
                                         *
                                         * */
                                        if (CharActivity.instance != null) {
                                            CharActivity.instance.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //这样就进入到主线程
                                                    messageModel.setFrom(message.getFrom());
                                                    messageModel.setMessage(message.getBody());
                                                    //这里进行测试
                                                    Log.i("服务器", message.getFrom());
                                                    if (CharActivity.instance != null) {
                                                        CharActivity.instance.data.add(messageModel);
                                                        handler.sendEmptyMessage(0);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }

                            }
                        }, null);

                    }
                } catch (XMPPException e) {
                    e.printStackTrace();
                    Log.i("ERROR", e + "");
                }

            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    CharActivity.instance.messageAdapter.notifyDataSetChanged();
//                    CharActivity.instance.lvMessage.setAdapter(CharActivity.instance.messageAdapter);
                    CharActivity.instance.lvMessage.setSelection(CharActivity.instance.data.size());
                    break;
            }
        }
    };
}
