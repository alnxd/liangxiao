package zhoushi.ist.network;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import zhoushi.ist.dal.DataManager;
import zhoushi.ist.eventbus.MessageEvent;

/**
 * Created by Administrator on 2016/3/30.
 */
public class NetHandler extends Handler {
    public NetHandler() {
    }

    public NetHandler(Looper L) {
        super(L);
    }

    protected int m_nErrorCode;
    // 子类必须重写此方法，接受数据
    @Override
    public void handleMessage(Message msg) {
        // TODO Auto-generated method stub
        super.handleMessage(msg);
        // 统一处理错误码
        String result = msg.getData().getString("result");
        try {
            JSONObject object = new JSONObject(result);
            m_nErrorCode = object.getInt("errorcode");
            if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)
               Toast.makeText(x.app(), DataManager.getInstance().getM_dataConfig().getM_listError().get(m_nErrorCode), Toast.LENGTH_LONG).show();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void postBllMessage()
    {
        EventBus.getDefault().post(
                new MessageEvent());
    }
}
