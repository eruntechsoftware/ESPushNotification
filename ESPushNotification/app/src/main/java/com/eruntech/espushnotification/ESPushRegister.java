package com.eruntech.espushnotification;

import android.content.Context;
import android.content.Intent;

import com.eruntech.espushnotification.service.PushMessageService;
import com.eruntech.espushnotification.utils.UserData;

import java.util.HashSet;
import java.util.Set;

/**
 * 推送消息注册
 * 2018/1/11.
 */
public class ESPushRegister
{

    /**
     * 注册推送标记
     *
     * @param context 上下文
     * @param tag     标记
     **/
    public static void registe (Context context, String tag)
    {
        try
        {
            UserData userData = new UserData(context);
            userData.put("username", tag);

            Intent intent = new Intent();
            intent.setAction("eruntech.net.conn.PUSH_MESSAGE");
            context.sendBroadcast(intent);
        }
        catch (Exception ex)
        {

        }
    }

    /**
     * 注册推送标记
     *
     * @param context 上下文
     * @param tags     标记
     **/
    public static void registe (Context context, Object[] tags)
    {
        try
        {
            Set<String> tagsets = new HashSet<String>();
            for (Object obj:tags)
            {
                tagsets.add(obj.toString());
            }
            UserData userData = new UserData(context);
            userData.put("grouptags", tagsets);

            Intent serviceIntent = new Intent(context, PushMessageService.class);
            context.startService(serviceIntent);
        }
        catch (Exception ex)
        {

        }
    }

    /**
     * 系统初始化
     *
     * @param context 应用程序上下文
     **/
    public static void init (Context context)
    {
        try
        {
            Intent intent = new Intent();
            intent.setAction("eruntech.net.conn.PUSH_INIT");
            context.sendBroadcast(intent);
        }
        catch (Exception ex)
        {

        }
    }
}
