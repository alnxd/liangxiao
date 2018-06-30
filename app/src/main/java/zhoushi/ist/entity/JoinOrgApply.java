package zhoushi.ist.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/28.
 * <p/>
 * id
 * applyer  申请者id
 * reason  申请理由
 * title  申请职位id
 * optype  加入或退出
 * state  当前申请的状态，0未审批 1批准 2不批准
 * orgid
 */
public class JoinOrgApply implements Serializable {


    private  int id;
    private int applyer;
    private int optype;
    private int state;
    private int orgid;

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    private int title;
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

    public int getOptype() {
        return optype;
    }

    public void setOptype(int optype) {
        this.optype = optype;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
