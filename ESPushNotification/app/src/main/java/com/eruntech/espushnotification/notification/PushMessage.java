package com.eruntech.espushnotification.notification;

import com.google.gson.Gson;

import java.nio.charset.Charset;

/**
 * 推送消息
 * 杜明悦
 * 2018/1/10.
 */

public class PushMessage
{
    private String title;
    private String content;
    private String parameter;

    /**
     * 推送通知
     * @param title 标题
     * @param content 内容
     * @param parameter 参数
     * **/
    public PushMessage(String title,String content,String parameter)
    {
        this.title = title;
        this.content = content;
        this.parameter = parameter;
    }

    /**
     * 设置标题
     * @param title 标题
     * **/
    public void setTitle (String title)
    {
        this.title = title;
    }

    /**
     * 获取标题
     * **/
    public String getTitle ()
    {
        return title;
    }

    /**
     * 设置内容
     * @param content 内容
     * **/
    public void setContent (String content)
    {
        this.content = content;
    }

    /**
     * 获取内容
     * **/
    public String getContent ()
    {
        return content;
    }

    public void setParameter (String parameter)
    {
        this.parameter = parameter;
    }

    public String getParameter ()
    {
        return parameter;
    }

    /**
     * 转换json字符串为Message
     * @param json json字符串
     * **/
    public static PushMessage jsonToPushMessage(String json)
    {
        PushMessage message = null;
        if(json!=null && json.length()>0)
        {
            Gson gson = new Gson();
            message = gson.fromJson(json,PushMessage.class);
        }
        return message;
    }

    /**
     * 字节流转对象
     * @param bytes
     * @return
     */
    public static PushMessage bytesToPushMessage(byte[] bytes)
    {
        PushMessage obj = null;
        try
        {
            String jsonstr = new String(bytes, Charset.forName("utf-8")).replace('\u0000', ' ').trim();
            if(jsonstr!=null)
            {
                obj = jsonToPushMessage(jsonstr);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return obj;
    }
}
