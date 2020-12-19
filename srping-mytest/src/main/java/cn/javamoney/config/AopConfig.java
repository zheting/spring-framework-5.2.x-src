package cn.javamoney.config;

import cn.javamoney.aop.LogAspects;
import cn.javamoney.aop.MathCaculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {

	@Bean
	public MathCaculator mathCaculator(){
		return new MathCaculator();
	}

	@Bean
	public LogAspects logAspects(){
		return new LogAspects();
	}

}
