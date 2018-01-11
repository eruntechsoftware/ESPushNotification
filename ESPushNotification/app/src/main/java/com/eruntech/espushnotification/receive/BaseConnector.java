package com.eruntech.espushnotification.receive;

import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class BaseConnector
{
    protected Channel channel;
    protected Connection connection;
    protected String queueName;

    public BaseConnector (String queueName) throws IOException
    {
        this.queueName = queueName;
        new Thread()
        {
            @Override
            public void run ()
            {
                try
                {
                    super.run();
                    //打开连接和创建频道
                    ConnectionFactory factory = new ConnectionFactory();
                    //设置MabbitMQ所在主机ip或者主机名  127.0.0.1即localhost
                    factory.setHost("192.168.1.150");
                    factory.setUsername("admin");
                    factory.setPassword("admin");
                    //创建连接
                    connection = factory.newConnection();
                    //创建频道
                    channel = connection.createChannel();
                    //声明创建队列
//                    channel.queueDeclare(queueName, false, false, false, null);
                }
                catch (Exception ex)
                {
                    Log.e("BaseConnector", ex.getMessage());
                }
            }
        }.start();
    }
}
