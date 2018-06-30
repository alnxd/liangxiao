package zhoushi.ist.viewpage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import zhoushi.ist.R;
import zhoushi.ist.activity.InterfaceActivity;
import zhoushi.ist.activity.MainActivity;
import zhoushi.ist.activity.PhotoSelectActivity;

public class WelcomeApp extends Activity {
    private boolean isFirstIn = false;
    private static final  int TIME = 500;
    private static final int GO_HOME = 1000;
    private static final int GUIDE =1001;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case GO_HOME:
                   goHome();
                   break;
               case GUIDE:
                   goGuide();
                   break;
           }
        }


    };

    private void goGuide() {
        Intent intent = new Intent(this,Guide.class);
        startActivity(intent);
        finish();
    }

    private void goHome() {
        //Intent intent = new Intent(this,MainActivity.class);
        Intent intent = new Intent(this, InterfaceActivity.class);
        startActivity(intent);
        finish();
    }
    private void init(){
        SharedPreferences sharedPreferences = getSharedPreferences("wangwang",MODE_PRIVATE);
        isFirstIn = sharedPreferences.getBoolean("isFirstIn",true);  //数据库中没有isFirstIn的值，所以首次时值为true；
//        getBoolean（）；的第二个参数为指定默认值；
        if (!isFirstIn)    //isFirstIn的值为true；
            handler.sendEmptyMessageDelayed(GO_HOME,TIME);
        else{
            handler.sendEmptyMessageDelayed(GUIDE,TIME);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstIn",false);
            editor.commit();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_welcome_app);
        init();
    }


}
