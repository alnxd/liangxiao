package zhoushi.ist.entity;

import java.io.Serializable;
import java.util.ArrayList;

import zhoushi.ist.utils.Utils;

/**
 * Created by Administrator on 2016/3/28.
 *   id  学号或者工号
 *   sname 用户真实姓名
 *   sex 性别 男 1 女 0
 *   grade 年级编号
 *   college 学院编号
 *   name 社管真实姓名
 *   title 社管职位编号
 *   roletype  角色类型，0学生，1社管
 *
 *
 */
public class Student extends StudentNative {
    public Student(StudentNative sn)
    {
        this.setId(sn.getId());
        this.setSex(sn.getSex());
        this.setTitle(sn.getTitle());
        this.setRoletype(sn.getRoletype());
        this.setName(sn.getName());
        this.setSname(sn.getSname());
        this.setCollege(sn.getCollege());
        this.setSummary(sn.getSummary());
        this.setGrade(sn.getGrade());
        this.setCreate_org_list(sn.getCreate_org_list());
        this.setCreate_act_list(sn.getCreate_act_list());
        this.setJoin_org_list(sn.getJoin_org_list());
        this.setJoin_act_list(sn.getJoin_act_list());
        this.setFun_list(sn.getFun_list());
        this.setBefun_list(sn.getBefun_list());
        this.setApply_org_list(sn.getApply_org_list());
        this.setApply_act_list(sn.getApply_act_list());
        this.setApply_create_org_list(sn.getApply_create_org_list());
        this.setApply_create_act_list(sn.getApply_create_act_list());
        this.setPhoto(sn.getPhoto());
        string2List();
    }

    public void string2List()
    {
        string2COList();
        string2CAList();
        string2JOList();
        string2JAList();
    }

    public ArrayList<Integer> getM_listCO() {
        return m_listCO;
    }

    public void setM_listCO(ArrayList<Integer> m_listCO) {
        this.m_listCO = m_listCO;
    }

    public ArrayList<Integer> getM_listCA() {
        return m_listCA;
    }

    public void setM_listCA(ArrayList<Integer> m_listCA) {
        this.m_listCA = m_listCA;
    }

    public ArrayList<Integer> getM_listJO() {
        return m_listJO;
    }

    public void setM_listJO(ArrayList<Integer> m_listJO) {
        this.m_listJO = m_listJO;
    }

    public ArrayList<Integer> getM_listJA() {
        return m_listJA;
    }

    public void setM_listJA(ArrayList<Integer> m_listJA) {
        this.m_listJA = m_listJA;
    }


    private ArrayList<Integer> m_listCO = new ArrayList<Integer>();
    private ArrayList<Integer> m_listCA = new ArrayList<Integer>();
    private ArrayList<Integer> m_listJO = new ArrayList<Integer>();
    private ArrayList<Integer> m_listJA = new ArrayList<Integer>();

    public void string2COList()
    {
        m_listCO = Utils.String2IntList(create_org_list,";");
    }

    public void string2CAList()
    {
        m_listCA = Utils.String2IntList(create_act_list,";");
    }

    public void string2JOList()
    {
        m_listJO = Utils.String2IntList(join_org_list,";");
    }

    public void string2JAList()
    {
        m_listJA = Utils.String2IntList(join_act_list,";");
    }

}
