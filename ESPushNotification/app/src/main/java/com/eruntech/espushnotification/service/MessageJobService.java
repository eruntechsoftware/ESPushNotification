package com.eruntech.espushnotification.service;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

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
public class MessageJobService extends JobService implements ReceiveListener
{
    private Receiver receiver;
    private String packgeName;
    private UserData userData;


    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage (Message msg)
        {

            JobParameters param = (JobParameters) msg.obj;
            jobFinished(param, true);

            userData = new UserData(MessageJobService.this.getApplicationContext());

            startReceiver ();
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            return true;
        }
    });

    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
        packgeName = getPackageName();
        return START_STICKY;
    }

    @Override
    public boolean onStartJob (JobParameters params)
    {
        Message m = Message.obtain();
        m.obj = params;
        handler.sendMessage(m);
        return true;
    }

    @Override
    public boolean onStopJob (JobParameters params)
    {
        handler.removeCallbacksAndMessages(null);
        return false;
    }

    /**
     * 启动消息接收器
     **/
    public void startReceiver ()
    {
        try
        {
            if(receiver==null)
            {
                //User.getUserID(getApplicationContext())
                receiver = new Receiver(this.getApplicationContext(), userData.getString("username"));
                receiver.setReceiveListener(this);
            }
            // Service被启动时，将会有弹出消息提示[c]
            Toast.makeText(this, "[开启我的服务]", Toast.LENGTH_LONG).show();
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
                PushNotificationBar.showNotification(this.getApplicationContext(), msg.getTitle(), msg.getContent(), msg.getParameter());
            }
        }
        catch (Exception ex)
        {
            Log.e("推送消息", ex.getMessage());
        }
    }
}
