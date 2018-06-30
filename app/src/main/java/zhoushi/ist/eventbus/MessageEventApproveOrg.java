package zhoushi.ist.eventbus;

/**
 * Created by wang on 2016/6/4.
 */
public class MessageEventApproveOrg extends MEBase {

    public int getM_nErrorCode() {
        return m_nErrorCode;
    }

    public void setM_nErrorCode(int m_nErrorCode) {
        this.m_nErrorCode = m_nErrorCode;
    }

    private int m_nErrorCode;

    public MessageEventApproveOrg(int m_nErrorCode){
        this.m_nErrorCode = m_nErrorCode;
    }
}
