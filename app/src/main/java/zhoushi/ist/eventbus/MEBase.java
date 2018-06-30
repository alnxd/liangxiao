package zhoushi.ist.eventbus;

/**
 * Created by wang on 2016/5/14.
 */
public class MEBase {
    public int getnErroecode() {
        return nErroecode;
    }
    public void setnErroecode(int nErroecode) {
        this.nErroecode = nErroecode;
    }
    protected   int nErroecode;
}
