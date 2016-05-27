package org.eu.mmacedo.mycrawler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:META-INF/spring/integration/spring-integration-context.xml")
public class Application {

    public static void main(String[] args) throws Exception {
    	String[] newargs = MailParser.parse(args);
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, newargs); 
    	if (CrawlerParser.parse(args, ctx)) {
            System.out.println("Hit Enter to terminate");
            System.in.read();    		
    	}
    	
        ctx.close();
    }
}