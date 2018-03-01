package com.eruntech.espushnotification.handlers;

import android.os.Handler;

import com.eruntech.espushnotification.interfaces.IMessageObjSearcherHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息接口循环类
 * 2018/2/26.
 */
public class ObjectSearcherHandler
{
    private Handler handler;
    private ArrayList<IMessageObjSearcherHandler> handlers;

    public ObjectSearcherHandler ()
    {
        this.handlers = new ArrayList<IMessageObjSearcherHandler>();
    }

    public ObjectSearcherHandler (List<IMessageObjSearcherHandler> handlers)
    {
        this.handlers = new ArrayList<IMessageObjSearcherHandler>(handlers);
    }

    public void search (byte[] message)
    {
        for (IMessageObjSearcherHandler handler : handlers)
        {
            if (handler.isPicked(message))
            {
                handler.handle(message);
            }
        }
    }

    public void add (IMessageObjSearcherHandler messageObjSearcherHandler)
    {
        if (handlers == null)
        {
            this.handlers = new ArrayList<IMessageObjSearcherHandler>(handlers);
        }
        handlers.add(messageObjSearcherHandler);
    }
}
