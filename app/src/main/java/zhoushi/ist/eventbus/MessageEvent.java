package zhoushi.ist.eventbus;

/**
 * Created by Administrator on 2016/4/11.
 */
public class MessageEvent extends  MEBase{

    public int getnID() {
        return nID;
    }

    public void setnID(int nID) {
        this.nID = nID;
    }

    private  int nID;

    public MessageEvent(int nErroecode, int nID) {
        this.nErroecode = nErroecode;
        this.nID = nID;
    }

    public  MessageEvent(int nErroecode){
        this.nErroecode = nErroecode;
    }
    public MessageEvent(){
    }

}