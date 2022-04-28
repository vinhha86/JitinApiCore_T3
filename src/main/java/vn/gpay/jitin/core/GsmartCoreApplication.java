package vn.gpay.jitin.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
//@EnableAuthorizationServer
//@EnableResourceServer
//@EnableConfigurationProperties(ConfigProperties.class)
public class GsmartCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(GsmartCoreApplication.class, args);
//		Log4jCommon.log_StartApp();
	}

}
