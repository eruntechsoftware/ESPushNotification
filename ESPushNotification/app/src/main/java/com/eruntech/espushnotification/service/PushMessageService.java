package com.eruntech.espushnotification.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eruntech.espushnotification.handlers.ReceiveService;
import com.eruntech.espushnotification.handlers.ReceiverPush;
import com.eruntech.espushnotification.utils.UserData;

import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 心跳服务
 * on 2018/2/26.
 */

public class PushMessageService extends Service
{
    private static Context serviceContext;
    public static HashMap<String, ReceiverPush> receiverPushHashMap = new HashMap<>();

    private Timer timer;
    private String packgeName;
    private UserData userData;


    @Nullable
    @Override
    public IBinder onBind (Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate ()
    {
        super.onCreate();

    }

    public static Context getServiceContext ()
    {
        return serviceContext;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
        try
        {
            serviceContext = getApplicationContext();
            this.userData = new UserData(serviceContext);
            this.packgeName = this.getPackageName();
//            startReceiver ();
            runRecevivePushTask ();
        }
        catch (Exception ex)
        {
        }
        return START_STICKY;
    }

    public void startReceiver ()
    {
        try
        {
            this.userData = new UserData(this.getApplicationContext());
            //群组标签
            if (userData != null && this.userData.getStringSet("grouptags") != null)
            {
                Set<String> sets = this.userData.getStringSet("grouptags");
                for (String v : sets)
                {
                    if (!receiverPushHashMap.containsKey(v))
                    {
                        ReceiveService receiver = new ReceiveService(this.getApplicationContext(), v);
                        receiverPushHashMap.put(v, receiver.startPush());
                    }
                }
            }

            if (this.userData.getString("username") != null)
            {
                if (!receiverPushHashMap.containsKey(this.userData.getString("username")))
                {
                    ReceiveService receiver = new ReceiveService(this.getApplicationContext(), this.userData
                            .getString("username"));
                    receiverPushHashMap.put(this.userData.getString("username"), receiver.startPush());
                }
            }

            if (!receiverPushHashMap.containsKey(getApplication().getPackageName()))
            {
                ReceiveService receiver = new ReceiveService(this.getApplicationContext(), getApplication()
                        .getPackageName());
                receiverPushHashMap.put(this.userData.getString("username"), receiver.startPush());
            }
            Log.e("消息服务","服务全部启动完成");
        }
        catch (Exception var2)
        {
            Log.e("eruntechMessageService:", var2.getMessage());
        }

    }

    @Override
    public void onLowMemory ()
    {
        super.onLowMemory();
    }

    /**
     * 定时启动一次任务，防止被JVM杀死
     */
    private void runRecevivePushTask ()
    {

        try
        {
            if (timer == null)
            {
                timer = new Timer();
            }

            timer.schedule(new TimerTask()
            {
                @Override
                public void run ()
                {
                    for (ReceiverPush pushMessage : receiverPushHashMap.values())
                    {
                        pushMessage.unBind();
                        Log.e("消息服务","服务终止");
                        pushMessage = null;
                    }
                    startReceiver();
//                    Intent mIntent = new Intent("com.eruntech.espushnotification.broadcast.NetworkConnectChangedReceiver");
//                    mIntent.setAction("eruntech.net.conn.PUSH_MESSAGE");
//                    PushMessageService.this.sendBroadcast(mIntent);
                }
            }, 500, 1000*60*30);

        }
        catch (Exception ex)
        {

        }
    }

    public boolean onUnbind (Intent intent)
    {
        Log.e("消息状态", "消息服务被卸载");
//        intent.setAction("eruntech.net.conn.PUSH_MESSAGE");
//        this.sendBroadcast(intent);
        return super.onUnbind(intent);
    }

    public void onDestroy ()
    {
        Log.e("消息服务：", "停止了");
//        for (ReceiverPush pushMessage : receiverPushHashMap.values())
//        {
//            pushMessage.unBind();
//            pushMessage = null;
//        }
//        receiverPushHashMap.clear();
        timer.cancel();
        timer.purge();

        Intent mIntent = new Intent("com.eruntech.espushnotification.broadcast.NetworkConnectChangedReceiver");
        mIntent.setAction("eruntech.net.conn.PUSH_MESSAGE");
        this.sendBroadcast(mIntent);
        super.onDestroy();
    }
}
