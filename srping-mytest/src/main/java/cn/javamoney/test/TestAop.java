package cn.javamoney.test;

import cn.javamoney.aop.MathCaculator;
import cn.javamoney.config.AopConfig;
import org.aspectj.lang.annotation.Aspect;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestAop {

	/**
	 * 前提：引入依赖 spring-aspects
	 * 1.0 开启Aop  @EnableAspectJAutoProxy
	 * 2.0 切面类 @Aspect
	 * 3.0 切面类中配置切入点表达式 @Pointcut， 表达式是连接点的集合
	 * 4.0 切面类中配置通知：前置通知，后置通知，环绕通知等
	 * 5.0 把切面类LogAspects和业务类MathCaculator 都加入spring ioc容器
	 *
	 */
	@Test
	public void test() {
		AnnotationConfigApplicationContext ac= new AnnotationConfigApplicationContext(AopConfig.class);
		MathCaculator mathCaculator = ac.getBean("mathCaculator", MathCaculator.class);
		int result = mathCaculator.caculator(10, 0);
		System.out.println(String.format("运行结果： %s", result));
	}
}
