package zhoushi.ist.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/28.
 * id
 * name
 * begintime
 * endtime
 * type 活动类型
 * ownertype 活动所属院系id
 * applylist 申请加入活动者列表',
 * area 活动地点
 * level 活动级别id
 * summary  活动简介
 * namelist  活动参加者列表
 * max
 * photo
 */
public class Act extends ActNative {
    public Act(ActNative an)
    {
        this.setId(an.getId());
        this.setType(an.getType());
        this.setOwnertype(an.getOwnertype());
        this.setLevel(an.getLevel());
        this.setMax(an.getMax());
        this.setOrgid(an.getOrgid());
        this.setName(an.getName());
        this.setApplylist(an.getApplylist());
        this.setArea(an.getArea());
        this.setSummary(an.getSummary());
        this.setMemlist(an.getMemlist());
        this.setBegintime(an.getBegintime());
        this.setEndtime(an.getEndtime());
        this.setPhoto(an.getPhoto());
        str2Hash();
    }

    public void str2Hash()
    {
        photoString2Hash();
        actMember2Hash();
    }

    private ArrayList<String> m_listPhoto = new ArrayList<String>();
    public ArrayList<String> getM_listPhoto() {
        return m_listPhoto;
    }
    public void setM_listPhoto(ArrayList<String> m_listPhoto) {
        this.m_listPhoto = m_listPhoto;
    }

    private HashMap<Integer,Integer> m_hashMember = new HashMap<Integer,Integer>();
    public HashMap<Integer, Integer> getM_hashMember() {
        return m_hashMember;
    }
    public void setM_hashMember(HashMap<Integer, Integer> m_hashMember) {
        this.m_hashMember = m_hashMember;
    }

    public int getActBoss()
    {
        Iterator iter = m_hashMember.entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry entry = (Map.Entry)iter.next();
            int nID = (int)entry.getKey();
            int nTitle = (int)entry.getValue();
            if(nTitle == 1)//如果头衔是社长，就返回对应的用户id
            {
                return nID;
            }
        }

        return -1;
    }

    public Boolean isActMember(int nID)
    {
        return m_hashMember.containsKey(nID);
    }

    public void actMember2Hash()
    {
        m_hashMember.clear();
        String[] result =  memlist.split(";");
        for(int i=0;i<result.length;i++)
        {
            String[] result1 = result[i].split(":");
            m_hashMember.put(Integer.parseInt(result1[0]),Integer.parseInt(result1[1]));
        }
    }

    public void photoString2Hash()
    {
        if(photo == null)return;
        m_listPhoto.clear();
        String[] result =  photo.split(";");
        for(int i=0;i<result.length;i++)
        {
            m_listPhoto.add(result[i]);
        }
    }
}
