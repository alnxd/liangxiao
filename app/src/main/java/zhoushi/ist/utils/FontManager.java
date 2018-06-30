package zhoushi.ist.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

import zhoushi.ist.MainApplication;
import zhoushi.ist.network.ApplyJoinOrgDetailHandler;
import zhoushi.ist.network.ApplyJoinOrgHandler;
import zhoushi.ist.network.ApproveACTHandler;
import zhoushi.ist.network.ApproveJoinOrgHandler;
import zhoushi.ist.network.ApproveOrgHandler;
import zhoushi.ist.network.CreateActHandle;
import zhoushi.ist.network.CreateOrgHandle;
import zhoushi.ist.network.LoginNetHandler;
import zhoushi.ist.network.NetHandler;
import zhoushi.ist.network.NetProtocol;
import zhoushi.ist.network.PullActDetailsHandle;
import zhoushi.ist.network.PullAllOrgHandle;
import zhoushi.ist.network.PullCreateActDetailHandler;
import zhoushi.ist.network.PullCreateOrgDetailHandler;
import zhoushi.ist.network.PullOrgDetailsHandle;
import zhoushi.ist.network.PullUserDetailsHandle;

/**
 * Created by Administrator on 2016/3/30.
 */
public class FontManager {

    private FontManager() {
        initFacePool();
    }

    private static FontManager ourInstance = new FontManager();

    public static FontManager getInstance() {
        return ourInstance;
    }

    private HashMap<String,Typeface> m_facePool = null;

    private void initFacePool()
    {
        m_facePool = new HashMap<String,Typeface>();
        Typeface customFont = Typeface.createFromAsset(MainApplication.getContextObject().getResources().getAssets(), "fonts/lantingxihei.TTF");
        m_facePool.put("lantingxihei",customFont);

    }

    public  Typeface GetFace(String key)
    {
        return m_facePool.get(key);
    }
}
