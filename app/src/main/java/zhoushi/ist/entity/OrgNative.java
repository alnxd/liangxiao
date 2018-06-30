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
public class OrgNative implements Serializable {

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

    public int getOwntype() {
        return owntype;
    }

    public void setOwntype(int owntype) {
        this.owntype = owntype;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getMemlist() {
        return memlist;
    }

    public void setMemlist(String memlist) {
        this.memlist = memlist;
    }

    public String getApplylist() {
        return applylist;
    }

    public void setApplylist(String applylist) {
        this.applylist = applylist;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getActlist() {
        return actlist;
    }

    public void setActlist(String actlist) {
        this.actlist = actlist;
    }

    protected int id,type,owntype,level,max;
    protected String name;
    protected String summary;
    protected String announce;
    protected String photo;
    protected String memlist;
    protected String applylist;
    protected String starttime;
    protected String actlist;
}
