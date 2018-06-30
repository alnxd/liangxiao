package zhoushi.ist;

import android.app.Application;
import android.content.Context;

import org.xutils.x;

import zhoushi.ist.dal.DataManager;
import zhoushi.ist.network.NetManager;
import zhoushi.ist.utils.FontManager;

/**
 * Created by Administrator on 2016/3/29.
 */
public class MainApplication extends Application {
    private static Context m_stContext;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        m_stContext = getApplicationContext();
    }

    public static Context getContextObject()
    {
        return m_stContext;
    }
}
