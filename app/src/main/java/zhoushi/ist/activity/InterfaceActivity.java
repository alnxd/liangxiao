package zhoushi.ist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import zhoushi.ist.R;
import zhoushi.ist.activity.office.OfficeMainActivity;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.eventbus.MessageEvent;
import zhoushi.ist.eventbus.MessageEventInit;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;
import zhoushi.ist.utils.Utils;

public class InterfaceActivity extends BaseActivity {

    private boolean isHiddle = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_app);
        init();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void renderUI() {
        super.renderUI();

    }

    protected void init() {
        Intent intent = getIntent();
        //String strName = "200720010279";//intent.getStringExtra("userid");//取得学号或者工号
        String strName = intent.getStringExtra("userid");//取得学号或者工号
        int nRoleType =NetProtocol.ROLE_STUDENT;
        login(strName,nRoleType);
        EventBus.getDefault().register(this);
    }


    protected void login(String sName,int nRoleType) {
        //Utils.logTime("");
        DataManager.getInstance().setM_nRoleType(nRoleType);
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.LOGIN);
        //RequestParams params = new RequestParams("https://www.kancloud.cn/book/alnxd/a_test/release");
        params.addParameter("id", sName);
//        params.addParameter("roletype", nRoleType);
        params.addParameter("name", nRoleType);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);

                NetManager inst = NetManager.getInstance();
                NetHandler hd = inst.GetNetHandler(NetProtocol.LOGIN);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEventBus(MessageEvent message) {
        pullUserData(DataManager.getInstance().getM_nMe());
        int errorCode= message.getnErroecode();
        int roletype = DataManager.getInstance().getM_nRoleType();

        switch (errorCode) {
            case 0:
                if(roletype ==0) {
                    pullAllData(NetProtocol.PULLALLID_ORG);
                    pullAllData(NetProtocol.PULLALLID_ACT);
                    pullAllData(NetProtocol.PULLALLID_APPLY_JOIN_ORG);
                    pullAllData(NetProtocol.PULLALLID_APPLY_JOIN_ACT);
                } else if(roletype == 1){
                    pullAllData(NetProtocol.PULLALLID_CREATE_ORG);
                    pullAllData(NetProtocol.PULLALLID_CREATE_ACT);
                    Intent intent = new Intent(InterfaceActivity.this,OfficeMainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case 2:
                Toast.makeText(InterfaceActivity.this, "用户名或角色不正确", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(InterfaceActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                break;
        }
       renderUI();
    }

    public void pullAllData(int state) {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.PULLALLORG);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("type", state);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v("ZHTabActivity", result);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                NetManager netManager = NetManager.getInstance();
                NetHandler handler = netManager.GetNetHandler(NetProtocol.PULLALLORG);
                handler.sendMessage(message);
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

    public void pullUserData(int nID)
    {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.PULLUSERDETAIL);
        params.addParameter("userid", DataManager.getInstance().getM_nMe());
        params.addParameter("roletype", DataManager.getInstance().getM_nRoleType());
        params.addParameter("idlist", nID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                bundle.putInt("type", NetProtocol.PULLALLID_ORG);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pullDataFinished(MessageEventInit message) {
        if(message.getM_nType() == NetProtocol.PULLALLID_ORG)
        {
            //Utils.logTime("进入主页面");
            Intent intent = new Intent(InterfaceActivity.this, ZHTabActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
