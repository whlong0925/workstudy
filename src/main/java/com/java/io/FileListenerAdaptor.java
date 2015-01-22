package com.java.io;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * 监听文件
 * @author rain
 *
 */
public class FileListenerAdaptor extends FileAlterationListenerAdaptor {

	/**
	 * File system observer started checking event.
	 */
	@Override
	public void onStart(FileAlterationObserver observer) {
		
		super.onStart(observer);
		System.out.println("文件系统观察者开始检查事件");
	}

	/**
	 * File system observer finished checking event.
	 */
	@Override
	public void onStop(FileAlterationObserver observer) {
		
		super.onStop(observer);
		System.out.println("文件系统完成检查事件观测器");
	}

	/**
	 * Directory created Event.
	 */
	@Override
	public void onDirectoryCreate(File directory) {
		
		super.onDirectoryCreate(directory);
		System.out.println("目录创建事件");
	}

	/**
	 * Directory changed Event
	 */
	@Override
	public void onDirectoryChange(File directory) {
		
		super.onDirectoryChange(directory);
		System.out.println("目录改变事件");
	}

	/**
	 * Directory deleted Event.
	 */
	@Override
	public void onDirectoryDelete(File directory) {
		
		super.onDirectoryDelete(directory);
		System.out.println("目录删除事件");
	}

	/**
	 * File created Event.
	 */
	@Override
	public void onFileCreate(File file) {
		
		super.onFileCreate(file);
		System.out.println("创建文件：" + file.getName());

	}

	/**
	 * File changed Event.
	 */
	@Override
	public void onFileChange(File file) {
		
		super.onFileChange(file);
		System.out.println("文件改变事件");
	}

	/**
	 * File deleted Event.
	 */
	@Override
	public void onFileDelete(File file) {
		
		super.onFileDelete(file);
		System.out.println("文件删除事件:" + file.getName());
	}

	static final class FileFilterImpl implements FileFilter {

		/**
		 * @return return true:返回所有目录下所有文件详细(包含所有子目录)
		 * @return return false:返回主目录下所有文件详细(不包含所有子目录)
		 */
		public boolean accept(File file) {
			
			System.out.println("文件路径: " + file);
			System.out.println("最后修改时间： " + file.lastModified());
			return true;
		}
	}

	public static void main(String[] args) {
		try {
			String directory = "/home/rain/ldm";
			// 构造观察类主要提供要观察的文件或目录，当然还有详细信息的filter
//			FileAlterationObserver observer = new FileAlterationObserver(directory, new FileFilterImpl());
//			IOFileFilter filter = FileFilterUtils.and(FileFilterUtils.fileFileFilter(),FileFilterUtils.suffixFileFilter(".json"));
//			FileAlterationObserver fileAlterationObserver = new FileAlterationObserver(directory, filter, null);
			FileAlterationObserver observer = new FileAlterationObserver(directory);
			// 构造收听类 没啥好说的
			FileListenerAdaptor listener = new FileListenerAdaptor();
			// 为观察对象添加收听对象
			observer.addListener(listener);
			// 配置Monitor，第一个参数单位是毫秒，是监听的间隔；第二个参数就是绑定我们之前的观察对象。
			FileAlterationMonitor fileMonitor = new FileAlterationMonitor(10000, new FileAlterationObserver[] { observer });
			// 启动开始监听
			fileMonitor.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
