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
public class StudentNative implements Serializable {
    protected int id,sex,title,roletype;
    protected String name;
    protected String sname;
    protected String college;
    protected String summary;
    protected String grade;
    protected String create_org_list;

    public String getCreate_act_list() {
        return create_act_list;
    }

    public void setCreate_act_list(String create_act_list) {
        this.create_act_list = create_act_list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getRoletype() {
        return roletype;
    }

    public void setRoletype(int roletype) {
        this.roletype = roletype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCreate_org_list() {
        return create_org_list;
    }

    public void setCreate_org_list(String create_org_list) {
        this.create_org_list = create_org_list;
    }

    public String getJoin_org_list() {
        return join_org_list;
    }

    public void setJoin_org_list(String join_org_list) {
        this.join_org_list = join_org_list;
    }

    public String getJoin_act_list() {
        return join_act_list;
    }

    public void setJoin_act_list(String join_act_list) {
        this.join_act_list = join_act_list;
    }

    public String getFun_list() {
        return fun_list;
    }

    public void setFun_list(String fun_list) {
        this.fun_list = fun_list;
    }

    public String getBefun_list() {
        return befun_list;
    }

    public void setBefun_list(String befun_list) {
        this.befun_list = befun_list;
    }

    public String getApply_org_list() {
        return apply_org_list;
    }

    public void setApply_org_list(String apply_org_list) {
        this.apply_org_list = apply_org_list;
    }

    public String getApply_act_list() {
        return apply_act_list;
    }

    public void setApply_act_list(String apply_act_list) {
        this.apply_act_list = apply_act_list;
    }

    public String getApply_create_org_list() {
        return apply_create_org_list;
    }

    public void setApply_create_org_list(String apply_create_org_list) {
        this.apply_create_org_list = apply_create_org_list;
    }

    public String getApply_create_act_list() {
        return apply_create_act_list;
    }

    public void setApply_create_act_list(String apply_create_act_list) {
        this.apply_create_act_list = apply_create_act_list;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    protected String create_act_list;
    protected String join_org_list;
    protected String join_act_list;
    protected String fun_list;
    protected String befun_list;
    protected String apply_org_list;
    protected String apply_act_list;
    protected String apply_create_org_list;
    protected String apply_create_act_list;
    protected String photo;
}
