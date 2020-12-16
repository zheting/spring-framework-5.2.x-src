package cn.javamoney.config;

import cn.javamoney.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public User user(){
		return new User("10","devin");
	}
}
