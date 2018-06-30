package zhoushi.ist.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import zhoushi.ist.network.NetHandler;

/**
 * Created by wangn on 16/3/29.
 */
public class BaseActivity extends AppCompatActivity {

    //广播接收器
   // private InnerReceiver receiver = new InnerReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }




//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        //注册广播
//        IntentFilter filter = new IntentFilter("test");
//        registerReceiver(receiver, filter);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        取消广播
//        unregisterReceiver(receiver);
//    }


    protected  void renderUI()
    {

    }

//    public class InnerReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //使用intent获取发送过来的数据
//            String msg = intent.getStringExtra("msg");
//            if(msg == m_sNetNotify)
//            {
//                renderUI();
//            }
//        }
//    }
}
