package com.eruntech.espushnotification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.eruntech.espushnotification.service.PushMessageService;
import com.eruntech.espushnotification.utils.HwPushManager;
import com.eruntech.espushnotification.utils.JobSchedulerManager;
import com.eruntech.espushnotification.utils.ScreenManager;

/**
 * Created by Ming on 2017/11/17.
 */

public class StartRunMessageServiceReceiver extends BroadcastReceiver implements ScreenReceiverUtil.SreenStateListener
{
    // 动态注册锁屏等广播
    private ScreenReceiverUtil mScreenListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;
    // JobService，执行系统任务
    private JobSchedulerManager mJobManager;
    // 华为推送管理类
    private HwPushManager mHwPushManager;
    private Handler handler;
    private static final String TAG = "Eruntech";
    public static final String TAG1 = "NetWork";

    @Override
    public void onReceive (final Context context, Intent intent)
    {
        try
        {
            // 1. 注册锁屏广播监听器
            mScreenListener = new ScreenReceiverUtil(context);
            mScreenManager = ScreenManager.getScreenManagerInstance(context);
            mScreenListener.setScreenReceiverListener(this);
            // 2. 启动系统任务
            mJobManager = JobSchedulerManager.getJobSchedulerInstance(context);
            mJobManager.startJobScheduler();
            // 3. 华为推送保活，允许接收透传
            mHwPushManager = HwPushManager.getInstance(context);
            mHwPushManager.startRequestToken();
            mHwPushManager.isEnableReceiveNormalMsg(true);
            mHwPushManager.isEnableReceiverNotifyMsg(true);

            final Intent serviceIntent = new Intent(context, PushMessageService.class);
            context.startService(serviceIntent);
            if (handler == null)
            {
                handler = new Handler(new Handler.Callback()
                {
                    @Override
                    public boolean handleMessage (Message message)
                    {
                        context.startService(serviceIntent);
                        return true;
                    }
                });
                handler.sendEmptyMessageDelayed(0, 1000);
            }

//            messageServiceConnection = new StartRunMessageServiceReceiver.MessageServiceConnection();
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

    @Override
    public void onSreenOn ()
    {
        // 亮屏，移除"1像素"
        mScreenManager.finishActivity();
    }

    @Override
    public void onSreenOff ()
    {
        // 接到锁屏广播，将SportsActivity切换到可见模式
        // "咕咚"、"乐动力"、"悦动圈"就是这么做滴
//            Intent intent = new Intent(SportsActivity.this,SportsActivity.class);
//            startActivity(intent);
        // 如果你觉得，直接跳出SportActivity很不爽
        // 那么，我们就制造个"1像素"惨案
        mScreenManager.startActivity();
    }

    @Override
    public void onUserPresent ()
    {
        // 解锁，暂不用，保留
    }
}