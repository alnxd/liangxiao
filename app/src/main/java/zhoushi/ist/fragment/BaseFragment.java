package zhoushi.ist.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

/**
 * Created by wang on 2016/5/11.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
