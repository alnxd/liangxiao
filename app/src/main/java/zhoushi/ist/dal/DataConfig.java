package zhoushi.ist.dal;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zhoushi.ist.utils.Utils;

/**
 * Created by Administrator on 2016/6/22.
 */
public class DataConfig {
    public static final  int CREATETYPE = 0;
    public static final  int CREATEOWNERTYPE = 1;
    public static final  int CREATELEVEL = 2;

    private List<String> m_listCollege = new ArrayList<String>();//院系配置数据
    private List<String> m_listOrgType = new ArrayList<String>();//社团种类配置数据

    public List<String> getM_listOrgLevel() {
        return m_listOrgLevel;
    }

    private List<String> m_listOrgLevel = new ArrayList<String>();//级别配置数据

    public List<String> getM_listCollege() {
        return m_listCollege;
    }

    public void setM_listCollege(List<String> m_listCollege) {
        this.m_listCollege = m_listCollege;
    }

    public List<String> getM_listOrgType() {
        return m_listOrgType;
    }

    public void setM_listOrgType(List<String> m_listOrgType) {
        this.m_listOrgType = m_listOrgType;
    }

    public List<String> getM_listError() {
        return m_listError;
    }

    //错误码信息
    private List<String> m_listError = new ArrayList<String>();

    public HashMap<String, String> getM_listClass() {
        return m_listClass;
    }

    //班级代码配置
    private HashMap<String, String> m_listClass = new HashMap<String,String>();

    public void initConfigs()
    {
        String jsonStr = Utils.readLocalJson("configs/Common.json");
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray arrayCollege = jsonObject.getJSONArray("colleges");
            for(int i=0;i<arrayCollege.length();i++)
            {
                m_listCollege.add(i,(String)arrayCollege.get(i));
            }

            JSONArray typeCollege = jsonObject.getJSONArray("orgtype");
            for(int i=0;i<typeCollege.length();i++)
            {
                m_listOrgType.add(i,(String)typeCollege.get(i));
            }

            JSONArray orgLevel = jsonObject.getJSONArray("orglevel");
            for(int i=0;i<orgLevel.length();i++)
            {
                m_listOrgLevel.add(i,(String)orgLevel.get(i));
            }

            JSONArray errors = jsonObject.getJSONArray("errors");
            for(int i=0;i<errors.length();i++)
            {
                m_listError.add(i,(String)errors.get(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        initClassConfigs();
    }
    public void initClassConfigs()
    {
        String jsonStr = Utils.readLocalJson("configs/T_BJ.json");
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray arrayRecords= jsonObject.getJSONArray("RECORDS");
            for(int i=0;i<arrayRecords.length();i++)
            {
                JSONObject recordObject = arrayRecords.getJSONObject(i);
                String classCode = recordObject.getString("BJDM");
                String classDesc = recordObject.getString("BJMC");
                m_listClass.put(classCode,classDesc);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
