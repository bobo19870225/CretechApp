package cn.com.cretech.util;

import android.os.Handler;
import android.os.Looper;

public class AboutHandlers extends Handler {
    private static volatile AboutHandlers instance;

    public static AboutHandlers getInstance() {
        if (null == instance) {
            synchronized (AboutHandlers.class) {
                if (null == instance) {
                    instance = new AboutHandlers();
                }
            }
        }
        return instance;
    }
    private AboutHandlers() {
        super(Looper.getMainLooper());
    }
}
