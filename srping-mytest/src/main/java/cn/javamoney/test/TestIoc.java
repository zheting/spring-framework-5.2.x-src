package cn.javamoney.test;

import cn.javamoney.bean.User;
import cn.javamoney.bean.UserFactoryBean;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestIoc {

	/*
	 *
	 */
	@Test
	public void test() {
		//ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring-${env}-beans.xml");
		User user = ac.getBean("user", User.class);
		System.out.println(user);
	}

	public static void main(String[] args) {
		//ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring-${env}-beans.xml");
		User user = ac.getBean("user", User.class);
		System.out.println(user);
	}

	/*
	 *	实现接口FactoryBean
	 * 	FactoyBean只需要调用getObject就可以返回具体的对象，整个对象的创建过程是由用户自己来控制的
	 */
	@Test
	public void testFactoryBean() {
		//容器启动的时候，只初始化了UserFactoryBean
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		//在获取User时，UserFactoryBean才调用getObject获取User,当然第二次获取User时就不会再调用getObject
		User user = ac.getBean("userFactoryBean", User.class);
		// 加&获取 UserFactoryBean
		UserFactoryBean userFactoryBean = ac.getBean("&userFactoryBean", UserFactoryBean.class);
		System.out.println(user);
		System.out.println(userFactoryBean);
	}


}
