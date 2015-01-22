package com.java.io;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * 文件排序　规则：目录排在前面，按字母顺序排序文件列表
 * @author rain
 *
 */
public class FileSort {
	public static void main(String[] args) {
		List<File> files = Arrays.asList(new File("/home/rain/ldm").listFiles());
		Collections.sort(files, new Comparator<File>() {
			public int compare(File o1, File o2) {
				if (o1.isDirectory() && o2.isFile()) {
					return -1;
				} else if (o1.isFile() && o2.isDirectory()) {
					return 1;
				}
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (File f : files) {
			System.out.println(f.getName());
		}
	}
}
