package vn.gpay.jitin.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource("classpath:config.properties")
//@ConfigurationProperties( "config")
public class ConfigProperties {
	
	public long getID_Mau() {
		try (InputStream input = ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties")) {

	        Properties prop = new Properties();

	        // load a properties file
	        prop.load(input);

	        String obj =  prop.getProperty("ID_Mau").toString();
	        return Long.parseLong(obj);

	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return (long)0;
	    }

	}
	
	
	public long getRole_id_admin() {
		try (InputStream input = ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties")) {

	        Properties prop = new Properties();

	        // load a properties file
	        prop.load(input);

	        String obj =  prop.getProperty("role_id_admin").toString();
	        return Long.parseLong(obj);

	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return (long)0;
	    }

	}
	
}
