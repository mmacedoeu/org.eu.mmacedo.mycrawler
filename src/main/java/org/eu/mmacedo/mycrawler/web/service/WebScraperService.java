package org.eu.mmacedo.mycrawler.web.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.xml.sax.SAXException;

public class WebScraperService {
	
	
	private static final Logger logger = 
			LoggerFactory.getLogger(WebScraperService.class);		

	public Collection<String> run(String payload, @Headers Map<String, Object> headerMap) throws IOException, SAXException, TikaException {
		Collection<String> output = new ArrayList<String>();
		String titulo = "título: " + headerMap.get("X-URL") + "\n";
		output.add(titulo);
		
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		HtmlParser htmlparser = new HtmlParser();
		InputStream stream = new ByteArrayInputStream(payload.getBytes());
		ParseContext pcontext = new ParseContext();		
		htmlparser.parse(stream, handler, metadata, pcontext);
		String text = handler.toString();
		output.add(text);
		if (logger.isTraceEnabled()) {
			logger.trace(text);
		}			

		return output;
	}

	public Collection<String> run(ResponseEntity payload, MessageHeaders headerMap) {
		Collection<String> output = new ArrayList<String>();
		String titulo = "título: " + headerMap.get("X-URL") + "\n";
		output.add(titulo);
		
		return output;
	}
}
