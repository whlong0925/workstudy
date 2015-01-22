package com.java.io;
import java.io.File;
import java.io.FileFilter;
 /**
  * 根据扩展名查文件
  * @author rain
  *
  */
public class ExtensionFileFilter implements FileFilter {
    private String extension;
    public ExtensionFileFilter(String extension) {
        this.extension = "."+extension;
    }
    public boolean accept(File file) {
        if(file.isDirectory()) {
            return false;
        }
        String name = file.getName();
        return name.endsWith(extension);
    }
    public static void main(String[] args) {
        String dir = "/home/rain/ldm";            //你所选择的目录
        File file = new File(dir);
        //这个例子就表示，查找/home/rain/ldm下扩展名为json的所有文件。
        File[] files = file.listFiles(new ExtensionFileFilter("json"));
        for(File f : files){
        	System.out.println(f.getName());
        }
    }
}