package zhoushi.ist.activity;

import android.content.Intent;
import android.os.Bundle;
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
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.network.NetProtocol;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private boolean isHiddle = true;

    @ViewInject(R.id.m_eStudentId)
    private EditText m_eStudentId;

    @ViewInject(R.id.m_ePasswd)
    private EditText m_ePasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//        Intent intent = getIntent();
//        String strName = intent.getStringExtra("id");//取得学号或者工号
//        int nRoleType = (strName.length() > 8)?NetProtocol.ROLE_STUDENT:NetProtocol.ROLE_OFFICE;
//        login(strName,nRoleType);
        EventBus.getDefault().register(this);
    }


    protected void login(String sName,int nRoleType) {
        DataManager.getInstance().setM_nRoleType(nRoleType);
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.LOGIN);
        params.addParameter("id", sName);
        params.addParameter("roletype",nRoleType);
        if(nRoleType == NetProtocol.ROLE_STUDENT)
        {
            pullAllData(NetProtocol.PULLALLID_ORG);
            pullAllData(NetProtocol.PULLALLID_ACT);
            pullAllData(NetProtocol.PULLALLID_APPLY_JOIN_ORG);
            pullAllData(NetProtocol.PULLALLID_APPLY_JOIN_ACT);
        }
        else
        {
            pullAllData(NetProtocol.PULLALLID_CREATE_ORG);
            pullAllData(NetProtocol.PULLALLID_CREATE_ACT);
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v("MainActivity", result);
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

    @Event(value = R.id.show)
    private void onShowLongClick(View view) {
        if (isHiddle) {
            m_ePasswd.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
//            editText2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
//            editText2.setInputType(InputType.TYPE_CLASS_TEXT);
            m_ePasswd.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
        isHiddle = !isHiddle;
    }


    //登录驱动
    @Event(value = R.id.login)
    private void onTestLoginClick(View view) throws DbException {
        RequestParams params = new RequestParams(NetProtocol.NETROOT + NetProtocol.LOGIN);
        params.addParameter("id", m_eStudentId.getText());
        params.addParameter("roletype", m_ePasswd.getText());
        DataManager.getInstance().setM_nRoleType(Integer.parseInt(m_ePasswd.getText().toString()));
        if(Integer.parseInt(m_ePasswd.getText().toString()) == 0)
        {
            pullAllData(NetProtocol.PULLALLID_ORG);
            pullAllData(NetProtocol.PULLALLID_ACT);
            pullAllData(NetProtocol.PULLALLID_APPLY_JOIN_ORG);
            pullAllData(NetProtocol.PULLALLID_APPLY_JOIN_ACT);
        }
        else
        {
            pullAllData(NetProtocol.PULLALLID_CREATE_ORG);
            pullAllData(NetProtocol.PULLALLID_CREATE_ACT);
        }
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.v("MainActivity", result);
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
    public void helloEventBus(MessageEvent message) {
        pullUserData(DataManager.getInstance().getM_nMe());
        int errorCode= message.getnErroecode();
        int roletype = DataManager.getInstance().getM_nRoleType();
        switch (errorCode) {
            case 0:
                if(roletype ==0) {
                    Intent intent = new Intent(MainActivity.this, ZHTabActivity.class);
                    startActivity(intent);
                    finish();
                } else if(roletype == 1){
                    Intent intent = new Intent(MainActivity.this,OfficeMainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case 2:
                Toast.makeText(MainActivity.this, "用户名或角色不正确", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(MainActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
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
}
