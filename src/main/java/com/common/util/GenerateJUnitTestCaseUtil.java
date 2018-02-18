package com.common.util;

import com.io.hw.file.util.FileUtils;
import com.string.widget.util.RandomUtils;
import com.string.widget.util.ValueWidget;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import java.lang.reflect.Parameter;

/**
 * Created by 黄威 on 13/12/2016.<br >
 * 要求java1.8 <br />
 * 生成单元测试源代码
 */
public class GenerateJUnitTestCaseUtil {
    /***
     * 参数类型的默认值
     */
    private static Map<String, Object> parameterValMap = new HashMap<String, Object>();
    /***
     * <java.util.List,java.util.ArrayList >
     */
    private static Map<String, String> parameterInstanceClassMap = new HashMap<String, String>();

    static {
        parameterValMap.put("java.lang.String", "\"\"");
        parameterValMap.put("java.lang.Long", "1L");
        parameterValMap.put("java.lang.Integer", 0);
        parameterValMap.put("java.lang.Short", "(Short)0");
        parameterValMap.put("java.lang.Float", "0f");
        parameterValMap.put("java.lang.Double", "0.0");
        parameterValMap.put("long", "1L");
        parameterValMap.put("int", 0);
        parameterValMap.put("short", "(short)0");
        parameterValMap.put("float", "0f");
        parameterValMap.put("double", "0.0");
        parameterValMap.put("java.lang.Boolean", false);
        parameterValMap.put("boolean", false);
        parameterValMap.put("java.util.List", "null");
        parameterValMap.put("java.util.Map", "null");
        parameterValMap.put("java.util.Set", "null");

        parameterInstanceClassMap.put("java.util.List", "java.util.ArrayList");
        parameterInstanceClassMap.put("java.util.Map", "java.util.HashMap");
    }

    /***
     * 把父类替换为具体的可实例化的子类
     * @param parameterType
     * @return
     */
    private static String replaceToInstanceClass(String parameterType) {
        for (Map.Entry<String, String> entry : parameterInstanceClassMap.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            parameterType = parameterType.replace(key, val);
        }
        return parameterType;
    }

    /***
     * 生成整个类
     * @param clazz
     * @return
     */
    public static String getServiceTestClassBodyString(Class clazz) {
//        System.out.println();
        StringBuffer serviceTestBody = new StringBuffer("package " + clazz.getPackage().getName() + ";");
        serviceTestBody.append(SystemHWUtil.CRLF);
        String serviceTestPrefix = null;//getServiceTestPrefix(clazz.getSimpleName());
        serviceTestBody.append(serviceTestPrefix);
        String serviceTestSuffix = getServiceTestsuffix();
        StringBuffer methodBody = getAllTestMethodBodyString(clazz);
        serviceTestBody.append(methodBody);
        serviceTestBody.append(serviceTestSuffix);
        return serviceTestBody.toString();
    }


