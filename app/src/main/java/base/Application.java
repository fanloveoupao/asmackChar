package base;

import android.os.Environment;
import android.util.Log;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.GetAccessUrlListener;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.io.File;

import cn.bmob.v3.Bmob;
import wechar.CharActivity;

/**
 * Created by bruse on 16/2/27.
 */
public class Application extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "5812255af6a3dab1327d24daca96c6c1");

    }


    @Override
    public String getTmpDirName() {
        return "Application";
    }


}
