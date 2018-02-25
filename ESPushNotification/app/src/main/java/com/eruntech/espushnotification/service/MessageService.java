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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * 消息服务
 * 2017/11/16.
 */

public class MessageService extends Service implements ReceiveListener {

    private String packgeName;
    private UserData userData;
    private List<ReceiverPushMessage> receiverPushMessageList = new LinkedList<>();
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
            this.userData = new UserData(this.getApplicationContext());
            //群组标签
            if(userData!=null && this.userData.getStringSet("grouptags")!=null)
            {
                Set<String> sets = this.userData.getStringSet("grouptags");
                for(String v:sets)
                {
                    ReceiverPushMessage receiver = new ReceiverPushMessage(this.getApplicationContext(), v);
                    receiver.setReceiveListener(this);
                    receiverPushMessageList.add(receiver);
                }
            }

            if(this.userData.getString("username")!=null) {
                ReceiverPushMessage receiver = new ReceiverPushMessage(this.getApplicationContext(), this.userData.getString("username"));
                receiver.setReceiveListener(this);
                receiverPushMessageList.add(receiver);
            }


            ReceiverPushMessage receiverPush = new ReceiverPushMessage(this.getApplicationContext(), getApplication().getPackageName());
            receiverPush.setReceiveListener(this);
            receiverPushMessageList.add(receiverPush);

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
//        intent.setAction("eruntech.net.conn.PUSH_MESSAGE");
//        this.sendBroadcast(intent);
        return super.onUnbind(intent);
    }

    public void onDestroy() {
        Log.e("消息服务：", "停止了");
        for (ReceiverPushMessage pushMessage:receiverPushMessageList)
        {
            pushMessage.unBind();
            pushMessage=null;
        }
        Intent intent = new Intent();
        intent.setAction("eruntech.net.conn.PUSH_MESSAGE");
        this.sendBroadcast(intent);
        super.onDestroy();
    }

    public class MessageBinder extends Binder implements IMessageBinder {
        public MessageBinder() {
        }

        public void invokeMethodInMessageService() {
//            MessageService.this.startReceiver();
        }
    }
}
