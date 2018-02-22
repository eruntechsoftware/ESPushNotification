package com.eruntech.espushnotification.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.eruntech.espushnotification.listener.ReceiveListener;
import com.eruntech.espushnotification.notification.PushMessage;
import com.eruntech.espushnotification.notification.PushNotificationBar;
import com.eruntech.espushnotification.receive.ReceiverPushMessage;
import com.eruntech.espushnotification.utils.PackgeManager;
import com.eruntech.espushnotification.utils.UserData;


/**
 * 消息服务
 * 2017/11/16.
 */

public class MessageService extends Service implements ReceiveListener {
    private ReceiverPushMessage receiver;
    private ReceiverPushMessage receiverPush;
    private String packgeName;
    private UserData userData;

    public MessageService() {
    }

    public IBinder onBind(Intent intent) {
        this.userData = new UserData(this.getApplicationContext());
        this.packgeName = this.getPackageName();
        return null;
//        return new MessageService.MessageBinder();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        startReceiver();
        return super.onStartCommand(intent, flags, startId);
    }

    public void startReceiver() {
        try {
            if(this.userData.getString("username")!=null) {
                this.receiver = new ReceiverPushMessage(this.getApplicationContext(), this.userData.getString("username"));
                this.receiver.setReceiveListener(this);
            }

            if(receiverPush==null)
            {
                receiverPush = new ReceiverPushMessage(this.getApplicationContext(), getApplication().getPackageName());
//                receiverPush.setReceiveListener(this);
            }
        } catch (Exception var2) {
            Log.e("eruntechMessageService:", var2.getMessage());
        }

    }

    public void receive(String message) {
        PushMessage msg = null;

        try {
            if(message != null && message.length() > 0) {
                msg = PushMessage.jsonToPushMessage(message);
            }

            if(!PackgeManager.isCurrentAppPackgeName(this.getApplicationContext(), this.packgeName)) {
                PushNotificationBar.showNotification(this.getApplicationContext(), msg.getTitle(), msg.getContent(), msg.getParameter());
            }
        } catch (Exception var4) {
            Log.e("推送消息", var4.getMessage());
        }

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

    public class MessageBinder extends Binder implements IMessageBinder {
        public MessageBinder() {
        }

        public void invokeMethodInMessageService() {
            MessageService.this.startReceiver();
        }
    }
}
