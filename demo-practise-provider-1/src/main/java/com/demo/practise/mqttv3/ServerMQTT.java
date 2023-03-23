package com.demo.practise.mqttv3;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: ServerMQTT
 * @Author: jiangyw8
 * @Date: 2020-11-12 19:54
 * @Description: 推送
 */
public class ServerMQTT {
    //tcp://MQTT安装的服务器地址:MQTT定义的端口号
    private static final String HOST = "tcp://192.168.1.102:1883";
    //定义一个主题
    private static final String TOPIC = "mtopic";
    //定义MQTT的ID，可以在MQTT服务配置中指定
    private static final String clientId = "server11";

    private MqttClient client;
    private MqttTopic topic11;
    private MqttMessage message;
    private String userName = "stonegeek";
    private String passWord = "123456";

    private ServerMQTT() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientId, new MemoryPersistence());
        connect();
    }

    private void connect(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        //password为字符数组
        options.setPassword(passWord.toCharArray());
        //超时时间，秒
        options.setConnectionTimeout(10);
        //设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);
           topic11 =  client.getTopic(TOPIC);
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(MqttTopic topic, MqttMessage message) throws MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("信息已发送：{}"+ token.isComplete());
    }

    public static void main(String[] args) throws MqttException {
        ServerMQTT serverMQTT = new ServerMQTT();
        serverMQTT.message = new MqttMessage();
        serverMQTT.message.setQos(1);
        serverMQTT.message.setRetained(true);
        serverMQTT.message.setPayload("hello. topic11".getBytes());
        serverMQTT.publish(serverMQTT.topic11, serverMQTT.message);
        System.out.println(serverMQTT.message.isRetained() + "------ratained状态");

    }

}
