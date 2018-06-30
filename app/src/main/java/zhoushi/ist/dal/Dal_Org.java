package zhoushi.ist.dal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import zhoushi.ist.entity.Org;

/**
 * Created by Administrator on 2016/3/28.
 */
public class Dal_Org extends Dal_Base<Integer,Org> {

    //根据社团名匹配
    public ArrayList<Integer> MatchData(String name)
    {
        ArrayList<Integer> listID = new ArrayList<Integer>();
        Iterator iter = getM_listData().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Org org = (Org)entry.getValue();
            //org.getName().contains(name);
            if(org.getName().contains(name))
            {
                listID.add(org.getId());
            }
        }
        return listID;
    }
}
