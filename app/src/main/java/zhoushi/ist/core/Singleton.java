package zhoushi.ist.core;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/28.
 */
public class Singleton implements Serializable {
    private static class SingletonHolder {
                /**
                  * 单例对象实例
                  */
                static final Singleton INSTANCE = new Singleton();
            }

    public static Singleton getInstance() {
                return SingletonHolder.INSTANCE;
            }

                /**
         * readResolve方法应对单例对象被序列化时候
          */
                private Object readResolve() {
                return getInstance();
            }
}
