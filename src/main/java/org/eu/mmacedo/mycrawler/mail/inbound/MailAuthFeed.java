package org.eu.mmacedo.mycrawler.mail.inbound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

public class MailAuthFeed {
	@Autowired
	@Qualifier("controlChannel")
	MessageChannel controlChannel;
	
	public void start() {
		controlChannel.send(new GenericMessage<String>("@imapAdapter.start()"));
	}	
}
