package org.eu.mmacedo.mycrawler.file.inbound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileInboundFeeder {
	private static final String[] extensions = {"txt","pdf","doc","docx","ppt","pptx"};
	
	private static final Logger logger = 
			LoggerFactory.getLogger(FileInboundFeeder.class);		
	
	public Collection<File> run(File baseFolder) throws IOException {
		Collection<File> output = new ArrayList<File>();
		Collection<File> listFiles = FileUtils.listFiles(baseFolder, extensions, true);
		for (File file : listFiles) {
			if (file.canRead() && !file.isDirectory()) {
				if (logger.isTraceEnabled()) {
					logger.trace(file.getCanonicalPath());
				}
				output.add(file);
			}
		}		
		return output;
	}

}
