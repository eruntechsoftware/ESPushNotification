package com.eruntech.espushnotification.handlers;

import android.util.Log;

import com.eruntech.espushnotification.interfaces.IMessageObjSearcherHandler;
import com.eruntech.espushnotification.notification.PushMessage;
import com.eruntech.espushnotification.notification.PushNotificationBar;
import com.eruntech.espushnotification.service.MessageService;

/**
 * 个人聊天消息，将接收到的消息通知服务端当前消息已经接收
 * 2018/2/28.
 */
public class HPushMessageHandler implements IMessageObjSearcherHandler
{

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
    public void handle (PushMessage message)
    {
        try
        {
//            if (!PackgeManager.isCurrentAppPackgeName(MessageService.getServiceContext(), MessageService.getServiceContext().getPackageName()))
//            {
                PushNotificationBar.showNotification(MessageService.getServiceContext(), message.getTitle(), message
                        .getContent(), message.getParameter());
//            }
        }
        catch (Exception var4)
        {
            Log.e("推送消息", var4.getMessage());
        }
    }

    @Override
    public Boolean isPicked (PushMessage message)
    {
        //判断消息是个人消息
        if (message != null)
        {
            return true;
        }
        return false;
    }
}
