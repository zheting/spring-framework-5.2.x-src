package cn.javamoney.config;

import cn.javamoney.aop.LogAspects;
import cn.javamoney.aop.MathCaculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @EnableAspectJAutoProxy 原理
 *		1.0 利用 @Import(AspectJAutoProxyRegistrar.class) 注册组件 AnnotationAwareAspectJAutoProxyCreator 到容器
 *		2.0 查看 AnnotationAwareAspectJAutoProxyCreator 的继承结构，发现实现了接口 BeanPostProcessor（SmartInstantiationAwareBeanPostProcessor） 和 Aware接口（BeanFactoryAware）
 *
 */
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
