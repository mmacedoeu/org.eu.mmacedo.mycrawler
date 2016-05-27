package org.eu.mmacedo.mycrawler.web.service;

import java.util.Map;

import org.apache.commons.validator.routines.UrlValidator;
import org.eu.mmacedo.mycrawler.web.inbound.RequestWeb;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Headers;

public class WebDepthHandlerService {

	@Autowired
	@Qualifier("webRequestFeeder")
	RequestWeb webRequestFeeder;	
	
	private static final Logger logger = 
			LoggerFactory.getLogger(WebDepthHandlerService.class);		
	
	public Void run(String payload, @Headers Map<String, Object> headerMap) {
		Object object = headerMap.get("X-depth");
		if (object instanceof Integer) {
			Integer profundidade = (Integer) object;
			if (profundidade > 0) {
				final Document htmlDoc = Jsoup.parse(payload);
				Elements links = htmlDoc.select("a[href]");
				for (Element link : links) {
					if (logger.isTraceEnabled()) {
						logger.trace("link : " + link.attr("href"));
					}									
					String href = link.attr("href");
					String[] schemes = {"http","https"};	
					UrlValidator urlValidator = new UrlValidator(schemes);					
					if (href.indexOf("http") == 0) {
						if (urlValidator.isValid(href)) {
							webRequestFeeder.sendRequest(href, profundidade-1);
						}
					} else {																
						String origin = (String) headerMap.get("X-URL");
						String newurl = origin + href;
						String to = newurl.replaceAll("(?<!(http:|https:))//", "/");						
						if (urlValidator.isValid(to)) {
							webRequestFeeder.sendRequest(to, profundidade-1);
						}
					}					
				}								
			}				
		}
		
		return null;
	}
	
}
