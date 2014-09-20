package com.com.boha.monitor.library.util;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aubreyM on 2014/08/28.
 */
public class TimerUtil {

    public interface TimerListener {
        public void onSessionDisconnected();
    }
    static TimerListener listener;
    static Timer timer;
    static final long TEN_SECONDS = 10 * 1000;
    public static void startTimer(TimerListener timerListener) {
        //
        Log.d("TimerUtil", "########## Websocket Session Timer starting .....");
        listener = timerListener;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e("TimerUtil", "########## about to disconnect websocket session");
                WebSocketUtil.disconnectSession();
                listener.onSessionDisconnected();
            }
        }, TEN_SECONDS);
    }
    public static void killTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            Log.w("TimerUtil", "########## Websocket Session Timer KILLED");
        }
    }
}
