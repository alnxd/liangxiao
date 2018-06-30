package zhoushi.ist.dal;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/29.
 */
public class Dal_Base<key,value> {
    private HashMap<key, value> m_listData = new HashMap<key,value>();


    public boolean AddData(key k,value v) {
        if(getM_listData().containsKey(k))
        {
            return false;
        }
        getM_listData().put(k, v);
        return true;
    }

    public boolean DeleteData(key k) {
        if(!getM_listData().containsKey(k))
        {
            return false;
        }
        getM_listData().remove(k);
        return true;
    }

    public boolean UpdateData(key k,value v) {
        if(!getM_listData().containsKey(k))
        {
            return false;
        }
        value srcV =  getM_listData().get(k);
        srcV = v;
        return true;
    }

    public value GetData(key k) {
        return getM_listData().get(k);
    }

    public Boolean HasData(key k) {
        return getM_listData().containsKey(k);
    }

    public HashMap<key, value> getM_listData() {
        return m_listData;
    }
}
