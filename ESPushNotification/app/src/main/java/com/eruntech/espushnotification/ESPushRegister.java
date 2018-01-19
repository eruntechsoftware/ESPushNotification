package com.eruntech.espushnotification;

import android.content.Context;
import android.content.Intent;

import com.eruntech.espushnotification.utils.UserData;

/**
 * 推送消息注册
 * 2018/1/11.
 */
public class ESPushRegister
{

    /**
     * 注册推送标记
     * @param context 上下文
     * @param tag 标记
     * **/
    public static void registe(Context context,String tag)
    {
        try
        {
            UserData userData = new UserData(context);
            userData.put("username",tag);

            Intent intent = new Intent();
            intent.setAction("eruntech.net.conn.PUSH_MESSAGE");
            context.sendBroadcast(intent);
        }
        catch (Exception ex)
        {

        }
    }

    /**
     * 系统初始化
     * @param context 应用程序上下文
     * **/
    public static void init(Context context)
    {
        try
        {
            Intent intent = new Intent();
            intent.setAction("eruntech.net.conn.PUSH_MESSAGE");
            context.sendBroadcast(intent);
        }
        catch (Exception ex)
        {

        }
    }
}
