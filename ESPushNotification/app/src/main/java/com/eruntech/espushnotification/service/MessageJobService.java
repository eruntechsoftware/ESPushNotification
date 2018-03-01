package com.eruntech.espushnotification.service;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.eruntech.espushnotification.handlers.ReceiveService;
import com.eruntech.espushnotification.handlers.ReceiverPush;
import com.eruntech.espushnotification.utils.UserData;

import java.util.Set;


/**
 * Created by Ming on 2018/1/10.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)//API需要在21及以上
public class MessageJobService extends JobService{
    private ReceiverPush receiver;

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

    public boolean onUnbind(Intent intent) {
        Log.e("消息状态", "消息服务被卸载");
//        intent.setAction("eruntech.net.conn.PUSH_MESSAGE");
//        this.sendBroadcast(intent);
        return super.onUnbind(intent);
    }

    public void onDestroy() {
        Log.e("消息服务：", "停止了");
//        Intent intent = new Intent();
//        intent.setAction("eruntech.net.conn.PUSH_MESSAGE");
//        this.sendBroadcast(intent);
        super.onDestroy();
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
}
