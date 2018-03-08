package com.eruntech.espushnotification;

import android.content.Context;
import android.content.Intent;

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

            Intent mIntent = new Intent("com.eruntech.espushnotification.receiver.StartRunMessageServiceReceiver");
            mIntent.setAction("eruntech.net.conn.PUSH_MESSAGE");
            context.sendBroadcast(mIntent);
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

            Intent mIntent = new Intent("com.eruntech.espushnotification.receiver.StartRunMessageServiceReceiver");
            mIntent.setAction("eruntech.net.conn.PUSH_MESSAGE");
            context.sendBroadcast(mIntent);
        }
        catch (Exception ex)
        {

        }
    }
}
