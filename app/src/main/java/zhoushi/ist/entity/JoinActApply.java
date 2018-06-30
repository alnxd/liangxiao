package zhoushi.ist.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/28.
 * id
 * applyer   申请者id
 * reason    申请理由
 * optype    加入或退出
 * state     当前申请的状态，0未审批 1批准 2不批准
 * actid     申请加入的活动id
 */
public class JoinActApply implements Serializable {

    private int id;
    private int applyer;

    public int getOptype() {
        return optype;
    }

    public void setOptype(int optype) {
        this.optype = optype;
    }

    private int optype;
    private int state;
    private int actid;
    private String reason;

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


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getActid() {
        return actid;
    }

    public void setActid(int actid) {
        this.actid = actid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
