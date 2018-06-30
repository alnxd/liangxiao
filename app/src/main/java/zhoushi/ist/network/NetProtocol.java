package zhoushi.ist.network;

/**
 * Created by Administrator on 2016/3/30.
 */
public class NetProtocol {

    public static final String NETROOT = "http://122.206.152.2:888/";//开发服务器
    //public static final String NETROOT = "http://182.254.242.101:8009/";//云服务器
    //public static final String NETROOT = "http://122.206.151.237:8009/";//校内测试服务器
    public static final String NETROOTBROADCAST = NETROOT + "indexannouce";

    public static final String NETROOTIMG =  NETROOT + "static/1.jpg";
    public static final String NETROOTORGIMG =  NETROOT +"static/st1.jpg";
    public static final String NETROOTUSERIMG = NETROOT + "static/hd1.jpg";

    public static final String NETROOT_KANNER_ORG =  NETROOT +"static/kanner/0/";
    public static final String NETROOT_KANNER_ACT =  NETROOT +"static/kanner/1/";

     //1注册 参数id(int) 学号或者工号 name(String)用户真实姓名 rolettype(int) 角色0：学生1：社管
    public static final String REGISTER = "register";

    //2登录 参数id(int) 学号或者工号  rolettype(int) 角色0：学生1：社管
    public static final String LOGIN = "login";
    public static final String PULLALLORG = "pullallorg";
    public static final String PULLORGDETAIL = "pullorgdetail";
    public static final String CREATEORG = "createorg";
    public static final String PULLCREATEORGDETAIL = "pullcreateorgdetail";
    public static final String APPROVEORG = "approveorg";
    public static final String APPROVEACT = "approveact";
    public static final String JOINNORG = "joinorg";
    public static final String JOINACT = "joinact";
    public static final String PULLAPPLYJOINORG = "pullapplyjoinorg";
    public static final String PULLAPPLYJOINACT = "pullapplyjoinact";
    public static final String APPROVEJOINORG = "approvejoinorg";
    public static final String APPROVEJOINACT = "approvejoinact";
    public static final String PULLACTDETAIL = "pullactdetail";
    public static final String CREATEACT = "createact";
    public static final String PULLCREATEACTDETAIL = "pullcreateactdetail";
    public static final String FILEHANDLER = "filehandle";
    public static final String UPDATEORG = "updateorg";
    public static final String PULLUSERDETAIL = "pulluserdetail";
    public static final String UPDATEUSER = "updateuser";


    //与协议相关的枚举
    public static final  int PULLALLID_ORG = 0;
    public static final  int PULLALLID_ACT = 1;
    public static final  int PULLALLID_CREATE_ORG = 2;
    public static final  int PULLALLID_CREATE_ACT = 3;
    public static final  int PULLALLID_APPLY_JOIN_ORG = 4;
    public static final  int PULLALLID_APPLY_JOIN_ACT = 5;

    public static final  int FILEHANDLE_USER = 0;
    public static final  int FILEHANDLE_ORG = 1;
    public static final  int FILEHANDLE_ACT = 2;

    public static final  int FILEHANDLE_ADD= 0;
    public static final  int FILEHANDLE_DELETE = 1;

    public static final  int JOINSTATE_ADD= 0;
    public static final  int JOINSTATE_DELETE = 1;

    public static final  int FILEHANDLE_TYPE_LOGO= 0;//logo
    public static final  int FILEHANDLE_TYPE = 1;//普通图片

    public static final  int ROLE_OFFICE = 1;//管理员
    public static final  int ROLE_STUDENT = 0;//学生

    public static final  int ERROR_SUCCESS = 0;//成功

    public static final  int LOGIC_BASE = 100;
    public static final  int LOGIC_PERSON = 101;
}
