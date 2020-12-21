package cn.javamoney.test;

import cn.javamoney.bean.User;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestIoc {

	/*
	 *
	 */
	@Test
	public void test() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		User user = ac.getBean("user", User.class);
		System.out.println(user);
	}


}
