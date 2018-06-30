package zhoushi.ist.eventbus;

import java.lang.reflect.Array;

/**
 * Created by wang on 2016/4/26.
 */
public class MessageEventInit extends  MEBase{
    private int m_nType;

    public int getM_nType() {
        return m_nType;
    }

    public void setM_nType(int m_nType) {
        this.m_nType = m_nType;
    }
}
