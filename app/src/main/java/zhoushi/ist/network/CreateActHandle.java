package zhoushi.ist.network;

import android.os.Message;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;

import zhoushi.ist.eventbus.MessageEventCreateAct;

/**
 * Created by wang on 2016/5/29.
 */
public class CreateActHandle extends NetHandler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;
        String result = msg.getData().getString("result");
        EventBus.getDefault().post(new MessageEventCreateAct());
    }




}
