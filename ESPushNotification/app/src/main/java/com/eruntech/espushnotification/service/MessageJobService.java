package com.eruntech.espushnotification.service;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.eruntech.espushnotification.listener.ReceiveListener;
import com.eruntech.espushnotification.notification.PushMessage;
import com.eruntech.espushnotification.notification.PushNotificationBar;
import com.eruntech.espushnotification.receive.Receiver;
import com.eruntech.espushnotification.utils.PackgeManager;
import com.eruntech.espushnotification.utils.UserData;


/**
 * Created by Ming on 2018/1/10.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//API需要在21及以上
public class MessageJobService extends JobService implements ReceiveListener {
    private Receiver receiver;
    private String packgeName;
    private UserData userData;
    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            JobParameters param = (JobParameters)msg.obj;
            MessageJobService.this.jobFinished(param, true);
            MessageJobService.this.userData = new UserData(MessageJobService.this.getApplicationContext());
            Intent mIntent = new Intent("com.eruntech.espushnotification.service.MessageService");
            mIntent.setClass(MessageJobService.this.getApplicationContext(), MessageService.class);
            MessageJobService.this.getApplicationContext().stopService(mIntent);
            MessageJobService.this.startReceiver();
            return true;
        }
    });

    public MessageJobService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.packgeName = this.getPackageName();
        return START_STICKY;
    }

    public boolean onStartJob(JobParameters params) {
        Message m = Message.obtain();
        m.obj = params;
        this.handler.sendMessage(m);
        return true;
    }

    public boolean onStopJob(JobParameters params) {
        this.handler.removeCallbacksAndMessages((Object)null);
        return false;
    }

    public void startReceiver() {
        try {
            if(this.receiver == null) {
                this.receiver = new Receiver(this.getApplicationContext(), this.userData.getString("username"));
                this.receiver.setReceiveListener(this);
            }

            Receiver receiver1 = new Receiver(this.getApplicationContext(),getApplication().getPackageName());
            receiver1.setReceiveListener(this);
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
}
