package com.eruntech.espushnotification.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.eruntech.espushnotification.listener.ReceiveListener;
import com.eruntech.espushnotification.notification.PushMessage;
import com.eruntech.espushnotification.notification.PushNotificationBar;
import com.eruntech.espushnotification.receive.Receiver;
import com.eruntech.espushnotification.utils.PackgeManager;
import com.eruntech.espushnotification.utils.UserData;


/**
 * 消息服务
 * 2017/11/16.
 */

public class MessageService extends Service implements ReceiveListener
{
    private String packgeName;
    private UserData userData;

    @Override
    public IBinder onBind (Intent intent)
    {
        userData = new UserData(MessageService.this.getApplicationContext());
        packgeName = getPackageName();
        return new MessageBinder();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
//        return START_STICKY;
        return super.onStartCommand(intent,flags,startId);
    }

    /**
     * 启动消息接收器
     **/
    public void startReceiver ()
    {
        try
        {
            Receiver receiver = new Receiver(this.getApplicationContext(), userData.getString("username"));
            receiver.setReceiveListener(this);

//            Receiver receiver1 = new Receiver(this.getApplicationContext(), this.getApplication().getPackageName());
//            receiver1.setReceiveListener(this);
            // Service被启动时，将会有弹出消息提示[c]
//            Toast.makeText(this, "[开启我的服务]", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Log.e("eruntechMessageService:", ex.getMessage());
        }
    }

    @Override
    public void receive (String message)
    {
        PushMessage msg = null;
        try
        {
            if(message!=null && message.length()>0)
            {
                msg = PushMessage.jsonToPushMessage(message);
            }

            //如果当前屏幕上运行的不是推送应对的程序，则显示推送消息
            if (!PackgeManager.isCurrentAppPackgeName(getApplicationContext(), packgeName))
            {
                PushNotificationBar.showNotification(this.getApplicationContext(), msg.getTitle(),msg.getContent(),msg.getParameter());
            }
        }
        catch (Exception ex)
        {
            Log.e("推送消息", ex.getMessage());
        }
    }

    @Override
    public boolean onUnbind (Intent intent)
    {
        Log.e("消息状态", "消息服务被卸载");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy ()
    {
        Log.e("消息服务：", "停止了");
        super.onDestroy();

    }
    public class MessageBinder extends Binder implements IMessageBinder
    {
        @Override
        public void invokeMethodInMessageService ()
        {
            startReceiver();
        }
    }
}
