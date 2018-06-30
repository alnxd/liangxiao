package zhoushi.ist.eventbus;

/**
 * Created by Administrator on 2016/6/25.
 */
public class MECreateORG extends MEBase {

    public int getnID() {
        return nID;
    }

    public void setnID(int nID) {
        this.nID = nID;
    }

    private  int nID;

    public MECreateORG(int nErroecode, int nID) {
        this.nErroecode = nErroecode;
        this.nID = nID;
    }
}
