package zhoushi.ist.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import zhoushi.ist.utils.Utils;

/**
 * Created by Administrator on 2016/3/28.
 * <p/>
 * <p/>
 * id
 * name  社团名字
 * type  社团所属类型id
 * owntype  社团所属院系id
 * summary  社团简介
 * namelist  社团成员id列表, id,title;id,title
 * applylist  加入社团申请id
 * starttime
 * level
 * max
 * photo
 */
public class Org extends OrgNative {
    public Org(OrgNative on)
    {
        this.setId(on.getId());
        this.setType(on.getType());
        this.setOwntype(on.getOwntype());
        this.setLevel(on.getLevel());
        this.setMax(on.getMax());
        this.setName(on.getName());
        this.setSummary(on.getSummary());
        this.setAnnounce(on.getAnnounce());
        this.setPhoto(on.getPhoto());
        this.setApplylist(on.getApplylist());
        this.setStarttime(on.getStarttime());
        this.setActlist(on.getActlist());
        this.setMemlist(on.getMemlist());
        string2Hash();
    }

    public void string2Hash()
    {
        actList2Hash();
        orgMember2Hash();
        photoStr2Hash();
    }

    private ArrayList<Integer> m_listAct;
    public ArrayList<Integer> getM_listAct() {
        return m_listAct;
    }
    public void setM_listAct(ArrayList<Integer> m_listAct) {
        this.m_listAct = m_listAct;
    }

    private ArrayList<String> m_listPhoto= new ArrayList<String>();
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

    public int getOrgBoss()
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

    public Boolean isOrgMember(int nID)
    {
        return m_hashMember.containsKey(nID);
    }

    public void orgMember2Hash()
    {
        m_hashMember.clear();
       String[] result =  memlist.split(";");
        for(int i=0;i<result.length;i++)
        {
            String[] result1 = result[i].split(":");
            m_hashMember.put(Integer.parseInt(result1[0]), Integer.parseInt(result1[1]));
        }
    }

    public void actList2Hash()
    {
        setM_listAct(Utils.String2IntList(actlist, ";"));
    }

    public void photoStr2Hash()
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
