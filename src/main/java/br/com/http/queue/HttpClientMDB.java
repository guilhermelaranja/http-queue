package br.com.http.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/http"),
		@ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1")})
public class HttpClientMDB implements MessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientMDB.class);

	public void onMessage(Message message) {
		try {
			ObjectMessage msg = (ObjectMessage) message;
			HttpRequestMessage httpRequestMessage = (HttpRequestMessage) msg.getObject();
			httpRequestMessage.send();
			if (!httpRequestMessage.success()) {
				LOGGER.error("Message not processed - HTTP error code : {}", httpRequestMessage.getResponseStatus());
			}
		} catch (JMSException e) {
			LOGGER.error("Error processing JMS message inside Http-Queue", e);
		}
	}
}
