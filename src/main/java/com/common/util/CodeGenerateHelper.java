package com.common.util;

import com.io.hw.file.util.FileUtils;
import com.string.widget.util.ValueWidget;
import com.time.util.TimeHWUtil;

import java.io.File;
import java.util.List;

/**
 * Created by whuanghkl on 17/5/31.
 */
public class CodeGenerateHelper {

    /***
     * GirlDao
     * @param clazz
     * @return
     */
    public static String getDaoClassName(Class clazz) {
        return clazz.getSimpleName() + "Dao";
    }

    public static String getControllerClassName(Class clazz) {
        return clazz.getSimpleName() + "Controller";
    }

    public static String getDaoCode(Class clazz) {
        String className = clazz.getSimpleName();
        String packageName = clazz.getPackage().getName();
        String daoPackage = getParentPackage(packageName);
        String tmplate = "package " + daoPackage + ".dao;" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "import com.common.dao.generic.GenericDao;" + SystemHWUtil.CRLF/* \r\n */ +
                "import " + clazz.getName() + ";" + SystemHWUtil.CRLF/* \r\n */ +
                "import org.springframework.stereotype.Component;" + SystemHWUtil.CRLF/* \r\n */ +
                "import java.io.Serializable;" + SystemHWUtil.CRLF/* \r\n */ +
                "import java.util.List;" + SystemHWUtil.CRLF/* \r\n */ +
                "import java.util.Map;" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "/***" + SystemHWUtil.CRLF/* \r\n */ +
                " * @author auto generate" + SystemHWUtil.CRLF/* \r\n */ +
                " *         " + TimeHWUtil.getCurrentFormattedTime() + SystemHWUtil.CRLF/* \r\n */ +
                " */" + SystemHWUtil.CRLF/* \r\n */ +
                "@Component(\"" + ValueWidget.title(className) + "Dao\")" + SystemHWUtil.CRLF/* \r\n */ +
                "public class " + getDaoClassName(clazz) + " extends GenericDao<" + className + "> {" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public void deleteById(int id) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        super.deleteById(id);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public void delete(List<Integer> ids) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        super.delete(ids);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public void updateSpecial(int id, String propertyName, String value) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        super.updateSpecial(id, propertyName, value);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public void updateSpecial(int id, String propertyName, int value) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        super.updateSpecial(id, propertyName, value);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public Object[] getPropertiesById(String entityName, int id, String propertyName1, String propertyName2) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.getPropertiesById(entityName, id, propertyName1, propertyName2);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public Object getOnePropertyById(String entityName, int id, String propertyName) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.getOnePropertyById(entityName, id, propertyName);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public " + className + " getByBean(Object obj) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.getByBean(obj);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public List<" + className + "> getList(String property, Object propertyValue) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.getList(property, propertyValue);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public List<" + className + "> getList(String property, Object propertyValue, boolean isLike) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.getList(property, propertyValue, isLike);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public Serializable save(Object obj) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.save(obj);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public List<" + className + "> find(Map condition) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.find(condition);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public int executeSql(String sql) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.executeSql(sql);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public String getStringById(int id, String propertyName) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.getStringById(id, propertyName);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "}";
//        System.out.println(tmplate);
        return tmplate;
    }

