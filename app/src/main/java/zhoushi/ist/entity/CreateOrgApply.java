package zhoushi.ist.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/28.
 * <p/>
 * id
 * applyer  申请创建社团人员id
 * name
 * max,
 * starttime
 * level,
 * summary
 * type
 * ownertype
 * state 当前申请的状态，0未审批 1批准 2不批
 */
public class CreateOrgApply implements Serializable {


    private int id, applyer, max, level, type, ownertype, state;
    private String name, starttime, summary;

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

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
