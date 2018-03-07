package com.eruntech.espushnotification.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.eruntech.espushnotification.handlers.ReceiverPush;
import com.eruntech.espushnotification.service.PushMessageService;

import static com.eruntech.espushnotification.service.PushMessageService.receiverPushHashMap;

/**
 * Created by Ming on 2017/11/17.
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver
{
    private Handler handler;
    private static final String TAG = "Eruntech";
    public static final String TAG1 = "NetWork";

    @Override
    public void onReceive (final Context context, Intent intent)
    {
        try
        {
            final Intent serviceIntent = new Intent(context, PushMessageService.class);
            context.startService(serviceIntent);
            if(handler==null)
            {
                handler = new Handler(new Handler.Callback()
                {
                    @Override
                    public boolean handleMessage (Message message)
                    {
                        for (ReceiverPush pushMessage : receiverPushHashMap.values())
                        {
                            pushMessage.unBind();
                            Log.e("消息服务","服务终止");
                            pushMessage = null;
                        }
                        context.startService(serviceIntent);
                        return true;
                    }
                });
                handler.sendEmptyMessageDelayed(0, 1000);
            }

//            messageServiceConnection = new NetworkConnectChangedReceiver.MessageServiceConnection();
//            Context localContext = context.getApplicationContext();
//            Intent mIntent = new Intent("com.eruntech.espushnotification.service.MessageService");
//            mIntent.setClass(localContext, MessageService.class);
//            mIntent.setAction("android.net.conn.CONNECTIVITY_CHANGE");//Service能够匹配的Action
//            mIntent.setPackage("com.eruntech.espushnotification");
//            localContext.bindService(mIntent, messageServiceConnection, Context.BIND_AUTO_CREATE);
//            localContext.startService(mIntent);

//            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
//            JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(context.getPackageName(), MessageJobService.class
//                    .getName()))
////                        .setPeriodic(1000)
//                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)//运行的网络环境
////                        .setMinimumLatency(3000)// 设置任务运行最少延迟时间
////                        .setOverrideDeadline(5000)// 设置deadline，若到期还没有达到规定的条件则会开始执行
////                        .setRequiresCharging(false)// 设置是否充电的条件,默认false
////                        .setRequiresDeviceIdle(false)// 设置手机是否空闲的条件,默认false
//                    .setPersisted(true)//设备重启之后你的任务是否还要继续执行
//                    .build();
//            int value = jobScheduler.schedule(jobInfo);
//            if (value < 0)
//            {
//                ToastHelper.toastShow(context, "服务启动失败");
//            }



            //Service被启动时，将会有弹出消息提示
//            Toast.makeText(context, "[开启我的服务]", Toast.LENGTH_LONG).show();

        }
        catch (Exception ex)
        {
            Log.e("服务启动失败：", ex.getMessage());
        }
    }
}