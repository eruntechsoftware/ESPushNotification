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
import java.util.Map;


public class Receiver implements Consumer {
    protected Channel channel;
    protected Connection connection;
    private Context context;
    private AMQP.Queue.DeclareOk offlineMsg;
    private ReceiveListener receiveListener;
    private String exchangeName = "eruntech";
    private String receiverID;

    public Receiver(Context context, final String receiverID) throws IOException {
        this.receiverID = receiverID;
        this.context = context;
        (new Thread(new Runnable() {
            public void run() {
                try {
                    ConnectionFactory ex = new ConnectionFactory();
                    ex.setHost("192.168.1.150");
                    ex.setUsername("admin");
                    ex.setPassword("admin");
                    Receiver.this.connection = ex.newConnection();
                    Receiver.this.channel = Receiver.this.connection.createChannel();
                    Receiver.this.channel.exchangeDeclare(Receiver.this.exchangeName, "direct", true);
                    Receiver.this.offlineMsg = Receiver.this.channel.queueDeclare(receiverID, false, false, false, (Map)null);
                    if(Receiver.this.offlineMsg.getMessageCount() > 0) {
                        Receiver.this.channel.queueBind(receiverID, Receiver.this.exchangeName, receiverID);
                    }

                    Receiver.this.channel.basicQos(1);
                    AMQP.Queue.DeclareOk q = Receiver.this.channel.queueDeclare();
                    String queue = q.getQueue();
                    Receiver.this.channel.queueBind(queue, Receiver.this.exchangeName, receiverID);
                    Receiver.this.channel.basicConsume(queue, false, Receiver.this);
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
        String message = new String(body);
        Log.d("离线消息数", String.valueOf(this.offlineMsg.getMessageCount()));
        if(this.offlineMsg != null && this.offlineMsg.getMessageCount() == 0) {
            this.channel.queueDelete(this.receiverID);
        }

        System.out.println(message);
        long deliveryTag = env.getDeliveryTag();
        this.channel.basicAck(deliveryTag, false);
        Intent intentReceiver = new Intent(this.context, NotificationBroadcastReceiver.class);
        intentReceiver.setAction("NOTIFICATION_RECEIVER_MESSAGE");
        intentReceiver.putExtra("params", message);
        this.context.getApplicationContext().sendBroadcast(intentReceiver);

        Intent intentAllReceiver = new Intent("NOTIFICATION_RECEIVER_MESSAGE");
        intentAllReceiver.putExtra("params", message);
        this.context.getApplicationContext().sendBroadcast(intentAllReceiver);
        if(this.receiveListener != null) {
            this.receiveListener.receive(message);
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
}
