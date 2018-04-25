package com.eruntech.espushnotification.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eruntech.espushnotification.interfaces.ITask;
import com.eruntech.espushnotification.task.TaskPool;

import java.util.Timer;


/**
 * 心跳服务
 * on 2018/2/26.
 */

public class TaskService extends Service
{
    private static Context serviceContext;
    private Timer timer;
    @Nullable
    @Override
    public IBinder onBind (Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate ()
    {
        super.onCreate();
    }

    public static Context getServiceContext ()
    {
        return serviceContext;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
        try
        {
            serviceContext = getApplicationContext();
            startTask ();
        }
        catch (Exception ex)
        {
        }
        return START_STICKY;
    }

    public void startTask ()
    {
        try
        {
            if(TaskPool.getTaskList().size()>0)
            {
                for(ITask task:TaskPool.getTaskList())
                {
                    if(!task.isRuning())
                    {
                        task.run();
                    }
                }
            }
            Log.e("消息服务","服务全部启动完成");
        }
        catch (Exception var2)
        {
            Log.e("eruntechMessageService:", var2.getMessage());
        }

    }

    @Override
    public void onLowMemory ()
    {
        super.onLowMemory();
    }

    public boolean onUnbind (Intent intent)
    {
        Log.e("消息状态", "消息服务被卸载");
//        intent.setAction("eruntech.net.conn.PUSH_MESSAGE");
//        this.sendBroadcast(intent);
        return super.onUnbind(intent);
    }

    public void onDestroy ()
    {
        Log.e("消息服务：", "停止了");
//        timer.cancel();
//        timer.purge();
        super.onDestroy();
    }
}
