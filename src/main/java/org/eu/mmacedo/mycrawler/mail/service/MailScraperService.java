package org.eu.mmacedo.mycrawler.mail.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class MailScraperService {

	private static final Logger logger = LoggerFactory.getLogger(MailScraperService.class);

	public Collection<String> run(MimeMessage mimeMessage)
			throws MessagingException, IOException, SAXException, TikaException {
		Collection<String> output = new ArrayList<String>();
		String titulo = "título: " + mimeMessage.getSubject() + "\n";
		output.add(titulo);

		StringWriter text = new StringWriter();

		// ByteArrayOutputStream out = new ByteArrayOutputStream();
		// mimeMessage.writeTo(out);
		//
		// BodyContentHandler handler = new BodyContentHandler();
		// Metadata metadata = new Metadata();
		// AutoDetectParser parser = new AutoDetectParser();
		// InputStream stream = new ByteArrayInputStream(out.toByteArray());
		// ParseContext pcontext = new ParseContext();
		//
		// parser.parse(stream, handler, metadata);
		//
		// String text = handler.toString();

		if (mimeMessage.getContent() instanceof MimeMultipart) {
			MimeMultipart multipart = (MimeMultipart) mimeMessage.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				MimeBodyPart bodyPart = (MimeBodyPart) multipart.getBodyPart(i);
				if (bodyPart.getDisposition() == null) {					
					if (bodyPart.getContent() instanceof MimeMultipart) {
						MimeMultipart subMultipart = (MimeMultipart) bodyPart.getContent();
						for (int j = 0; j < subMultipart.getCount(); j++) {
							MimeBodyPart subBodyPart = (MimeBodyPart) subMultipart.getBodyPart(j);
							if (subBodyPart.getContentType().startsWith("text/plain")) {
								text.append(subBodyPart.getContent().toString());
							}
						}
					} else {
						text.append(bodyPart.getContent().toString());
					}					
				}
			}
		} else {
			Parser parser = new AutoDetectParser();
			BodyContentHandler handlerContent = new BodyContentHandler();
			Metadata metadata = new Metadata();
			ParseContext context = new ParseContext();
			parser.parse(new ByteArrayInputStream(mimeMessage.getContent().toString().getBytes()), handlerContent,
					metadata, context);
			text.append(handlerContent.toString());
		}

		output.add(text.toString());
		if (logger.isTraceEnabled()) {
			logger.trace(text.toString());
		}

		return output;
	}

}
