package zhoushi.ist.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/28.
 * id  管理层人员Id，工号
 *
 */
public class Officer implements Serializable {

    private  int id,title;
    private String name;

    public String getTname() {
        return Tname;
    }

    public void setTname(String tname) {
        Tname = tname;
    }

    private String Tname;

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    private String sname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
