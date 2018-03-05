package com.eruntech.espushnotification.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.eruntech.espushnotification.handlers.ReceiveService;
import com.eruntech.espushnotification.handlers.ReceiverPush;
import com.eruntech.espushnotification.utils.UserData;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * 消息服务
 * 2017/11/16.
 */

public class MessageService extends Service
{
    private static Context serviceContext;
    private String packgeName;
    private UserData userData;
    public static List<ReceiverPush> receiverPushMessageList = new LinkedList<>();

    public MessageService ()
    {
        serviceContext = getApplicationContext();
    }

    public static Context getServiceContext ()
    {
        return serviceContext;
    }

    public IBinder onBind (Intent intent)
    {
        this.userData = new UserData(this.getApplicationContext());
        this.packgeName = this.getPackageName();
        return null;
//        return new MessageService.MessageBinder();
    }

    public int onStartCommand (Intent intent, int flags, int startId)
    {

        startReceiver();
        return super.onStartCommand(intent, flags, startId);
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
                    ReceiveService receiver = new ReceiveService(this.getApplicationContext(), v);
                    receiver.startPush();
                }
            }

            if (this.userData.getString("username") != null)
            {
                ReceiveService receiver = new ReceiveService(this.getApplicationContext(), this.userData
                        .getString("username"));
                receiver.startPush();
            }

            ReceiveService receiver = new ReceiveService(this.getApplicationContext(), getApplication()
                    .getPackageName());
            receiver.startPush();

        }
        catch (Exception var2)
        {
            Log.e("eruntechMessageService:", var2.getMessage());
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
        for (ReceiverPush pushMessage : receiverPushMessageList)
        {
            pushMessage.unBind();
            pushMessage = null;
        }
        Intent mIntent = new Intent("com.eruntech.espushnotification.broadcast.NetworkConnectChangedReceiver");
        mIntent.setAction("eruntech.net.conn.PUSH_MESSAGE");
        this.sendBroadcast(mIntent);
        super.onDestroy();
    }

    public class MessageBinder extends Binder implements IMessageBinder
    {
        public MessageBinder ()
        {
        }

        public void invokeMethodInMessageService ()
        {
//            MessageService.this.startReceiver();
        }
    }
}
