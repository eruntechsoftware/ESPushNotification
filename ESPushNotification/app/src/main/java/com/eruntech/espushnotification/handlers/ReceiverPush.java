package com.eruntech.espushnotification.handlers;

import android.util.Log;

import com.eruntech.espushnotification.interfaces.IReceiveCallback;
import com.eruntech.espushnotification.notification.PushMessage;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;


public class ReceiverPush implements Consumer
{
    protected Channel channel;
    protected Connection connection;
    private String exchangeName = "eruntechpush";
    private String tag;
    private IReceiveCallback receiveCallback;

    public ReceiverPush (String tag,IReceiveCallback receiveCallback) throws IOException
    {
        this.tag = tag;
        this.receiveCallback = receiveCallback;
        new Thread(new Runnable()
        {
            public void run ()
            {
                try
                {

                    //打开连接和创建频道
                    ConnectionFactory factory = new ConnectionFactory();
                    //设置MabbitMQ所在主机ip或者主机名  127.0.0.1即localhost
                    factory.setHost("47.104.78.112");
                    factory.setUsername("admin");
                    factory.setPassword("7YWMenHqJXMtgQM8");
                    factory.setVirtualHost("pushmessage");
                    //创建连接
                    connection = factory.newConnection();
                    //创建频道
                    channel = connection.createChannel();

//                    channel.exchangeDeclare(exchangeName, "direct", true);
//                    channel.queueDeclare(ReceiverPush.this.tag, false, false, false, null);
//                    channel.basicQos(1);
//                    channel.basicConsume(ReceiverPush.this.tag, false, ReceiverPush.this);

                    channel.exchangeDeclare(exchangeName, "direct",true);
                    channel.basicQos(1);
                    //绑定交换机和路由规则
                    AMQP.Queue.DeclareOk q = channel.queueDeclare();
                    String queue = q.getQueue();
                    channel.queueBind(queue, exchangeName, ReceiverPush.this.tag);
                    channel.basicConsume(queue, false, ReceiverPush.this);
                }
                catch (Exception var4)
                {
                    Log.e("BaseConnector", var4.getMessage());
                }

            }
        }).start();
    }

    public void handleConsumeOk (String consumerTag)
    {
        System.out.println("Consumer " + consumerTag + " registered");
    }

    public void handleDelivery (String consumerTag, Envelope env, BasicProperties props, byte[] body) throws IOException
    {
        this.channel.basicAck(env.getDeliveryTag(), false);
        PushMessage message = PushMessage.bytesToPushMessage(body);

        if (receiveCallback != null)
        {
            receiveCallback.callback(body);
        }
    }

    public void handleCancelOk (String consumerTag)
    {
    }

    public void handleCancel (String consumerTag) throws IOException
    {
    }

    public void handleShutdownSignal (String consumerTag, ShutdownSignalException sig)
    {
    }

    public void handleRecoverOk (String consumerTag)
    {
    }

    //卸载绑定，并清理内存
    public void unBind ()
    {
        try
        {
            if (channel != null)
            {
                channel.exchangeUnbind(tag, exchangeName, tag);
                channel = null;
            }

            if (connection != null)
            {
                connection.close();
                connection.abort();
                connection = null;
            }
        }
        catch (Exception ex)
        {

        }
    }
}
