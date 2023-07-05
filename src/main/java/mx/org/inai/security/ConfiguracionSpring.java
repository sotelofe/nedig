package mx.org.inai.security;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.util.unit.DataSize;

@SpringBootApplication(scanBasePackages="mx.org.inai")
@EnableJpaRepositories("mx.org.inai.repository")
@EntityScan(basePackages="mx.org.inai.model")
@EnableAsync
@EnableScheduling
@EnableAutoConfiguration
@EnableAuthorizationServer
@EnableResourceServer
public class ConfiguracionSpring{	
	
	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
	    factory.setMaxFileSize(DataSize.ofBytes(512000000L));
	    factory.setMaxRequestSize(DataSize.ofBytes(512000000L));
	    return factory.createMultipartConfig();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ConfiguracionSpring.class, args);
	}	
}
