package zhoushi.ist.utils;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zhoushi.ist.MainApplication;
import zhoushi.ist.network.NetProtocol;

/**
 * Created by wang on 2016/5/13.
 */
public class Utils {
    public static String IntList2String( List<Integer> list,int nStart,int nStep,String sI  )
    {
        String outStr = "";
        for (int i=nStart;i<(nStart + nStep);i++){
            if(i >= list.size())
            {
                break;
            }
            outStr = outStr + list.get(i) + sI;
        }
        return outStr.substring(0,outStr.length()-1);
    }

    public static ArrayList<Integer> String2IntList( String sR,String sI  )
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if(sR == null || sR.length() <=0)
            return list;
        String[] outStr = sR.split(sI);
        for (int i=0;i<outStr.length;i++){
            list.add(Integer.parseInt(outStr[i]));
        }
        return list;
    }

    public static String readLocalJson( String fileName){
        String jsonString="";
        String resultString="";
        try {
            InputStream inputStream= MainApplication.getContextObject().getResources().getAssets().open(fileName);
            byte[] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString=new String(buffer,"utf8");
        } catch (Exception e) {
            // TODO: handle exception
            int a = 0;
        }
        return resultString;
    }

    //根据协议规则组织文件访问的url地址
    public static String configFileUrl( int nType ,int nID,String fileName){
     return NetProtocol.NETROOT + "static/" + nType + "/" + nID +"/"+fileName;
    }

    public static String configLogoFileUrl( int nType ,int nID){
        return NetProtocol.NETROOT + "static/" + nType + "/" + nID +"/logo/"+"0.jpg";
    }

    public static void logTime(String sEx ){
        sEx = (sEx == null)?"":sEx;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        String date = sDateFormat.format(new java.util.Date());
        Log.d("当前系统时间","当前系统时间" + date + sEx);
    }

}
