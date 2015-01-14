package com.java.util;
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
		System.out.println(System.getProperty("user.dir") );//  /home/rain/studyworkspace/study
		//获取类的classpath路径
		System.out.println(TestPath.class.getClass().getResource("/").getPath());// /home/rain/studyworkspace/study/bin/
		System.out.println(this.getClass().getResource("Test.properties").getPath());// /home/rain/studyworkspace/study/bin/com/java/util/Test.properties
		
		System.out.println(this.getClass().getResource("").getPath());// /home/rain/studyworkspace/study/bin/com/java/util/
		
		System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());// /home/rain/studyworkspace/study/bin/
		
	}
}
