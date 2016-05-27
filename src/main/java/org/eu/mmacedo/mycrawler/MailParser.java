package org.eu.mmacedo.mycrawler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public class MailParser {
	static String[] parse(String[] args) throws UnsupportedEncodingException {
		if (Arrays.asList(CrawlerParser.mailcmds).contains(args[0]) && args.length > 2 && args[1] != null
				&& args[2] != null) {
			String username = URLEncoder.encode(args[1], "UTF-8");
			String password = URLEncoder.encode(args[2], "UTF-8");
			String[] output = { "--username=" + username, "--password=" + password };
			return output;
		} else {
			String[] output = { "--username=mywebcrawling", "--password=123456" };
			return output;
		}
	}
}
