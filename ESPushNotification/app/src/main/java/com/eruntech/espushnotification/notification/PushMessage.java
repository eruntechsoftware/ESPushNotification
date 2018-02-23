package com.eruntech.espushnotification.notification;

import com.google.gson.Gson;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * 推送消息
 * 杜明悦
 * 2018/1/10.
 */

public class PushMessage
{
    private String tag;
    private String title;
    private String content;
    private String imageURL;
    private Map<String,String> parameter;

    /**
     * 推送通知
     * @param tag 标记,消息接收者
     * @param title 标题
     * @param content 内容
     * @param imageURL 消息通知栏图片
     * @param parameter 参数
     * **/
    public PushMessage(String tag,String title,String content, String imageURL, Map<String,String> parameter)
    {
        this.tag = tag;
        this.title = title;
        this.content = content;
        this.imageURL = imageURL;
        this.parameter = parameter;
    }

    public void setTag (String tag)
    {
        this.tag = tag;
    }

    public String getTag ()
    {
        return tag;
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

    /**
     * 设置消息图片
     * @param imageURL 消息图片
     * **/
    public void setImageURL (String imageURL)
    {
        this.imageURL = imageURL;
    }

    /**
     * 获取消息图片
     * **/
    public String getImageURL ()
    {
        return imageURL;
    }

    public void setParameter (Map<String,String> parameter)
    {
        this.parameter = parameter;
    }

    public Map<String,String> getParameter ()
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

    /**
     * 消息类转字节流
     * @return byte[]
     */
    public byte[] toBytes()
    {
        byte[] bytes = null;
        try
        {
            bytes = this.toJsonString().getBytes();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 转换为json字符串
     * **/
    public String toJsonString()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
