package com.eruntech.espushnotification.interfaces;

import com.eruntech.espushnotification.notification.PushMessage;

/**
 * 消息接口循环
 * 2018/2/26.
 */
public interface IMessageObjSearcherHandler
{
    void handle (PushMessage message);

    Boolean isPicked (PushMessage message);
}
