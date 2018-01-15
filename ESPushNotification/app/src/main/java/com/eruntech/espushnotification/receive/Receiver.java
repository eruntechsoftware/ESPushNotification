package com.eruntech.espushnotification.receive;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.eruntech.espushnotification.broadcast.NotificationBroadcastReceiver;
import com.eruntech.espushnotification.listener.ReceiveListener;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;


public class Receiver implements Consumer
{
    protected Channel channel;
    protected Connection connection;
    private Context context;
    private ReceiveListener receiveListener;
    private String exchangeName = "eruntech";

    public Receiver (Context context,final String receiverID) throws IOException
    {
        this.context = context;
        new Thread(new Runnable()
        {
            @Override
            public void run ()
            {
                try
                {
                    //打开连接和创建频道
                    ConnectionFactory factory = new ConnectionFactory();
                    //设置MabbitMQ所在主机ip或者主机名  127.0.0.1即localhost
                    factory.setHost("192.168.1.150");
//                    factory.setHost("202.136.60.9");
//                    factory.setPort(80);
                    factory.setUsername("admin");
                    factory.setPassword("admin");
                    //创建连接
                    connection = factory.newConnection();
                    //创建频道
                    channel = connection.createChannel();

                    channel.exchangeDeclare(exchangeName, "direct", true);
//                    channel.queueDeclare(receiverID,false,false,false,null);
                    channel.basicQos(1);
                    //绑定交换机和路由规则
                    AMQP.Queue.DeclareOk q = channel.queueDeclare();
                    String queue = q.getQueue();
                    channel.queueBind(queue, exchangeName, receiverID);
                    channel.basicConsume(queue, false, Receiver.this);
                }
                catch (Exception ex)
                {
                    Log.e("BaseConnector", ex.getMessage());
                }
            }
        }).start();

    }

    /**
     * 下面这些方法都是实现Consumer接口的
     **/
    //当消费者注册完成自动调用
    public void handleConsumeOk (String consumerTag)
    {
        System.out.println("Consumer " + consumerTag + " registered");
    }

    /**
     * @param consumerTag
     * @param env
     * @param props
     * @param body
     * @throws IOException
     */
    //当消费者接收到消息会自动调用
    public void handleDelivery (String consumerTag, Envelope env, BasicProperties props, byte[] body) throws IOException
    {
        String message = new String(body);
//        DataCollection params = new DataCollection();
//        params.add(new Data("userid", message.getToUserID()));
//        params.add(new Data("frienduserid", message.getFromUserID()));
//        params.add(new Data("text", message.getText()));
//        params.add(new Data("type", 1));
//        params.add(new Data("messagetype", message.getMessageType().toString()));
//        try
//        {
//            params.add(new Data("fileurl", message.getFileUrl()));
//            params.add(new Data("voicetime", message.getVoiceTime()));
//            params.add(new Data("time", message.getTime()));
//            params.add(new Data("messageid", message.getMessageID()));
//        }
//        catch (Exception ex)
//        {
//            Log.e("RECEIVER::::::", ex.getMessage());
//        }
//        MyApplication.DB.executeCursor("spMessageAdd", params);

//        EventBus.getDefault().post(message);
        System.out.println(message);
        long deliveryTag = env.getDeliveryTag();
        channel.basicAck(deliveryTag, false);

        Intent intentReceiver = new Intent(context, NotificationBroadcastReceiver.class);
        intentReceiver.setAction("NOTIFICATION_RECEIVER_MESSAGE");
        intentReceiver.putExtra("params", message);

        context.getApplicationContext().sendBroadcast(intentReceiver);

        Intent intentAllReceiver = new Intent("NOTIFICATION_RECEIVER_MESSAGE");
        intentAllReceiver.putExtra("params", message);
        context.getApplicationContext().sendBroadcast(intentAllReceiver);
        if(receiveListener!=null)
        {
            receiveListener.receive(message);
        }
    }

    //下面这些方法可以暂时不用理会
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

    /**
     * 设置接收消息事件接口
     * @param receiveListener 消息接收接口
     * **/
    public void setReceiveListener (ReceiveListener receiveListener)
    {
        this.receiveListener = receiveListener;
    }
}
