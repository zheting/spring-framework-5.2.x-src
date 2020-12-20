package cn.javamoney.test;

import cn.javamoney.aop.MathCaculator;
import cn.javamoney.config.AopConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestAop {

	/*
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
		//这里是MathCaculator的代理对象
		MathCaculator mathCaculator = ac.getBean("mathCaculator", MathCaculator.class);
		int result = mathCaculator.caculator(10, 2);
		System.out.println(String.format("运行结果： %s", result));
	}

	/*
	 *  1.0 传入配置类，创建IOC容器
	 *  2.0 注册配置类，调用refresh()刷新容器
	 *  3.0 注册bean的后置处理器（注册其实就是实例化），以便拦截bean的创建  registerBeanPostProcessors(beanFactory);
	 * 		（1）先获取IOC容器已经定义了的需要创建对象的所有BeanPostProcessor
	 *			其中 org.springframework.aop.config.internalAutoProxyCreator 就是 @EnableAspectJAutoProxy注解加入的 BeanDefinition
	 *		(2) BeanPostProcessor优先级处理: PriorityOrdered > Ordered > 没有实现PriorityOrdered和Ordered的普通BeanPostProcessor
	 * 			AnnotationAwareAspectJAutoProxyCreator实现了Ordered接口
	 * 		（3）创建 BeanPostProcessor(BeanPostProcessor本生也是一个bean，需要被容器创建管理)
	 *				A: 创建实例 getBean->doGetBean-->getSingleton-->createBean-->doCreateBean-->createBeanInstance
	 * 				B：populateBean 给bean的属性赋值
	 * 				C: initializeBean 初始化bean
	 * 						a: invokeAwareMethods  判断bean是否实现接口Aware，如果是Aware 或者是子接口，那么给Aware接口的方法赋值。 AnnotationAwareAspectJAutoProxyCreator实现了BeanFactoryAware接口
	 * 								setBeanFactory -->initBeanFactory   创建 ReflectiveAspectJAdvisorFactory/BeanFactoryAspectJAdvisorsBuilderAdapter
	 * 						b: applyBeanPostProcessorsBeforeInitialization    BeanPostProcessor 的  postProcessBeforeInitialization() 方法在这里调用
	 * 						c: invokeInitMethods     执行自定义的 init方法
	 * 						d: applyBeanPostProcessorsAfterInitialization    // BeanPostProcessor 的  postProcessAfterInitialization() 方法在这里调用
	 *		(4) registerBeanPostProcessors -> beanFactory.addBeanPostProcessor(postProcessor);
	 *
	 * ======= 以上是创建和注册AnnotationAwareAspectJAutoProxyCreator的过程 ============
	 * AnnotationAwareAspectJAutoProxyCreator 是一个 SmartInstantiationAwareBeanPostProcessor 后置处理器
	 *
	 * 4.0 finishBeanFactoryInitialization 创建剩下的单实例bean
	 * 		（1） 遍历获取容器中所有的beanDefinitionNames, 依次创建对象 getBean(beanName)
	 *				getBean->doGetBean->getSingleton()->检查bean是否在单实例bean的缓存中  如果没有 createBean()
	 * 		（2） 创建bean
	 * 				先从缓存中获取当前bean， 如果能获取到，说明bean是之前被创建过的，之间使用，否则再创建；只要创建好的bean都会被缓存起来。
	 *				createBean()
	 * 					resolveBeforeInstantiation() 希望后置处理器在此能能返回一个代理对象；如果能返回代理对象就使用， 如果不能就继续执行 doCreateBean()[createBeanInstance,populateBean,initializeBean]
	 * 						applyBeanPostProcessorsBeforeInstantiation()
	 * 									循环所有的后置处理器，如果是InstantiationAwareBeanPostProcessor(AnnotationAwareAspectJAutoProxyCreator实现了InstantiationAwareBeanPostProcessor)类型的后置处理器，
	 * 										就执行AnnotationAwareAspectJAutoProxyCreator.postProcessBeforeInstantiation()。
	 *											 判断当前bean是否在advisedBeans中（advisedBeans中保存了所有需要增强的bean）
	 * 											 判断当前bean是否是 Advice Pointcut Advisor AopInfrastructureBean 或者是否是切面（判断切面@Aspect）
	 * 												判断是否需要跳过
	 * 														获取候选的增强器（切面里的通知方法） List<Advisor> candidateAdvisors
	 * 														每一个通知方法被封装成Advisor就是一个增强器, 类型是InstantiationModelAwarePointcutAdvisor
	 * 														判断每一个增强器是否是AspectJPointcutAdvisor类型的，如果是返回true
	 * 														shouldSkip() 返回 false
	 * 						applyBeanPostProcessorsAfterInitialization()
	 * 							postProcessAfterInitialization()
	 *							return wrapIfNecessary(bean, beanName, cacheKey);
	 * 								获取当前bean的所有增强器（通知方法） getAdvicesAndAdvisorsForBean()
	 * 									找到候选的所有的增强器（找哪些通知方法时需要切入当前bean方法的）
	 * 									获取到能在bean使用的增强器
	 * 									增强器排序
	 * 							 保存当前bean在advisedBeans中
	 * 							如果当前bean需要增强，创建当前bean的代理对象
	 *							  createProxy
	 * 									JdkDynamicAopProxy(config)
	 * 									ObjenesisCglibAopProxy(config)
	 *
	 *  目标方法执行
	 * 		org.springframework.aop.framework.CglibAopProxy.DynamicAdvisedInterceptor.intercept()
	 *		根据ProxyFactory对象获取将要执行的目标方法拦截器链
	 * 		List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
	 * 		如果没有拦截器链，直接执行目标方法
	 * 		如果有拦截器链，把需要执行的目标对象，目标方法，拦截器链等信息传入创建一个CglibMethodInvocation对象 并调用proceed()
	 * 		new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
	 * 			拦截器链
	 * 				List<Object> interceptorList  = getInterceptorsAndDynamicInterceptionAdvice（）
	 *			遍历所有的增强器， 将其转为Interceptor
	 * 				 registry.getInterceptors(advisor)
	 */

	/*
		@EnableAspectJAutoProxy 通过@Import(AspectJAutoProxyRegistrar.class)
		AspectJAutoProxyRegistrar实现接口ImportBeanDefinitionRegistrar
		注册AnnotationAwareAspectJAutoProxyCreator的定义到 beanDefinitionMap， key是 org.springframework.aop.config.internalAutoProxyCreator value是 new RootBeanDefinition(AnnotationAwareAspectJAutoProxyCreator.class);

	 */
}
