package com.sen.api.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ClassFinder {


    static ClassLoader classloader = Thread.currentThread().getContextClassLoader();  
    /** 
     * 获取同一路径下所有子类或接口实现类 
     *  
     * @param intf 
     * @return 
     * @throws IOException 
     * @throws ClassNotFoundException 
     */  
    public static List<Class<?>> getAllAssignedClass(Class<?> cls) {  
        List<Class<?>> classes = new ArrayList<Class<?>>();  
        for (Class<?> c : getClasses(cls)) {  
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {  
                classes.add(c);  
            }  
        }  
        return classes;  
    }  
  
    /** 
     * 取得当前类路径下的所有类 
     *  
     * @param cls 
     * @return 
     * @throws IOException 
     * @throws ClassNotFoundException 
     */  
    public static List<Class<?>> getClasses(Class<?> cls) {  
        String pk = cls.getPackage().getName();  
        String path = pk.replace('.', '/');  
//      URL url = classloader.getResource(path);  
//      return getClasses(new File(url.getFile()), pk);  
      try {
			String dirPath = URLDecoder.decode(classloader.getResource(path).getPath(),"utf-8");
			return getClasses(new File(dirPath), pk);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      return new ArrayList<Class<?>>();
    }  
  
    /** 
     * 迭代查找类 
     *  
     * @param dir 
     * @param pk 
     * @return 
     * @throws ClassNotFoundException 
     */  
    private static List<Class<?>> getClasses(File dir, String pk) {  
        List<Class<?>> classes = new ArrayList<Class<?>>();  
        if (!dir.exists()) {  
            return classes;  
        }  
        for (File f : dir.listFiles()) {  
            if (f.isDirectory()) {  
                classes.addAll(getClasses(f, pk + "." + f.getName()));  
            }  
            String name = f.getName();  
            if (name.endsWith(".class")) {  
            	try{
                classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
                }catch(Exception ex){
                	//TODO console warn
                }
            }  
        }  
        return classes;  
    }  
  
}  