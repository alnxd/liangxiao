package zhoushi.ist.network;

import android.os.Message;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import zhoushi.ist.dal.Dal_Org;
import zhoushi.ist.dal.Dal_Student;
import zhoushi.ist.dal.DataManager;
import zhoushi.ist.entity.Org;
import zhoushi.ist.entity.Student;
import zhoushi.ist.entity.StudentNative;
import zhoushi.ist.eventbus.MEOrgItemDetails;
import zhoushi.ist.eventbus.MEUserDetails;

/**
 * Created by wang on 2016/4/28.
 */
public class PullUserDetailsHandle extends NetHandler {


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(m_nErrorCode != NetProtocol.ERROR_SUCCESS)return;
        String result = msg.getData().getString("result");
        int nType = msg.getData().getInt("type");
        try {
            JSONObject object = new JSONObject(result);
            JSONObject list = object.getJSONObject("userlist");
            Iterator iterator = list.keys();
            String key;
            JSONObject value;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                value = list.getJSONObject(key);
                Gson gson = new Gson();
                StudentNative st = gson.fromJson(value.toString(),StudentNative.class);
                Student stu = new Student(st);
                Dal_Student ds =  DataManager.getInstance().getM_dataStudent();
                if(ds.HasData(stu.getId()))
                {
                    ds.UpdateData(stu.getId(), stu);
                }
                else
                {
                    ds.AddData(stu.getId(),stu);
                }

        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MEUserDetails meDetails = new MEUserDetails();
        meDetails.setM_nType(nType);
        EventBus.getDefault().post(meDetails);
    }
}
