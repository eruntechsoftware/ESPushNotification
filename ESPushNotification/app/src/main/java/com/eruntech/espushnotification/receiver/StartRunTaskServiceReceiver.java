package com.eruntech.espushnotification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.eruntech.espushnotification.service.TaskService;
import com.eruntech.espushnotification.utils.HwPushManager;
import com.eruntech.espushnotification.utils.JobSchedulerManager;
import com.eruntech.espushnotification.utils.ScreenManager;

/**
 * 启动广播接收服务
 * 2017/11/17.
 */

public class StartRunTaskServiceReceiver extends BroadcastReceiver implements ScreenReceiverUtil.SreenStateListener
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
            if (intent.getAction().equals("eruntech.net.conn.PUSH_MESSAGE"))
            {
                // 1. 注册锁屏广播监听器
                new Thread(new Runnable()
                {
                    @Override
                    public void run ()
                    {
                        mScreenListener = new ScreenReceiverUtil(context);
                        mScreenManager = ScreenManager.getScreenManagerInstance(context);
                        mScreenListener.setScreenReceiverListener(StartRunTaskServiceReceiver.this);
                        // 2. 启动系统任务
                        mJobManager = JobSchedulerManager.getJobSchedulerInstance(context);
                        mJobManager.startJobScheduler();

                        // 那么，我们就制造个"1像素"惨案
                        mScreenManager.startActivity();

                        // 3. 华为推送保活，允许接收透传
                        mHwPushManager = HwPushManager.getInstance(context);
                        mHwPushManager.startRequestToken();
                        mHwPushManager.isEnableReceiveNormalMsg(true);
                        mHwPushManager.isEnableReceiverNotifyMsg(true);
                    }
                }).start();


                Intent serviceIntent = new Intent(context, TaskService.class);
                context.stopService(serviceIntent);
                context.startService(serviceIntent);
                mScreenManager.finishActivity();
            }
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