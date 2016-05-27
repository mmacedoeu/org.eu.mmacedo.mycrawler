package org.eu.mmacedo.mycrawler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.validator.routines.UrlValidator;
import org.eu.mmacedo.mycrawler.file.FeedRequestGateway;
import org.eu.mmacedo.mycrawler.mail.inbound.MailAuthFeed;
import org.eu.mmacedo.mycrawler.web.inbound.RequestWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;


public class CrawlerParser {
	static String[] arqcmds = {"arquivo", "file", "arquivos", "files", "f"};
	static String[] webcmds = {"web", "w"};
	public static String[] mailcmds = {"gmail", "mail","g","m"};	
	
	private static final Logger logger = 
			LoggerFactory.getLogger(CrawlerParser.class);	
	
	private static void errParamMissing() {
		logger.error("Parâmetro faltando");
	}
	
	private static void errExist() {
		logger.error("Inexistente");
	}
		
	private static void errAccess() {
		logger.error("Sem permissão de leitura");
	}	
	
	private static void errFolder() {
		logger.error("Não é pasta");
	}
	
	private static void errUnsupported() {
		logger.error("Não suportado");
	}	
	
	private static void errUrlInvalid() {
		logger.error("Url inválida");
	}
	
	private static void errIntexpected() {
		logger.error("Inteiro esperado");
	}
	
	public static Boolean parse(String[] args, ConfigurableApplicationContext ctx) throws IOException {
		if (args == null || args.length < 2 || args[0] == null) {
			errParamMissing();
			return false;
		}
		
		if (Arrays.asList(arqcmds).contains(args[0])) {
			if (args[1] == null) {
				errParamMissing();
				return false;
			}
			logger.info("argumento : " + args[1]);			
			File f = new File(args[1]);
			if (!f.exists()) {
				errExist();
				return false;
			}
			if (!f.canRead()) {
				errAccess();
				return false;
			}
			if (!f.isDirectory()) {
				errFolder();
				return false;
			}				
			FeedRequestGateway req = ctx.getBean("fileGateway", FeedRequestGateway.class);
			req.request(f);
			return true;
		} else if (Arrays.asList(webcmds).contains(args[0])) {
			if (args.length < 3 || args[1] == null || args[2] == null) {
				errParamMissing();
				return false;
			}
			logger.info("argumento 1: " + args[1]);
			logger.info("argumento 2: " + args[2]);
			String[] schemes = {"http","https"};
			UrlValidator urlValidator = new UrlValidator(schemes);
			if (!urlValidator.isValid(args[1])) {
				errUrlInvalid();
				return false;
			}
			Integer profundidade = 0;
			try {
				profundidade = Integer.parseInt(args[2]);
			} catch(NumberFormatException e) {
				errIntexpected();
				return false;
			}
			RequestWeb req = ctx.getBean("webRequestFeeder", RequestWeb.class);
			req.sendRequest(args[1], profundidade);
			return true;
		} else if (Arrays.asList(mailcmds).contains(args[0])) {
			if (args.length < 3 || args[1] == null || args[2] == null) {
				errParamMissing();
				return false;
			}		
			
//			PropertyPlaceholderConfigurer propConfig = new PropertyPlaceholderConfigurer();
//			Properties properties = new Properties();
//			properties.put("username",URLEncoder.encode(args[1], "UTF-8"));
//			properties.put("password",URLEncoder.encode(args[2], "UTF-8"));
//			propConfig.setProperties(properties);
//			ctx.addBeanFactoryPostProcessor(propConfig);
			//ctx.refresh();
			
			MailAuthFeed req = ctx.getBean("mailAuthFeed", MailAuthFeed.class);
			req.start();
			return true;
		}
		errUnsupported();
		return false;
	}
}
