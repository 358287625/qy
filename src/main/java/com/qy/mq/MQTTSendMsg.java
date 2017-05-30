/**
 * 
 */
package com.qy.mq;

import java.io.IOException;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kathy
 * https://m.aliyun.com/doc/document_detail/44874.html
 */
public class MQTTSendMsg {
	private static Logger log = LoggerFactory.getLogger(MQTTSendMsg.class);
	/**
	 * 设置当前用户私有的MQTT的接入点。例如此处示意使用XXX，实际使用请替换用户自己的接入点。接入点的获取方法是，在控制台申请MQTT实例，
	 * 每个实例都会分配一个接入点域名。
	 */
	final static String broker = "tcp://XXXX.mqtt.aliyuncs.com:1883";
	/**
	 * 设置阿里云的AccessKey，用于鉴权
	 */
	final static String acessKey = "XXXXXX";
	/**
	 * 设置阿里云的SecretKey，用于鉴权
	 */
	final static String secretKey = "XXXXXXX";
	/**
	 * 发消息使用的一级Topic，需要先在MQ控制台里申请
	 */
	final static String topic = "XXXX";
	static String CLIENTID_SPLIT = "@@@";

	/**
	 * MQTT的ClientID，一般由两部分组成，GroupID@@@DeviceID 其中GroupID在MQ控制台里申请
	 * DeviceID由应用方设置，可能是设备编号等，需要唯一，否则服务端拒绝重复的ClientID连接
	 * 发布p2p点对点消息
	 * @param groupId
	 * @param deviceId
	 * @throws IOException
	 */
	public static void sendP2pMsg(String groupId, String deviceId,
			String msgContent) throws IOException {

		final String clientId = groupId + CLIENTID_SPLIT + deviceId;
		String sign;
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			final MqttClient sampleClient = new MqttClient(broker, clientId,
					persistence);
			final MqttConnectOptions connOpts = new MqttConnectOptions();
			sign = MacSignature.macSignature(clientId.split(CLIENTID_SPLIT)[0],
					secretKey);
			connOpts.setUserName(acessKey);
			connOpts.setServerURIs(new String[] { broker });
			connOpts.setPassword(sign.toCharArray());
			connOpts.setCleanSession(true);
			connOpts.setKeepAliveInterval(90);
			sampleClient.setCallback(new MqttCallback() {
				public void connectionLost(Throwable throwable) {
					log.info("mqtt connection lost");
					while (!sampleClient.isConnected()) {
						try {
							Thread.sleep(1000);
							sampleClient.connect(connOpts);
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
				}

				public void messageArrived(String topic, MqttMessage mqttMessage)
						throws Exception {
					log.info("messageArrived:" + topic + "------"
							+ new String(mqttMessage.getPayload()));
				}

				public void deliveryComplete(
						IMqttDeliveryToken iMqttDeliveryToken) {
					log.info("deliveryComplete:"
							+ iMqttDeliveryToken.getMessageId());
				}
			});
			sampleClient.connect(connOpts);

			try {
				final MqttMessage message = new MqttMessage(
						msgContent.getBytes());
				message.setQos(0);
				/**
				 * 如果发送P2P消息，二级Topic必须是“p2p”，三级Topic是目标的ClientID
				 * 此处设置的三级Topic需要是接收方的ClientID
				 */
				String p2pTopic = topic + "/p2p/" + clientId;
				sampleClient.publish(p2pTopic, message);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	/**
	 * 发布订阅消息
	 * @param groupId
	 * @param deviceId
	 * @param msgContent
	 * @throws IOException
	 */
	public static void sendSubscribeMsg(String groupId, String deviceId,
			String msgContent) throws IOException {

		final String clientId = groupId + CLIENTID_SPLIT + deviceId;
		String sign;
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			final MqttClient sampleClient = new MqttClient(broker, clientId,
					persistence);
			final MqttConnectOptions connOpts = new MqttConnectOptions();
			sign = MacSignature.macSignature(clientId.split(CLIENTID_SPLIT)[0],
					secretKey);
			connOpts.setUserName(acessKey);
			connOpts.setServerURIs(new String[] { broker });
			connOpts.setPassword(sign.toCharArray());
			connOpts.setCleanSession(true);
			connOpts.setKeepAliveInterval(90);
			sampleClient.setCallback(new MqttCallback() {
				public void connectionLost(Throwable throwable) {
					log.info("mqtt connection lost");
					while (!sampleClient.isConnected()) {
						try {
							Thread.sleep(1000);
							sampleClient.connect(connOpts);
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
				}

				public void messageArrived(String topic, MqttMessage mqttMessage)
						throws Exception {
					log.info("messageArrived:" + topic + "------"
							+ new String(mqttMessage.getPayload()));
				}

				public void deliveryComplete(
						IMqttDeliveryToken iMqttDeliveryToken) {
					log.info("deliveryComplete:"
							+ iMqttDeliveryToken.getMessageId());
				}
			});
			sampleClient.connect(connOpts);
			try {
				final MqttMessage message = new MqttMessage(
						msgContent.getBytes());
				message.setQos(0);
				log.info(" pushed at " + new Date() + " " + msgContent);
				/**
				 * 消息发送到某个主题Topic，所有订阅这个Topic的设备都能收到这个消息。
				 * 遵循MQTT的发布订阅规范，Topic也可以是多级Topic。此处设置了发送到二级Topic
				 */
				sampleClient.publish(topic + "/notice/", message);

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
