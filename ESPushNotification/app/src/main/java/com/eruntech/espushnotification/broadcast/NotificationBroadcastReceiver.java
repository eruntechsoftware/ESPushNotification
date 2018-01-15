package com.eruntech.espushnotification.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationBroadcastReceiver extends BroadcastReceiver
{

    public static final String TYPE = "type"; //这个type是为了Notification更新信息的，这个不明白的朋友可以去搜搜，很多

    @Override
    public void onReceive (Context context, Intent intent)
    {
        String action = intent.getAction();
        int type = intent.getIntExtra(TYPE, -1);
        String params = intent.getStringExtra("params");

        if (type != -1)
        {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(type);
        }

        if (action.equals("NOTIFICATION_RECEIVER_MESSAGE"))
        {
            //处理滑动清除和点击删除事件
//            ToastHelper.toastShow(context, "接收到消息");
            receive(params);
        }

        if (action.equals("NOTIFICATION_CLICKED"))
        {
            //处理点击事件
//            ToastHelper.toastShow(context, "处理点击事件");
            clicked(params);
        }

        if (action.equals("NOTIFICATION_CANCELLED"))
        {
            //处理滑动清除和点击删除事件
//            ToastHelper.toastShow(context, "处理滑动清除和点击删除事件");
        }
    }

    /**
     * 点击通知栏消息的处理方法
     *
     * @param message 消息
     **/
    public void clicked (String message)
    {

    }

    /**
     * 收到通知后的处理方法
     *
     * @param message 消息
     */
    public void receive (String message)
    {

    }
}