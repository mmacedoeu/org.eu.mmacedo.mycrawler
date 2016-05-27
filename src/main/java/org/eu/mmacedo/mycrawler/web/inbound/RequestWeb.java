package org.eu.mmacedo.mycrawler.web.inbound;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

public class RequestWeb {
	@Autowired
	@Qualifier("webChannel")
	MessageChannel webChannel;

	public void sendRequest(String url, Integer profundidade) {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("X-URL", url);
		headers.put("X-depth", profundidade);			
		Message<String> reqmsg = new GenericMessage<String>("",headers);
	
		webChannel.send(reqmsg);
	}
}
