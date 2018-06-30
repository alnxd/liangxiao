package zhoushi.ist.network;

import android.os.Message;

/**
 * Created by wang on 2016/6/5.
 */
public class ApplyJoinActHandler extends NetHandler {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;
        String result = msg.getData().getString("result");
    }
}
