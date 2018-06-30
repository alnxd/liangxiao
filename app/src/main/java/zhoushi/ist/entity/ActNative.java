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
public class ActNative implements Serializable {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplylist() {
        return applylist;
    }

    public void setApplylist(String applylist) {
        this.applylist = applylist;
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

    public String getMemlist() {
        return memlist;
    }

    public void setMemlist(String memlist) {
        this.memlist = memlist;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    protected int id;
    protected int type;
    protected int ownertype;
    protected int level;
    protected int max;
    protected int orgid;
    private String name;
    protected String applylist;
    protected String area;
    protected String summary;
    protected String memlist;
    protected String begintime;
    protected String endtime;
    protected String announce;
    protected String photo;

}
