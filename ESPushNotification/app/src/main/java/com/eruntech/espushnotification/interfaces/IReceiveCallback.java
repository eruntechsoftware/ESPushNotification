package com.eruntech.espushnotification.interfaces;

import com.eruntech.espushnotification.notification.PushMessage;

/**
 * 接收消息的回调处理方法
 */
public interface IReceiveCallback
{

    /**
     * 消息回调处理
     * @param message 消息
     * **/
    public void callback (PushMessage message);
}
