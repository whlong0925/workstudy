package com.java.util;

import java.io.InputStream;

/**
 * 获取项目路径的方法
 * @author rain
 *
 */
public class TestPath {
	public static void main(String[] args) {
		new TestPath().printPath();
	}
	public void printPath(){
		//得到工程的路径
		System.out.println(System.getProperty("user.dir") );//  /studyworkspace/workstudy
		//获取类的classpath路径
		System.out.println(TestPath.class.getClass().getResource("/").getPath());// /studyworkspace/workstudy/target/classes/
		System.out.println(this.getClass().getResource("Test.properties").getPath());// /home/rain/studyworkspace/workstudy/target/classes/com/java/util/Test.properties

		
		System.out.println(this.getClass().getResource("").getPath());// /home/rain/studyworkspace/workstudy/target/classes/com/java/util/
		
		System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());// /home/rain/studyworkspace/workstudy/target/classes/
		//获取inputstream
		InputStream ips = TestPath.class.getResourceAsStream("Test.properties");
	}
}
