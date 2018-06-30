package zhoushi.ist.network;

import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;

import zhoushi.ist.dal.DataManager;

/**
 * Created by Administrator on 2016/3/30.
 */
public class NetManager {

    private NetManager() {
        initHandlerPool();
    }

    private static NetManager ourInstance = new NetManager();

    public static NetManager getInstance() {
        return ourInstance;
    }

    private HashMap<String,NetHandler> m_handlerPool = null;

    private void initHandlerPool()
    {
        m_handlerPool = new HashMap<String,NetHandler>();
        m_handlerPool.put(NetProtocol.LOGIN,new LoginNetHandler());
        m_handlerPool.put(NetProtocol.PULLALLORG,new PullAllOrgHandle());
        m_handlerPool.put(NetProtocol.PULLORGDETAIL,new PullOrgDetailsHandle());
        m_handlerPool.put(NetProtocol.CREATEORG,new CreateOrgHandle());
        m_handlerPool.put(NetProtocol.PULLCREATEORGDETAIL,new PullCreateOrgDetailHandler());
        m_handlerPool.put(NetProtocol.APPROVEORG,new ApproveOrgHandler());
        m_handlerPool.put(NetProtocol.APPROVEACT,new ApproveACTHandler());
        m_handlerPool.put(NetProtocol.JOINNORG,new ApplyJoinOrgHandler());
        m_handlerPool.put(NetProtocol.JOINACT,new ApplyJoinActHandler());
        m_handlerPool.put(NetProtocol.PULLAPPLYJOINORG,new ApplyJoinOrgDetailHandler());
        m_handlerPool.put(NetProtocol.APPROVEJOINORG,new ApproveJoinOrgHandler());
        m_handlerPool.put(NetProtocol.APPROVEJOINACT,new ApproveJoinActHandler());
        m_handlerPool.put(NetProtocol.PULLACTDETAIL,new PullActDetailsHandle());
        m_handlerPool.put(NetProtocol.CREATEACT,new CreateActHandle());
        m_handlerPool.put(NetProtocol.PULLCREATEACTDETAIL,new PullCreateActDetailHandler());
        m_handlerPool.put(NetProtocol.PULLUSERDETAIL,new PullUserDetailsHandle());
    }

    public  NetHandler GetNetHandler(String proto)
    {
        return m_handlerPool.get(proto);
    }

    public void pullUserData(final int nOwner,String sUser)
    {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.PULLUSERDETAIL);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("roletype", DataManager.getInstance().getM_nRoleType());
        params.addParameter("idlist", sUser);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                bundle.putInt("type", nOwner);
                message.setData(bundle);

                NetManager netManager = NetManager.getInstance();
                NetHandler hd = netManager.GetNetHandler(NetProtocol.PULLUSERDETAIL);
                hd.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
            }
        });
    }
}