    public static String getControllerCode(Class clazz) {
        String className = clazz.getSimpleName();
        String packageName = clazz.getPackage().getName();
        String daoPackage = getParentPackage(packageName);
        String requestMapping = ValueWidget.title(className);
        String tmplate = "package " + daoPackage + ".web.controller;" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "import com.common.web.view.PageView;" + SystemHWUtil.CRLF/* \r\n */ +
                "import " + clazz.getName() + ";" + SystemHWUtil.CRLF/* \r\n */ +
                "import oa.web.controller.base.BaseController;" + SystemHWUtil.CRLF/* \r\n */ +
                "import org.apache.log4j.Logger;" + SystemHWUtil.CRLF/* \r\n */ +
                "import org.springframework.stereotype.Controller;" + SystemHWUtil.CRLF/* \r\n */ +
                "import org.springframework.ui.Model;" + SystemHWUtil.CRLF/* \r\n */ +
                "import org.springframework.web.bind.annotation.RequestMapping;" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "import javax.servlet.http.HttpServletRequest;" + SystemHWUtil.CRLF/* \r\n */ +
                "import javax.servlet.http.HttpServletResponse;" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "/**" + SystemHWUtil.CRLF/* \r\n */ +
                " * Created by auto generate on " + TimeHWUtil.getCurrentFormattedTime() + ".<br >" + SystemHWUtil.CRLF/* \r\n */ +
                " */" + SystemHWUtil.CRLF/* \r\n */ +
                "@Controller" + SystemHWUtil.CRLF/* \r\n */ +
                "@RequestMapping(\"/" + requestMapping + "\")" + SystemHWUtil.CRLF/* \r\n */ +
                "public class " + getControllerClassName(clazz) + " extends BaseController<" + className + "> {" + SystemHWUtil.CRLF/* \r\n */ +
                "    protected static Logger logger = Logger.getLogger(" + getControllerClassName(clazz) + ".class);" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    /***" + SystemHWUtil.CRLF/* \r\n */ +
                "     * 保存(新增一条记录)之前会调用" + SystemHWUtil.CRLF/* \r\n */ +
                "     * @param roleLevel" + SystemHWUtil.CRLF/* \r\n */ +
                "     * @param model" + SystemHWUtil.CRLF/* \r\n */ +
                "     * @param response" + SystemHWUtil.CRLF/* \r\n */ +
                "     * @return" + SystemHWUtil.CRLF/* \r\n */ +
                "     */" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    protected boolean beforeSave(" + className + " roleLevel, Model model, HttpServletResponse response) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.beforeSave(roleLevel, model, response);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    protected " + className + " detailTODO(int id, Model model, HttpServletRequest request, HttpServletResponse response) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.detailTODO(id, model, request, response);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    protected void deleteTODO(int id, " + className + " roleLevel, Model model, HttpServletRequest request) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        super.deleteTODO(id, roleLevel, model, request);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    /***" + SystemHWUtil.CRLF/* \r\n */ +
                "     * 保存修改前调用" + SystemHWUtil.CRLF/* \r\n */ +
                "     * @param roleLevel" + SystemHWUtil.CRLF/* \r\n */ +
                "     * @param justQuery" + SystemHWUtil.CRLF/* \r\n */ +
                "     */" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    protected void beforeUpdate(" + className + " roleLevel, " + className + " justQuery) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        super.beforeUpdate(roleLevel, justQuery);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    protected void beforeList(" + className + " roleLevel) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        super.beforeList(roleLevel);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    protected void listTODO(Model model, PageView view, HttpServletRequest request, HttpServletResponse response) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        super.listTODO(model, view, request, response);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    /***" + SystemHWUtil.CRLF/* \r\n */ +
                "     * 每个接口都会调用<br />" + SystemHWUtil.CRLF/* \r\n */ +
                "     *" + SystemHWUtil.CRLF/* \r\n */ +
                "     * @param model" + SystemHWUtil.CRLF/* \r\n */ +
                "     * @param request" + SystemHWUtil.CRLF/* \r\n */ +
                "     * @param response" + SystemHWUtil.CRLF/* \r\n */ +
                "     * @param requestType : 操作方式:<code>" + SystemHWUtil.CRLF/* \r\n */ +
                "     *                    public static final int REQUEST_TYPE_ADD = 1;" + SystemHWUtil.CRLF/* \r\n */ +
                "    public static final int REQUEST_TYPE_ADD_SAVE = 2;" + SystemHWUtil.CRLF/* \r\n */ +
                "    public static final int REQUEST_TYPE_DETAIL = 3;" + SystemHWUtil.CRLF/* \r\n */ +
                "    public static final int REQUEST_TYPE_DELETE = 4;" + SystemHWUtil.CRLF/* \r\n */ +
                "    public static final int REQUEST_TYPE_EDIT = 6;" + SystemHWUtil.CRLF/* \r\n */ +
                "    public static final int REQUEST_TYPE_UPDATE = 7;" + SystemHWUtil.CRLF/* \r\n */ +
                "    public static final int REQUEST_TYPE_LIST = 8;" + SystemHWUtil.CRLF/* \r\n */ +
                "     * </code>" + SystemHWUtil.CRLF/* \r\n */ +
                "     * @return" + SystemHWUtil.CRLF/* \r\n */ +
                "     */" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    protected String callback(Model model, HttpServletRequest request, HttpServletResponse response, int requestType) {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return super.callback(model, request, response, requestType);" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    protected void beforeAddInput(Model model, HttpServletRequest request) {" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    protected void errorDeal(Model model) {" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "" + SystemHWUtil.CRLF/* \r\n */ +
                "    @Override" + SystemHWUtil.CRLF/* \r\n */ +
                "    public String getJspFolder() {" + SystemHWUtil.CRLF/* \r\n */ +
                "        return \"" + requestMapping + "\";" + SystemHWUtil.CRLF/* \r\n */ +
                "    }" + SystemHWUtil.CRLF/* \r\n */ +
                "}";
//        System.out.println(tmplate);
        return tmplate;
    }

    public static String getParentPackage(String packageName) {
        return packageName.replaceAll("\\.[\\w]+$", SystemHWUtil.EMPTY);
    }

    public static void writeDaoCode(Class clazz, String packagePath, boolean overwrite) {
        String sourcePath = getSourceCodeParentPath(packagePath) + "dao";
        String code = getDaoCode(clazz);
        if (!sourcePath.endsWith("/")) {
            sourcePath = sourcePath + "/";
        }
        String daoPath = sourcePath + getDaoClassName(clazz) + ".java";
//        System.out.println("sourcePath :" + sourcePath);
        System.out.println("daoPath :" + daoPath);
        writeCodeToFile(code, daoPath, overwrite);
    }

    /***
     *
     * @param clazz
     * @param packagePath
     * @param overwrite : 是否覆写
     */
    public static void writeControllerCode(Class clazz, String packagePath, boolean overwrite) {
        String sourcePath = getSourceCodeParentPath(packagePath) + "web/controller";
        String code = getControllerCode(clazz);
        if (!sourcePath.endsWith("/")) {
            sourcePath = sourcePath + "/";
        }
        String daoPath = sourcePath + getControllerClassName(clazz) + ".java";
//        System.out.println("sourcePath :" + sourcePath);
        System.out.println("controller Path :" + daoPath);
        writeCodeToFile(code, daoPath, overwrite);
    }

    private static void writeCodeToFile(String code, String daoPath, boolean overwrite) {
        File daoFile = new File(daoPath);
        if (daoFile.exists()) {
            System.out.println("已经存在 :\t\t" + daoPath);
            if (overwrite) {
                daoFile.delete();
            } else {
                return;
            }
        }
        FileUtils.writeStrToFile(daoFile, code, true);
    }

    /***
     * 例如:"/Users/whuanghkl/code/mygit/convention/src/main/java/com/girltest"
     * @param packagePath
     * @return
     */
    public static String getSourceCodeParentPath(String packagePath) {
        return packagePath.replace("src/main/webapp/WEB-INF/classes", "src/main/java")
                .replace("target/classes", "src/main/java").replaceAll("[\\w]+$", SystemHWUtil.EMPTY);
    }

    /***
     * 会写入文件
     * @param packageName
     */
    public static void generateDaoFile(String packageName, boolean overwrite) {
        List<Class<?>> classes = ClassFinder.find(packageName);
        int size = classes.size();
        for (int i = 0; i < size; i++) {
            Class clazz = classes.get(i);
            String className = clazz.getSimpleName();
//            System.out.println("className :" + className);
            CodeGenerateHelper.writeDaoCode(clazz, ClassFinder.getPackagePath(packageName), overwrite);
        }
    }

    /***
     * 会写入文件
     * @param packageName
     */
    public static void generateControllerFile(String packageName, boolean overwrite) {
        List<Class<?>> classes = ClassFinder.find(packageName);
        int size = classes.size();
        for (int i = 0; i < size; i++) {
            Class clazz = classes.get(i);
            String className = clazz.getSimpleName();
//            System.out.println("className :" + className);
            CodeGenerateHelper.writeControllerCode(clazz, ClassFinder.getPackagePath(packageName), overwrite);
        }
    }
}
