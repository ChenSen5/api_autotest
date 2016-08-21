package com.sen.api.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 文件通用类
 * 
 * @author hedan
 *
 */
public class FileUtil {
	/**
	 * 多個匹配條件匹配文件（去重）
	 * 
	 * @param dir
	 * @param fileConfArr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<File> getFilesByConf(String dir, String fileConf) {
		String[] fileConfArr = fileConf.split(";");// 多个配置‘;’分开

		List<File> list = new ArrayList<File>();
		if (fileConfArr != null && fileConfArr.length > 0) {
			for (String conftemp : fileConfArr) {
				int at = conftemp.lastIndexOf("/");
				File file = null;
				String fileContextPath = "";
				String contextPath = dir;// 绝对目录路径
				if (at > 0) {// 目录部分
					fileContextPath = fileConf.substring(0, at);
				}
				if (StringUtil.isNotEmpty(fileContextPath)) {
					contextPath = contextPath + fileContextPath;
				}
				file = new File(contextPath);
				String fileNameConf = conftemp.substring(at + 1,
						conftemp.length());// 文件名配置
				String fileConfTemp = generatePattern(fileNameConf);
				System.out.println(fileConfTemp);

				Pattern p = Pattern.compile(fileConfTemp);
				ArrayList<File> listtemp = filePattern(file, p);
				list.addAll(listtemp);
			}
		}

		return removeDuplicate(list);// 去重
	}

	/**
	 * list保留順序去重
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List removeDuplicate(List list) {
		if (list == null) {
			return null;
		}
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element)) {
				newList.add(element);
			}
		}
		return newList;

	}

	/**
	 * 根据配置生成正确的正则
	 * 
	 * @param fileConf
	 * @return
	 */
	private static String generatePattern(String fileConf) {
		fileConf = fileConf.trim();
		// 根据配置生成正确的正则
		fileConf = fileConf.replace('*', '#');
		fileConf = fileConf.replaceAll("#", ".*");// 将*号之前加上.

		return fileConf;
	}

	/**
	 * 根据正则匹配正确的文件
	 * 
	 * @param file
	 * @param p
	 * @return
	 */
	private static ArrayList<File> filePattern(File file, Pattern p) {
		if (file == null) {
			return null;
		}
		if (file.isFile()) {// 如果是文件
			Matcher fMatcher = p.matcher(file.getName());
			if (fMatcher.matches()) {
				ArrayList<File> list = new ArrayList<File>();
				list.add(file);
				return list;
			}
		} else if (file.isDirectory()) {// 是目录
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				ArrayList<File> list = new ArrayList<File>();
				for (File f : files) {
					ArrayList<File> rlist = filePattern(f, p);
					if (rlist != null) {
						list.addAll(rlist);
					}
				}
				return list;
			}
		}
		return null;
	}

	public static boolean writeFile(InputStream is, String filePath) {
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		FileOutputStream fileout;
		try {
			fileout = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		/**
		 * 根据实际运行效果 设置缓冲区大小
		 */
		byte[] buffer = new byte[10 * 1024];
		int ch = 0;
		try {
			while ((ch = is.read(buffer)) != -1) {
				fileout.write(buffer, 0, ch);
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				is.close();
				fileout.flush();
				fileout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
