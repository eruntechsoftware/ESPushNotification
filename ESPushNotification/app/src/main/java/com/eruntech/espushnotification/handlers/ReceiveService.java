package com.eruntech.espushnotification.handlers;

import android.content.Context;

import com.eruntech.espushnotification.interfaces.IMessageObjSearcherHandler;
import com.eruntech.espushnotification.interfaces.IReceiveCallback;
import com.eruntech.espushnotification.notification.PushMessage;

import java.io.IOException;


/**
 * 消息接收服务
 * 2018/3/1.
 */
public class ReceiveService implements IReceiveCallback
{

    private static ObjectSearcherHandler objectSearcherHandler;
    private Context context;
    private String tag;
    /**
     * 消息接收服务
     * @param context 上下文
     * @param tag 消息标记
     * **/
    public ReceiveService (Context context,String tag)
    {
        this.context = context;
        this.tag = tag;
        if(objectSearcherHandler==null)
        {
            objectSearcherHandler = new ObjectSearcherHandler();

            //消息展示存储处理器
            objectSearcherHandler.add(new HPushMessageHandler());
        }
    }

    public void add(IMessageObjSearcherHandler messageObjSearcherHandler)
    {
        if(objectSearcherHandler==null)
        {
            objectSearcherHandler = new ObjectSearcherHandler();
        }
        objectSearcherHandler.add(messageObjSearcherHandler);
    }

    @Override
    public void callback (PushMessage message)
    {
        objectSearcherHandler.search(message);
    }

    /**
     * 启动群聊天接收器
     * **/
    public ReceiverPush startPush()
    {
        try
        {
            return new ReceiverPush(tag, ReceiveService.this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
