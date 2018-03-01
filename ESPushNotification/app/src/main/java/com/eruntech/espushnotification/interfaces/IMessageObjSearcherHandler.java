package com.eruntech.espushnotification.interfaces;

/**
 * 消息接口循环
 * 2018/2/26.
 */
public interface IMessageObjSearcherHandler
{
    void handle (byte[] message);

    Boolean isPicked (byte[] message);
}
