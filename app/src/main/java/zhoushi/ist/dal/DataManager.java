package zhoushi.ist.dal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class DataManager {
    private int m_nMe = 0;//当前登录者的id
    private Dal_Act m_dataAct;
    private Dal_CAA m_dataCAA;
    private Dal_COA m_dataCOA;
    private Dal_JAA m_dataJAA;
    private Dal_JOA m_dataJOA;
    private Dal_Officer m_dataOfficer;
    private Dal_Org m_dataOrg;
    private Dal_Student m_dataStudent;
    private DataConfig m_dataConfig;
    private ArrayList<String> m_listPhotoPath ;//上传的文件路径

    public int getM_nCurrItem() {
        return m_nCurrItem;
    }

    public void setM_nCurrItem(int m_nCurrItem) {
        this.m_nCurrItem = m_nCurrItem;
    }

    private  int m_nCurrItem = 1;//记录当前选择的主页fragment索引

    public int getM_nRoleType() {
        return m_nRoleType;
    }

    public void setM_nRoleType(int m_nRoleType) {
        this.m_nRoleType = m_nRoleType;
    }

    private int m_nRoleType;

    public int getM_nMe() {
        return m_nMe;
    }

    public void setM_nMe(int m_nMe) {
        this.m_nMe = m_nMe;
    }


    public ArrayList<String> getM_listPhotoPath() {
        return m_listPhotoPath;
    }

    private DataManager() {
         m_dataAct = new Dal_Act();
         m_dataCAA = new Dal_CAA();
         m_dataCOA = new Dal_COA();
         m_dataJAA = new Dal_JAA();
         m_dataJOA = new Dal_JOA();
         m_dataOfficer = new Dal_Officer();
         m_dataOrg = new Dal_Org();
         m_dataStudent = new Dal_Student();
         m_dataConfig = new DataConfig();
         getM_dataConfig().initConfigs();
         m_listPhotoPath = new ArrayList<String>();
    }

    public Dal_Act getM_dataAct() {
        return m_dataAct;
    }

    public Dal_CAA getM_dataCAA() {
        return m_dataCAA;
    }

    public Dal_COA getM_dataCOA() {
        return m_dataCOA;
    }

    public Dal_JAA getM_dataJAA() {
        return m_dataJAA;
    }

    public Dal_JOA getM_dataJOA() {
        return m_dataJOA;
    }

    public Dal_Org getM_dataOrg() {
        return m_dataOrg;
    }

    public Dal_Student getM_dataStudent() {
        return m_dataStudent;
    }

    public DataConfig getM_dataConfig() {
        return m_dataConfig;
    }

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

}
