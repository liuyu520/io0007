package com.common.util;

import com.io.hw.file.util.FastByteArrayOutputStream;
import com.string.widget.util.ValueWidget;
import org.springframework.beans.BeanUtils;

import java.io.*;
import java.util.List;

/***
 * deep copy object
 * @author huangwei
 * @since 2015年3月5日
 */
public class BeanHWUtil {
	/**
	 * This method makes a "deep clone" of any Java object it is given.
	 */
	public static Object deepClone(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns a copy of the object, or null if the object cannot be serialized.<br>
	 * refre:http://javatechniques.com/blog/faster-deep-copies-of-java-objects/
	 */
	public static Object copy(Object orig) {
		Object obj = null;
		try {
			// Write the object out to a byte array
			FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(fbos);
			out.writeObject(orig);
			out.flush();
			out.close();

			// Retrieve an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
			obj = in.readObject();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}

    public static boolean isEntity(Object value) {
        return isEntity(value.getClass());
    }

    public static boolean isEntity(Class clazz) {
        String package2 = clazz.getName();
        return isEntity(package2);
    }

    public static boolean isEntity(String package2) {
        return (package2.contains("entity.") ||
                package2.contains("domain."));
    }

    public static void copyProperties(Object source, Object target) throws BeansException {
        BeanUtils.copyProperties(source, target);
    }

    public static void copyProperties(Object source, Object target, String... ignoreProperties) throws BeansException {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    /***
     * 排除hibernate字段
     * @param source
     * @param target
     */
    public static void copyPropsExcludHibernate(Object source, Object target, String... ignoreProperties) {
        ignoreProperties = getIgnoreProperties(ignoreProperties);

        copyProperties(source, target, ignoreProperties);
    }

    /***
     * 会自动排除hibernate 字段
     * @param ignoreProperties
     * @return
     */
    public static String[] getIgnoreProperties(String[] ignoreProperties) {
        String[] props = new String[]{"handler", "hibernateLazyInitializer", "logger"};
        ignoreProperties = getIgnoreProperties(ignoreProperties, props);
        return ignoreProperties;
    }

    public static String[] getIgnoreProperties(String[] ignoreProperties, String[] props) {
        if (ValueWidget.isNullOrEmpty(ignoreProperties)) {
            return props;
        }
        if (ValueWidget.isNullOrEmpty(props)) {
            return ignoreProperties;
        }
        ignoreProperties = ValueWidget.copyArray(ignoreProperties, props);

        return ignoreProperties;
    }


    /***
     * 没有传递target,<br />
     *
     * @param source
     * @param ignoreProperties
     * @return : target
     */
    public static Object copyProperties(Object source, String... ignoreProperties) {
        Object target = ReflectHWUtils.createEmptyObj(source.getClass());
        copyProperties(source, target, ignoreProperties);
        return target;
    }

    public static Object copyProperties(CopyPropsParam copyPropsParam, String... ignoreProperties) {
        //1. 生成ignoreProperties
        if (copyPropsParam.getExcludHibernate()) {
            ignoreProperties = getIgnoreProperties(ignoreProperties);
//            copyPropsExcludHibernate(copyPropsParam.getSource(),copyPropsParam.getTarget(),ignoreProperties);
        }
        if (copyPropsParam.getExcludeNullProps()) {
            List<String> nullColumns = ReflectHWUtils.getNullColumns(copyPropsParam.getSource(), true);
            String[] ignoreProperties2 = ValueWidget.list2Array(nullColumns);
            ignoreProperties = getIgnoreProperties(ignoreProperties, ignoreProperties2);

        }
        if (null == copyPropsParam.getTarget()) {
            return copyProperties(copyPropsParam.getSource(), ignoreProperties);
        } else {
            copyProperties(copyPropsParam.getSource(), copyPropsParam.getTarget(), ignoreProperties);
            return copyPropsParam.getTarget();
        }

    }
}
