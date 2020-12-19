package cn.javamoney.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class LogAspects {

	@Pointcut("execution(public int cn.javamoney.aop.MathCaculator.caculator(..))")
	public void pointCut() {
	}

	// 前置通知 在目标方法执行之前运行
	// 还可以传入参数 JoinPoint 获取参数列表  方法名等
	@Before("pointCut()")
	public void before(JoinPoint joinPoint) {
		System.out.println("前置通知 在目标方法执行之前运行");
		System.out.println(String.format("获取参数名称 %s", joinPoint.getSignature().getName()));
		System.out.println(String.format("获取参数列表 %s", joinPoint.getArgs()));
	}

	// 后置通知 在目标方法运行结束之后运行, 无论是否正常放回 都会执行
	@After("pointCut()")
	public void after() {
		System.out.println("后置通知 在目标方法运行结束之后运行, 无论是否正常放回 都会执行");
	}

	//返回通知 在目标方法正常返回之后运行
	//正常返回获取返回值, 通过 returning的result和参数 result一致
	//注意：如果既要返回结果，又想使用JoinPoint, 一定要把JoinPoiint放在第一个参数
	@AfterReturning(value = "pointCut()", returning = "result")
	public void afterReturning(Object result) {
		System.out.println("返回通知 在目标方法正常返回之后运行");
		System.out.println(String.format("获取返回值 %s", result));
	}

	//异常通知 在目标方法出现异常以后运行
	@AfterThrowing(value = "pointCut()", throwing = "exception")
	public void afterThrowing(Exception exception) {
		System.out.println("异常通知 在目标方法出现异常以后运行");
		System.out.println(String.format("返回的异常 %s", exception.getMessage()));
	}

	//环绕通知  动态代理， 手动执行，在执行目标方法前后增加逻辑
	// Null return value from advice does not match primitive return type for:  出现这个错误的原因是代理的方法有返回值，但是环绕通知没有写返回值
	@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("环绕通知  目标方法前  手动执行，在执行目标方法前后增加逻辑");
		Object proceed = joinPoint.proceed();
		System.out.println("环绕通知  目标方法后  手动执行，在执行目标方法前后增加逻辑");
		return  proceed;
	}

}
