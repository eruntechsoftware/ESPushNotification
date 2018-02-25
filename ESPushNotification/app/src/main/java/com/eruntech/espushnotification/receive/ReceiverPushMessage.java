package com.eruntech.espushnotification.receive;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.Map;


public class ReceiverPushMessage implements Consumer {
    protected Channel channel;
    protected Connection connection;
    private Context context;
    private AMQP.Queue.DeclareOk offlineMsg;
    private ReceiveListener receiveListener;
    private String exchangeName = "eruntechpush";
    private String receiverID,queue;

    public ReceiverPushMessage (Context context, final String receiverID) throws IOException {
        this.receiverID = receiverID;
        this.context = context;
        (new Thread(new Runnable() {
            public void run() {
                try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("47.104.78.112");
                    factory.setUsername("admin");
                    factory.setPassword("7YWMenHqJXMtgQM8");
                    factory.setVirtualHost("pushmessage");
                    ReceiverPushMessage.this.connection = factory.newConnection();
                    ReceiverPushMessage.this.channel = ReceiverPushMessage.this.connection.createChannel();
                    ReceiverPushMessage.this.channel.exchangeDeclare(ReceiverPushMessage.this.exchangeName, "direct", true);
                    ReceiverPushMessage.this.offlineMsg = ReceiverPushMessage.this.channel.queueDeclare(receiverID, false, false, false, (Map)null);
//                    if(ReceiverPushMessage.this.offlineMsg.getMessageCount() > 0) {
//                        ReceiverPushMessage.this.channel.queueBind(receiverID, ReceiverPushMessage.this.exchangeName, receiverID);
//                    }

                    ReceiverPushMessage.this.channel.basicQos(1);
                    AMQP.Queue.DeclareOk q = ReceiverPushMessage.this.channel.queueDeclare();
                    queue = q.getQueue();
                    ReceiverPushMessage.this.channel.queueBind(queue, ReceiverPushMessage.this.exchangeName, receiverID);
                    ReceiverPushMessage.this.channel.basicConsume(queue, false, ReceiverPushMessage.this);
                } catch (Exception var4) {
                    Log.e("BaseConnector", var4.getMessage());
                }

            }
        })).start();
    }

    public void handleConsumeOk(String consumerTag) {
        System.out.println("Consumer " + consumerTag + " registered");
    }

    public void handleDelivery(String consumerTag, Envelope env, BasicProperties props, byte[] body) throws IOException {
        String message = new String(body, Charset.forName("gbk"));

        long deliveryTag = env.getDeliveryTag();
        this.channel.basicAck(deliveryTag, false);

        Intent intentAllReceiver = new Intent("NOTIFICATION_RECEIVER_MESSAGE");
        intentAllReceiver.putExtra("params", message);
        this.context.getApplicationContext().sendBroadcast(intentAllReceiver);

        if(receiveListener!=null)
        {
            receiveListener.receive(message);
        }
    }

    public void handleCancelOk(String consumerTag) {
    }

    public void handleCancel(String consumerTag) throws IOException {
    }

    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
    }

    public void handleRecoverOk(String consumerTag) {
    }

    public void setReceiveListener(ReceiveListener receiveListener) {
        this.receiveListener = receiveListener;
    }

    //卸载绑定，并清理内存
    public void unBind()
    {
        try
        {
            if(channel!=null)
            {
                channel.exchangeUnbind(queue, exchangeName, receiverID);
                channel=null;
            }

            if(connection!=null)
            {
                connection.close();
                connection.abort();
                connection=null;
            }
        }
        catch (Exception ex)
        {

        }
    }
}
