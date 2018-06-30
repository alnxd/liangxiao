package zhoushi.ist.network;

import android.os.Message;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import zhoushi.ist.dal.DataManager;
import zhoushi.ist.eventbus.MessageEvent;

/**
 * Created by Administrator on 2016/3/30.
 */
//处理登录网络消息
public class LoginNetHandler extends NetHandler {

    private int nErrercode,m_userid,m_nRoletype;
    // 子类必须重写此方法，接受数据
    @Override
    public void handleMessage(Message msg) {
        // TODO Auto-generated method stub
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;
        String result = msg.getData().getString("result");
        try {
            JSONObject jsonObject = new JSONObject(result);
            nErrercode = jsonObject.getInt("errorcode");
            m_userid = jsonObject.getInt("id");
            m_nRoletype = jsonObject.getInt("roletype");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataManager.getInstance().setM_nMe(m_userid);
        DataManager.getInstance().setM_nRoleType(m_nRoletype);



        //通知消息给业务层

        EventBus.getDefault().post(
                new MessageEvent(nErrercode,m_userid));
    }

}
