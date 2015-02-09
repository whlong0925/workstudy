package com.java.classloader;

public class TestClassLoader {

	public static void main(String[] args) {
        ClassLoader loader = TestClassLoader.class.getClassLoader(); 
        while(loader!=null){
        	System.out.println(loader.getClass().getName());
        	loader = loader.getParent();
        }
        System.out.println(loader); 
	}

}