    /***
     * 生成指定类的所有测试方法(不是完整代码)
     * @param clazz
     * @return
     */
    private static StringBuffer getAllTestMethodBodyString(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        StringBuffer methodBody = new StringBuffer();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            //是public且不是静态方法
            if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {
                String methodInvokeSt = getMethodInvokeString(clazz, method);
                System.out.println(methodInvokeSt);
                methodBody.append(getTestMethed(clazz, method)).append(SystemHWUtil.CRLF).append(SystemHWUtil.CRLF);
//            System.out.println(parameterDefaultVal);
            }
        }
        return methodBody;
    }

    /***
     * productService.queryProduct(0,"")<br />
     * productService.sayHello(0,null)
     * @param clazz
     * @param method
     * @return
     */
    private static String getMethodInvokeString(Class clazz, Method method) {
        String parameterDefaultVal = getParameterDefaultValBrackets(method);
        return ValueWidget.title(clazz.getSimpleName()) + "." + method.getName() + parameterDefaultVal;
    }

    /***
     * (0,"")
     * @param method
     * @return
     */
    private static String getParameterDefaultValBrackets(Method method) {
        String parameterDefaultVal = getParameterDefaultVal(method);
        return "(" + parameterDefaultVal + ")";
    }

    /***
     * 获取方法的默认值
     * @param method
     * @return
     */
    private static String getParameterDefaultVal(Method method) {
        /*Parameter[] parameterArr = method.getParameters(); TODO 需要更高版本的java,比如1.8
        Type[] typeArr = new Type[parameterArr.length];
        StringBuffer parameterStringBuffer = new StringBuffer();
        for (int i = 0; i < parameterArr.length; i++) {
            typeArr[i] = parameterArr[i].getParameterizedType();
            Object parameterType = parameterValMap.get(typeArr[i].getTypeName());
            if (null == parameterType && (!"javax.servlet.http.HttpServletRequest".equals(typeArr[i].getTypeName()))
                    && !"javax.servlet.http.HttpServletResponse".equals(typeArr[i].getTypeName())) {
                String newInstanceClass = "new " + replaceToInstanceClass(typeArr[i].getTypeName());
                if (typeArr[i].getTypeName().endsWith("[]")) {
                    parameterType = newInstanceClass + "{}";
                } else {
                    if ("java.math.BigDecimal".equals(typeArr[i].getTypeName())) {
                        parameterType = newInstanceClass + "(0)";
                    } else {
                        parameterType = newInstanceClass + "()";
                    }
                }
            }
            parameterStringBuffer.append(parameterType).append(",");
//            System.out.println(typeArr[i]);
        }
        return parameterStringBuffer.toString().replaceAll(",$", SystemHWUtil.EMPTY);*/
        return null;
    }


    /***
     * 类的后缀
     * @return
     */
    public static String getServiceTestsuffix() {
        return "	" + SystemHWUtil.CRLF +
                "	" + SystemHWUtil.CRLF +
                "}";
    }

    /***
     * 生成单个单元测试方法
     * @param clazz
     * @param method
     * @return
     */
    private static String getTestMethed(Class clazz, Method method) {
        String testMethedBody = "    @Test" + SystemHWUtil.CRLF +
                "    public final void test" + ValueWidget.capitalize(method.getName()) + SystemHWUtil.UNDERLINE + RandomUtils.getRandomStr(2) + "() {" + SystemHWUtil.CRLF +
                "        try {" + SystemHWUtil.CRLF +
                "            " + getMethodInvokeString(clazz, method) + ";" + SystemHWUtil.CRLF +
                "        }catch (Exception ex){" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "        }" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "    }";
        return testMethedBody;
    }

    /***
     * Dto的单元测试
     * @param clazz
     * @param classFullName
     * @return
     */
    private static String getDtoMethed(Class clazz, String classFullName) {
        String simpleClassName = clazz.getSimpleName();
        String instanceName = ValueWidget.title(simpleClassName);
        String testDtoJUnitBody = "import com.chanjet.gov.service.dto.OrgInfoDto;" + SystemHWUtil.CRLF +
                "import com.chanjet.gov.util.JSONExtension;" + SystemHWUtil.CRLF +
                "import org.junit.Assert;" + SystemHWUtil.CRLF +
                "import org.junit.Test;" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "import java.lang.reflect.Method;" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "/**" + SystemHWUtil.CRLF +
                " * Created by 黄威 on 19/12/2016.<br >" + SystemHWUtil.CRLF +
                " */" + SystemHWUtil.CRLF +
                "public class " + simpleClassName + "TestDtoUnit  {" + SystemHWUtil.CRLF +
                "    @Test" + SystemHWUtil.CRLF +
                "    public final void testToJson(){" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "        try {" + SystemHWUtil.CRLF +
                "            Class clazz=Class.forName(\"" + classFullName + "\");" + SystemHWUtil.CRLF +
                "            try {" + SystemHWUtil.CRLF +
                "                Method method= clazz.getMethod(\"toJson\");" + SystemHWUtil.CRLF +
                "                if(method!=null){" + SystemHWUtil.CRLF +
                "                    " + simpleClassName + " instanceName=new " + simpleClassName + "();" + SystemHWUtil.CRLF +
                "                    String json=instanceName.toJson();" + SystemHWUtil.CRLF +
                "                    instanceName= JSONExtension.parseObject(json," + simpleClassName + ".class);" + SystemHWUtil.CRLF +
                "                    Assert.assertEquals(false,instanceName==null);" + SystemHWUtil.CRLF +
                "                }" + SystemHWUtil.CRLF +
                "            } catch (NoSuchMethodException e) {" + SystemHWUtil.CRLF +
                "                e.printStackTrace();" + SystemHWUtil.CRLF +
                "            }" + SystemHWUtil.CRLF +
                "        } catch (ClassNotFoundException e) {" + SystemHWUtil.CRLF +
                "            e.printStackTrace();" + SystemHWUtil.CRLF +
                "        }" + SystemHWUtil.CRLF +
                "" + SystemHWUtil.CRLF +
                "    }" + SystemHWUtil.CRLF +
                "}";
        return ("package " + clazz.getPackage().getName() + ";" + SystemHWUtil.CRLF + testDtoJUnitBody);
    }

    /***
     * 生成单元测试类的绝对路径<br />
     * 例如:"/Users/whuanghkl/work/mygit/io0007_new/src/test/java/com/service/ProductService.java"
     * @param testFolder
     * @param classFullPath
     * @return
     */
    public static String getTestClassFullPath(String testFolder, String classFullPath) {
        String packageNameXiegang = getPackageNameXiegang(classFullPath);
        return testFolder + File.separator + packageNameXiegang;
    }

    /***
     * com/service/ProductService.java
     * @param classFullPath
     * @return
     */
    public static String getPackageNameXiegang(String classFullPath) {
        int index = classFullPath.indexOf("src/main/java");
        return classFullPath.substring(index + 14);
    }

    public static void generateDtoJUnitTestClassAndSave(String testFolder, List<File> files) {
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
//        File file=new File("/Users/whuanghkl/work/mygit/io0007_new/src/main/java/com/common/bean/Student.java");
            String classFullPath = file.getAbsolutePath();
//            System.out.println(classFullPath);
            String testClassFullpath = GenerateJUnitTestCaseUtil.getTestClassFullPath(testFolder, classFullPath);
            System.out.println(testClassFullpath);
            String packageNameXiegang = getPackageNameXiegang(classFullPath);
            System.out.println(packageNameXiegang);
            //"com.common.bean."
            String packageName = packageNameXiegang.replaceAll("[\\w]+.java", SystemHWUtil.EMPTY).replace("/", SystemHWUtil.ENGLISH_PERIOD);
            System.out.println(packageName);
            Class clazz = null;
            String classFullName = null;
            try {
                //"com.common.bean.Student"
                classFullName = packageNameXiegang.replace(".java", "").replace("/", SystemHWUtil.ENGLISH_PERIOD);
                clazz = Class.forName(classFullName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (null == clazz) {
                System.out.println("generateDtoJUnitTestClassAndSave clazz is null");
                return;
            }
            try {
                Method method = clazz.getMethod("toJson");
                if (null == method) {
                    continue;
                }
                String testDtoJUnitBody = getDtoMethed(clazz, classFullName);
                File unitTestFile = new File(testClassFullpath.replace(".java", "TestDtoUnit.java"));
                if (!unitTestFile.exists()) {
                    FileUtils.writeToFile(unitTestFile, testDtoJUnitBody, true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }


    public static void generateJUnitTestClassAndSave(String testFolder, List<File> files) {
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String classFullPath = file.getAbsolutePath();
//            System.out.println(classFullPath);
            String testClassFullpath = GenerateJUnitTestCaseUtil.getTestClassFullPath(testFolder, classFullPath);
            System.out.println(testClassFullpath);
            String packageNameXiegang = getPackageNameXiegang(classFullPath);
            System.out.println(packageNameXiegang);
            //"com.common.bean."
            String packageName = packageNameXiegang.replaceAll("[\\w]+.java", SystemHWUtil.EMPTY).replace("/", SystemHWUtil.ENGLISH_PERIOD);
            System.out.println(packageName);
            Class clazz = null;
            try {
                //"com.common.bean.Student"
                String classFullName = packageNameXiegang.replace(".java", "").replace("/", SystemHWUtil.ENGLISH_PERIOD);
                clazz = Class.forName(classFullName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (null == clazz) {
                System.out.println("generateJUnitTestClassAndSave clazz is null");
                return;
            }
            String serviceTestBody = GenerateJUnitTestCaseUtil.getServiceTestClassBodyString(clazz);
//            System.out.println( serviceTestBody);
            File unitTestFile = new File(testClassFullpath.replace(".java", "Test.java"));
            if (!unitTestFile.exists()) {
                FileUtils.writeToFile(unitTestFile, serviceTestBody, true);
            }
        }
    }


}
