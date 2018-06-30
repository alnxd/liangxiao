package zhoushi.ist.dal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import zhoushi.ist.entity.Act;
import zhoushi.ist.entity.Org;

/**
 * Created by Administrator on 2016/3/28.
 */
public class Dal_Act extends Dal_Base<Integer,Act>{
    //根据社团名匹配
    public ArrayList<Integer> MatchData(String name)
    {
        ArrayList<Integer> listID = new ArrayList<Integer>();
        Iterator iter = getM_listData().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Act act = (Act)entry.getValue();
            if(act.getName().contains(name))
            {
                listID.add(act.getId());
            }
        }
        return listID;
    }
}
