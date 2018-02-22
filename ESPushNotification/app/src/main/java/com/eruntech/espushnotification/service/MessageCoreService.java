package com.eruntech.espushnotification.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


/**
 * 消息监听服务
 * 2018/2/22.
 */

public class MessageCoreService extends Service {

    public MessageCoreService () {
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startService(new Intent(this,MessageService.class));
        return super.onStartCommand(intent, flags, startId);
    }

    public boolean onUnbind(Intent intent) {
        Log.e("消息状态", "消息服务被卸载");
        return super.onUnbind(intent);
    }

    public void onDestroy() {
        Log.e("消息服务：", "停止了");
        this.startService(new Intent(this,MessageService.class));
        super.onDestroy();
    }
}
