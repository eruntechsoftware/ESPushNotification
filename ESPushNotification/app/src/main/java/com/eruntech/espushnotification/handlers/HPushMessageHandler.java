package com.eruntech.espushnotification.handlers;

import android.content.Intent;
import android.util.Log;

import com.eruntech.espushnotification.interfaces.IMessageObjSearcherHandler;
import com.eruntech.espushnotification.service.PushMessageService;

import java.nio.charset.Charset;

/**
 * 个人聊天消息，将接收到的消息通知服务端当前消息已经接收
 * 2018/2/28.
 */
public class HPushMessageHandler implements IMessageObjSearcherHandler
{

    private String message;
    public HPushMessageHandler ()
    {
        try
        {

        }
        catch (Exception ex)
        {
        }
    }


    @Override
    public void handle (byte[] body)
    {
        try
        {

            Intent intentAllReceiver = new Intent("NOTIFICATION_RECEIVER_MESSAGE");
            intentAllReceiver.putExtra("params", message);
            PushMessageService.getServiceContext().sendBroadcast(intentAllReceiver);
        }
        catch (Exception var4)
        {
            Log.e("推送消息", var4.getMessage());
        }
    }

    @Override
    public Boolean isPicked (byte[] body)
    {
        message = new String(body, Charset.forName("gbk"));
        //判断消息是个人消息
        if (message != null)
        {
            return true;
        }
        return false;
    }
}
