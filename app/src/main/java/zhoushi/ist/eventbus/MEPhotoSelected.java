package zhoushi.ist.eventbus;

/**
 * Created by Administrator on 2016/6/25.
 */
public class MEPhotoSelected extends MEBase {
    public int getM_nType() {
        return m_nType;
    }

    public void setM_nType(int m_nType) {
        this.m_nType = m_nType;
    }

    private int m_nType;

    public int getM_nImageType() {
        return m_nImageType;
    }

    public void setM_nImageType(int m_nImageType) {
        this.m_nImageType = m_nImageType;
    }

    private int m_nImageType;


}
