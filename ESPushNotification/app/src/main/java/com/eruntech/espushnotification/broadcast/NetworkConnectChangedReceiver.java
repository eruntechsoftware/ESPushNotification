package com.eruntech.espushnotification.broadcast;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.birthstone.core.helper.ToastHelper;
import com.eruntech.espushnotification.service.IMessageBinder;
import com.eruntech.espushnotification.service.MessageJobService;
import com.eruntech.espushnotification.service.MessageService;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

/**
 * Created by Ming on 2017/11/17.
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver
{
    private IMessageBinder messageBinder;
    private NetworkConnectChangedReceiver.MessageServiceConnection messageServiceConnection;
    private static final String TAG = "Eruntech";
    public static final String TAG1 = "NetWork";

    @Override
    public void onReceive (Context context, Intent intent)
    {
        try
        {
            context.startService(new Intent(context,MessageService.class));

//            if(intent.getAction().equals("eruntech.net.conn.PUSH_START_SERVICE"))
//            {
//                context.startService(new Intent(context, MessageService.class));
//            }
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(context.getPackageName(), MessageJobService.class
                    .getName()))
                        .setPeriodic(1000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)//运行的网络环境
                        .setMinimumLatency(3000)// 设置任务运行最少延迟时间
//                        .setOverrideDeadline(5000)// 设置deadline，若到期还没有达到规定的条件则会开始执行
                        .setRequiresCharging(false)// 设置是否充电的条件,默认false
                        .setRequiresDeviceIdle(false)// 设置手机是否空闲的条件,默认false
                    .setPersisted(true)//设备重启之后你的任务是否还要继续执行
                    .build();
            int value = jobScheduler.schedule(jobInfo);
            if (value < 0)
            {
                ToastHelper.toastShow(context, "服务启动失败");
            }

//            messageServiceConnection = new NetworkConnectChangedReceiver.MessageServiceConnection();
//            Context localContext = context.getApplicationContext();
//            Intent mIntent = new Intent("com.eruntech.espushnotification.service.MessageService");
//            mIntent.setClass(localContext, MessageService.class);
//            mIntent.setAction("android.net.conn.CONNECTIVITY_CHANGE");//Service能够匹配的Action
//            mIntent.setPackage("com.eruntech.espushnotification");
//            localContext.bindService(mIntent, messageServiceConnection, Context.BIND_AUTO_CREATE);
//            localContext.startService(mIntent);
            //Service被启动时，将会有弹出消息提示
//            Toast.makeText(context, "[开启我的服务]", Toast.LENGTH_LONG).show();

        }
        catch (Exception ex)
        {
            Log.e("服务启动失败：", ex.getMessage());
        }
    }

    public class MessageServiceConnection implements ServiceConnection
    {

        public void onServiceConnected (ComponentName componentName, IBinder iBinder)
        {
            try
            {
                Log.e("服务状态：", "服务启动了");
                messageBinder = (IMessageBinder) iBinder;
                messageBinder.invokeMethodInMessageService();
            }
            catch (Exception ex)
            {
                Log.e("服务启动失败：", ex.getMessage());
            }
        }

        public void onServiceDisconnected (ComponentName var1)
        {
            Log.e("服务状态：", "服务关闭了");
        }
    }
}