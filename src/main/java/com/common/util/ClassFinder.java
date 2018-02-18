package com.common.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by whuanghkl on 17/5/31.
 */
public class ClassFinder {
    public static final char PKG_SEPARATOR = '.';

    public static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    public static List<Class<?>> find(String scannedPackage) {
        URL scannedUrl = getPackageUrl(scannedPackage);
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }

    public static URL getPackageUrl(String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
//        System.out.println("package :" +scannedUrl.getFile());
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        return scannedUrl;
    }

    /***
     * 获取package 的路径,例如:<br />
     * /Users/whuanghkl/code/mygit/convention/src/main/webapp/WEB-INF/classes/com/girltest/dao
     * @param scannedPackage
     * @return
     */
    public static String getPackagePath(String scannedPackage) {
        URL scannedUrl = getPackageUrl(scannedPackage);
        return scannedUrl.getFile();
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }

    public static String getJavaPackage(String path) {
        String classPath = path.replaceAll("^.*" + "/src/main/java/", "");//"com/common/bean/Student.java"
        System.out.println("classPath :" + classPath);
        //com.common.bean.Student
        return classPath.replace(".java", "").replaceAll(String.valueOf(ClassFinder.DIR_SEPARATOR), String.valueOf(ClassFinder.PKG_SEPARATOR));
    }
}
