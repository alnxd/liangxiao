package zhoushi.ist.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/28.
 * <p/>
 * id
 * applyer   申请者id
 * name
 * area
 * level
 * max
 * summary
 * type
 * ownertype
 * starttime
 * endtime
 * state   当前申请的状态，0未审批 1批准 2不批准
 *
 */
public class CreateActApply implements Serializable {


    private int id;
    private int applyer;
    private int level;
    private int max;
    private int type;
    private int ownertype;
    private int state;

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }

    private int orgid;
    private String name,area,summary,starttime,endtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplyer() {
        return applyer;
    }

    public void setApplyer(int applyer) {
        this.applyer = applyer;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOwnertype() {
        return ownertype;
    }

    public void setOwnertype(int ownertype) {
        this.ownertype = ownertype;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
