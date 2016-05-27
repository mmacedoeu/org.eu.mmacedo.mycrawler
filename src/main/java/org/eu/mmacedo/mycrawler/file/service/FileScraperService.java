package org.eu.mmacedo.mycrawler.file.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileScraperService {
	
	private static final Logger logger = 
			LoggerFactory.getLogger(FileScraperService.class);		
	
	public Collection<String> run(File file) throws IOException, TikaException {
		Collection<String> output = new ArrayList<String>();
		Tika tika = new Tika();
	    String filecontent = tika.parseToString(file);
	    String titulo = "título: " + file.getCanonicalPath() + "\n";
	    output.add(titulo);
	    output.add(filecontent);
		if (logger.isTraceEnabled()) {
			logger.trace(filecontent);
		}	    
	    return output;
	}
}
