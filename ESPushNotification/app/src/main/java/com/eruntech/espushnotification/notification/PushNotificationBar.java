package com.eruntech.espushnotification.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.eruntech.espushnotification.R;
import com.eruntech.espushnotification.broadcast.NotificationBroadcastReceiver;
import com.google.gson.Gson;

import java.util.Map;


/**
 * 推送消息通知
 * 杜明悦
 * 2018/1/10.
 */

public class PushNotificationBar
{
    public static void showNotification (Context mContext,String title, String text, Map<String,String> map)
    {
        Intent intentClick = new Intent("NOTIFICATION_CLICKED");
        intentClick.putExtra(NotificationBroadcastReceiver.TYPE, 1);
        intentClick.putExtra("params", new Gson().toJson(map));

        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(mContext, 0, intentClick, PendingIntent.FLAG_ONE_SHOT);

        Intent intentCancel = new Intent("NOTIFICATION_CANCELLED");
        intentCancel.putExtra(NotificationBroadcastReceiver.TYPE, -1);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(mContext, 0, intentCancel, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntentClick)
                .setDeleteIntent(pendingIntentCancel);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());  //这就是那个type，相同的update，不同add
    }
}
