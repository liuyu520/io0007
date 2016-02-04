package com.common.util;

import com.string.widget.util.ValueWidget;
import com.time.util.TimeHWUtil;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

public class ReflectHWUtils {

	/***
	 * get all field ,including fields in father/super class
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<Field> getAllFieldList(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		List<Field> fieldsList = new ArrayList<Field>();// return object
		Class<?> superClass = clazz.getSuperclass();// father class
		if(ValueWidget.isNullOrEmpty(superClass)){
			return null;
		}
		if (!superClass.getName().equals(Object.class.getName()))/*
																 * java.lang.Object
																 */{

			// System.out.println("has father");
			fieldsList.addAll(getAllFieldList(superClass));// Recursive
		}
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			// 排除因实现Serializable 接口而产生的属性serialVersionUID
			if (!field.getName().equals("serialVersionUID")) {
				fieldsList.add(field);
			}
		}
		return fieldsList;
	}

	/***
	 * 设置对象的属性值。
	 * 
	 * @param obj
	 * @param propertyName
	 *            : property name
	 * @param propertyValue
	 *            : value of property<br> must be String or Field
	 * @param isIgnoreNull : 是否强制设置,不管<code>propertyValue</code>是否为null<br>
	 * true:不设置  ; false:设置
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setObjectValue(Object obj, Object propertyName,
			Object propertyValue,boolean isIgnoreNull) throws SecurityException,
			IllegalArgumentException,
			IllegalAccessException {
		if (ValueWidget.isNullOrEmpty(propertyName)
				|| ValueWidget.isNullOrEmpty (obj)) {
			return;
		}
		if((propertyValue==null)&&isIgnoreNull){
			return;
		}
		Class<?> clazz = obj.getClass();
		Field name = null;
		if(propertyName instanceof String){
			name=getSpecifiedField(clazz, (String)propertyName);
		}else{
			name=(Field)propertyName;
		}
		if(!ValueWidget.isNullOrEmpty(name)){
			name.setAccessible(true);
			name.set(obj, propertyValue);
		}
	}
	/***
	 * 当propertyValue 为null 时,忽略
	 * @param obj
	 * @param propertyName
	 * @param propertyValue
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setObjectValue(Object obj, Object propertyName,
			Object propertyValue) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		setObjectValue(obj, propertyName, propertyValue, true);
	}
	/***
	 * 利用反射设置对象的属性值. 注意:属性可以没有setter 方法.
	 * 
	 * @param obj
	 * @param params
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setObjectValue(Object obj, Map<String, Object> params)
			throws SecurityException,
			IllegalArgumentException, IllegalAccessException {
		if(ValueWidget.isNullOrEmpty(obj)){
			return;
		}
		if (ValueWidget.isNullOrEmpty(params)) {
			return;
		}
		Class<?> clazz = obj.getClass();
		for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it
					.next();
			String key = entry.getKey();
			Object propertyValue = entry.getValue();
			if (ValueWidget.isNullOrEmpty(propertyValue)) {
				continue;
			}
			Field name = getSpecifiedField(clazz, key);
			if (name != null) {
				name.setAccessible(true);
				name.set(obj, propertyValue);
			}
		}

	}

	/***
	 * Get Specified Field
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getSpecifiedField(Class<?> clazz, String fieldName) {
		if (ValueWidget.isNullOrEmpty(clazz)||ValueWidget.isNullOrEmpty(fieldName)) {
			return null;
		}
		Field f = null;
		try {
			f = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class superClass2=clazz.getSuperclass();
			if (superClass2.getName().equals(Object.class.getName())){
				return null;
			}
			return getSpecifiedField(superClass2/*
														 * may be null if it is
														 * Object .
														 */, fieldName);
			// e.printStackTrace();
		}
		return f;
	}

	/***
	 * 获取指定对象的属性值
	 * 
	 * @param obj
	 * @param name
	 *            :Field
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getObjectValue(Object obj, Field name)
			throws SecurityException,
			IllegalArgumentException, IllegalAccessException {

		// Field f = getSpecifiedField(obj.getClass(), name.getName());
		if (name == null) {
			System.out.println("[ReflectHWUtils.getObjectValue]"
					+ obj.getClass().getName() + " does not has field ");
			return null;
		}
		name.setAccessible(true);
		return name.get(obj);
	}

	/***
	 * 获取指定对象的属性值
	 * 
	 * @param obj
	 * @param propertyName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getObjectValue(Object obj, String propertyName)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		if (ValueWidget.isNullOrEmpty(obj)||ValueWidget.isNullOrEmpty(propertyName)) {
			return null;
		}
		Class<?> clazz = obj.getClass();
		Field name = getSpecifiedField(clazz, propertyName);
		if (ValueWidget.isNullOrEmpty(name)) {
			propertyName=ValueWidget.title(propertyName);//convert "Key2" to "key2"
			name = getSpecifiedField(clazz, propertyName);
			
			if (ValueWidget.isNullOrEmpty(name)) {
				System.out.println("[ReflectHWUtils.getObjectValue]"
						+ obj.getClass().getName() + " does not has field "
						+ propertyName);
				return null;
			}
		}
		return getObjectValue(obj, name);
	}

	/***
	 * 通过反射获取成员变量的值
	 * @param obj
	 * @param propertyName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static int getObjectIntValue(Object obj, String propertyName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		Object val=getObjectValue(obj, propertyName);
		if(val instanceof Integer){
			int id=(Integer)val;
			return id;
		}else if(val instanceof Long){
			Long id=(Long)val;
			return id.intValue();
		}
		return Integer.parseInt((String)val);
	}
	/***
	 * Determine whether the object's fields are empty
	 * 
	 * @param obj
	 * @param isExcludeZero :true:数值类型的值为0,则当做为空;<br>----false:数值类型的值为0,则不为空
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static boolean isNullObject(Object obj, boolean isExcludeZero)
			throws SecurityException, IllegalArgumentException,
			NoSuchFieldException, IllegalAccessException {
		return isNullObject(obj, false/*ignoreNumber*/, isExcludeZero);
	}
	/***
	 * Determine whether the object's fields are empty
	 * 
	 * @param obj
	 * @param isExcludeZero  :true:数值类型的值为0,则当做为空;<br>----false:数值类型的值为0,则不为空
	 * <br >当ignoreNumber值为true时,isExcludeZero 无效.
	 * @param ignoreNumber :当值为true时,isExcludeZero 无效.
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static boolean isNullObject(Object obj, boolean ignoreNumber,boolean isExcludeZero)
			throws SecurityException, IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException {
		if(ValueWidget.isNullOrEmpty(obj)){//对象本身就为null
			return true;
		}
		List<Field> fieldList = ReflectHWUtils.getAllFieldList(obj.getClass());
		boolean isNull = true;
		for (int i = 0; i < fieldList.size(); i++) {
			Field f = fieldList.get(i);
			Object propertyValue = null;
			propertyValue = getObjectValue(obj, f);

			if (!ValueWidget.isNullOrEmpty(propertyValue)) {// 字段不为空
				if(ignoreNumber){
					if (propertyValue instanceof Integer||propertyValue instanceof Long||propertyValue instanceof Double||propertyValue instanceof Float||propertyValue instanceof Short){
						continue;
					}
				}
				if (propertyValue instanceof Integer) {// 是数字
					if (!((Integer) propertyValue == 0 && isExcludeZero)) {
						isNull = false;
						break;
					}
				} else if (propertyValue instanceof Double) {// 是数字
					if (!((Double) propertyValue == 0 && isExcludeZero)) {
						isNull = false;
						break;
					}
				}else if (propertyValue instanceof Float) {// 是数字
					if (!((Float) propertyValue == 0 && isExcludeZero)) {
						isNull = false;
						break;
					}
				}else if (propertyValue instanceof Short) {// 是数字
					if (!((Short) propertyValue == 0 && isExcludeZero)) {
						isNull = false;
						break;
					}
				}else if (propertyValue instanceof Long) {// 是数字
					if (!((Long) propertyValue == 0 && isExcludeZero)) {
						isNull = false;
						break;
					}
				} else {
					isNull = false;
					break;
				}
			}
		}
		return isNull;
	}

	/***
	 * 判断两个对象的属性值是否都相等.
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static boolean isSamePropertyValue(Object obj1, Object obj2)
			throws SecurityException, IllegalArgumentException,
			NoSuchFieldException, IllegalAccessException {
		List<String> exclusiveProperties = null;
		return isSamePropertyValue(obj1, obj2, exclusiveProperties);
	}

	/***
	 * 判断两个对象的属性值是否都相等.
	 * 
	 * @param obj1
	 * @param obj2
	 * @param exclusiveProperty
	 *            or String[]
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static boolean isSamePropertyValue(Object obj1, Object obj2,
			String... exclusiveProperty) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {
		List<String> exclusiveProperties = new ArrayList<String>();
		exclusiveProperties.addAll(Arrays.asList(exclusiveProperty));
		return isSamePropertyValue(obj1, obj2, exclusiveProperties);
	}

	/***
	 * 判断两个对象的属性值是否都相等.
	 * 
	 * @param obj1
	 * @param obj2
	 * @param exclusiveProperties
	 *            : 要过滤的属性
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public static boolean isSamePropertyValue(Object obj1, Object obj2,
			List<String> exclusiveProperties) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {
		if(!SystemHWUtil.checkSameByNull(obj1, obj2)){
			return false;
		}
		if((obj1 instanceof List)&&!(obj2 instanceof List)){
			return false;
		}else if((obj2 instanceof List)&&!(obj1 instanceof List)){
			return false;
		}else if(obj1 instanceof List){
			List list1=(List)obj1;
			List list2=(List)obj2;
			if(list1.size()!=list2.size()){
				System.out.println("list 长度不同");
				return false;
			}
			for(int i=0;i<list1.size();i++){
				Object objOne=list1.get(i);
				Object objTwo=list2.get(i);
				if(!isSamePropertyValue(objOne, objTwo,exclusiveProperties)){
//					System.out.println(objOne);
					return false;
				}
			}
			return true;
		}
		if((obj1 instanceof Map)&&!(obj2 instanceof Map)){
			return false;
		}else if((obj2 instanceof Map)&&!(obj1 instanceof Map)){
			return false;
		}else if(obj1 instanceof Map){
			Map map1=(Map)obj1;
			Map map2=(Map)obj2;
			if(map1.size()!=map2.size()){
				System.out.println("map 长度不同");
				return false;
			}
			return SystemHWUtil.compareMapByEntrySet(map1, map2);
		}
		List<Field> fieldsList = getAllFieldList(obj1.getClass()); 
		for (int i = 0; i < fieldsList.size(); i++) {
			Field f = fieldsList.get(i);
			if ((!ValueWidget.isNullOrEmpty(exclusiveProperties))
					&& exclusiveProperties.contains(f.getName())) {// 过滤掉，不比较
				continue;
			}
			Object propertyValue1 = getObjectValue(obj1, f);
			Object propertyValue2 = getObjectValue(obj2, f);

//			System.out.println(f.getName());
			if (propertyValue1 == propertyValue2) {// if propertyValue1 is null
				continue;
			}
			if(!SystemHWUtil.checkSameByNull(propertyValue1, propertyValue2)){
				return false;
			}
			if(isSimpleType(propertyValue1)||isSimpleType(propertyValue2)){//简单对象,比如Integer,Long,String
				if (!isSameBySimpleTypes(propertyValue1, propertyValue2)) {
					System.out.println(propertyValue1);
					System.out.println(obj1);
					return false;
				}
			}else{
				if(!isSamePropertyValue(propertyValue1, propertyValue2,exclusiveProperties)){//复杂对象
//					System.out.println(propertyValue1);
					return false;
				}
			}
			
		}
		return true;
	}

	/***
	 * 判断是否是简单类型
	 * @param obj1
	 * @return
	 */
	public static boolean isSimpleType(Object obj1){
		if (obj1 instanceof Integer) {// int
			return true;
		} else if (obj1 instanceof Double) {// double
			return true;
		} else if (obj1 instanceof Number) {// Number
			return true;
		}else if (obj1 instanceof Boolean) {// Boolean
			return true;
		} else if (obj1 instanceof String) {
			return true;
		} else if (obj1 instanceof Timestamp) {
			return true;
		} else if (obj1 instanceof java.util.Date) {
			return true;
		} else if (obj1 instanceof java.sql.Date) {
			return true;
		}
		return false;
	}

	/***
	 * 比较java 基本类型的值是否相同.
	 * 
	 * @param obj1
	 *            : String,Integer,Double,Boolean
	 * @param obj2
	 * @return
	 */
	public static boolean isSameBySimpleTypes(Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		}
		if (obj1 instanceof Integer) {// int
			Integer int1 = (Integer) obj1;
			Integer int2 = (Integer) obj2;
			return int1.intValue() == int2.intValue();
		} else if (obj1 instanceof Float) {// 
			Float int1 = (Float) obj1;
			Float int2 = (Float) obj2;
			return int1.compareTo(int2) == 0;
		} else if (obj1 instanceof Double) {// double
			Double double1 = (Double) obj1;
			Double double2 = (Double) obj2;
			return double1.compareTo(double2) == 0;
		} else if (obj1 instanceof Boolean) {// double
			Boolean boolean1 = (Boolean) obj1;
			Boolean boolean2 = (Boolean) obj2;
			return boolean1.compareTo(boolean2) == 0;
		} else if (obj1 instanceof String) {
			String str1 = (String) obj1;
			String str2 =null;
			if(obj2 instanceof String){
				str2 =(String) obj2;
			}else {
				str2=String.valueOf(obj2);//java.lang.Charater
			}
			
			return str1.equals(str2);
		} else if (obj1 instanceof Timestamp) {
			Timestamp time1 = (Timestamp) obj1;
			Timestamp time2 = (Timestamp) obj2;
			return time1.compareTo(time2) == 0;
		} else if (obj1 instanceof java.util.Date) {
			java.util.Date time1 = (java.util.Date) obj1;
			java.util.Date time2 = (java.util.Date) obj2;
			return time1.compareTo(time2) == 0;
		} else if (obj1 instanceof java.sql.Date) {
			java.sql.Date time1 = (java.sql.Date) obj1;
			java.sql.Date time2 = (java.sql.Date) obj2;
			return time1.compareTo(time2) == 0;
		}
		return obj1 == obj2;
	}

	/***
	 * 对object中的所有成员变量的值,执行trim操作<br>
	 * 即去掉首尾的空格
	 * @param obj
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void trimObject(Object obj) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		if(obj==null){
			return;
		}
		List<Field> fieldsList =getAllFieldList(obj.getClass());
		for(int i=0;i<fieldsList.size();i++){
			Field f=fieldsList.get(i);
			Object vObj=getObjectValue(obj,f );
			if(f.getType().getName().equals(String.class.getName()/*"java.lang.String"*/) && (vObj instanceof String) ){
				String str=(String)vObj;
				str=str.trim();
				f.setAccessible(true);
				f.set(obj, str);
			}
		}
	}
	/***
	 * 给String类型的成员变量值加上前缀和后缀
	 * @param obj
	 * @param prefix
	 * @param suffix
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void modifyObject(Object obj,String prefix,String suffix) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		if(obj==null){
			return;
		}
		List<Field> fieldsList =getAllFieldList(obj.getClass());
		for(int i=0;i<fieldsList.size();i++){
			Field f=fieldsList.get(i);
			Object vObj=getObjectValue(obj,f );
			if(f.getType().getName().equals(String.class.getName()/*"java.lang.String"*/) /*&& (vObj instanceof String)*/ ){
				String str=(String)vObj;
				if(str==null){
					str=SystemHWUtil.EMPTY;
				}
				if(!ValueWidget.isNullOrEmpty(prefix)){
					str=prefix+str;
				}
				if(!ValueWidget.isNullOrEmpty(suffix)){
					str=str+suffix;
				}
				f.setAccessible(true);
				f.set(obj, str);
			}
		}
	}
	/***
	 * 遍历List,给其中每个对象,指定字段值增加前缀和后缀
	 * @param list
	 * @param fieldName
	 * @param prefix
	 * @param suffix
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void modifyObjectFromList(List<?> list,String fieldName,String prefix,String suffix) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		if(ValueWidget.isNullOrEmpty(list)){
			return;
		}
		Object obj22=list.get(0);
		Class clazz=obj22.getClass();
		Field f=getSpecifiedField(clazz, fieldName);
		int length=list.size();
		for(int i=0;i<length;i++){
			Object obj=list.get(i);
			Object vObj=getObjectValue(obj,f );
			String str;
			if(vObj==null){
				str=SystemHWUtil.EMPTY;
			}else{
				str=vObj.toString();
			}
			
			if(!ValueWidget.isNullOrEmpty(prefix)){
				str=prefix+str;
			}
			if(!ValueWidget.isNullOrEmpty(suffix)){
				str=str+suffix;
			}
			f.setAccessible(true);
			f.set(obj, str);
		}
	}
	
	/***
	 * 把对象中空字符串改为null
	 * @param obj : 要修改的对象:java bean
	 * @param isTrim : 是否清除成员变量的值前后的空格
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void convertEmpty2Null(Object obj,boolean isTrim) 
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		if(obj==null){
			return;
		}
		List<Field> fieldsList =getAllFieldList(obj.getClass());
		if(ValueWidget.isNullOrEmpty(fieldsList)){
			return ;
		}
		for(int i=0;i<fieldsList.size();i++){
			Field f=fieldsList.get(i);
			Object vObj=getObjectValue(obj,f );
			if(f.getType().getName().equals(String.class.getName()/*"java.lang.String"*/) && (vObj instanceof String) ){
				String str=(String)vObj;
				if(isTrim){//清除首尾的空格
					str=str.trim();//str can not be null
				}
				if(SystemHWUtil.EMPTY.equals(str)){
//					System.out.println(f.getName());
//					System.out.println(f.getType().getName());
					f.setAccessible(true);
					f.set(obj, null);
				}else{
					if(isTrim){//清除首尾的空格
						f.setAccessible(true);
						f.set(obj, str);
					}
				}
			}
		}
	}
	
	/***
	 * 不trim
	 * @param obj
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void convertEmpty2Null(Object obj) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		if(obj==null){
			return;
		}
		convertEmpty2Null(obj, false/* isTrim */);
	}
	/***
	 * 把对象转化为map,<br>对象的属性(成员变量)名作为map的key;对象的属性值作为map的value
	 * @param obj
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Map<String,Object> parseObject(Object obj,List<String> excludeProperties) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		List<Field> fields=getAllFieldList(obj.getClass());
		Map<String,Object>map=new TreeMap<String, Object>();
		for(int i=0;i<fields.size();i++){
			Field field=fields.get(i);
			String fieldName=field.getName();
			if(ValueWidget.isNullOrEmpty(excludeProperties)||(!excludeProperties.contains(fieldName))){
				Object propertyValue=getObjectValue(obj, field);
				map.put(fieldName, propertyValue);
			}
		}
		return map;
	}
	public static Map<String,Object> parseObject(Object obj,String excludeProperty) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		List<String> excludeProperties=new ArrayList<String>();
		excludeProperties.add(excludeProperty);
		return parseObject(obj, excludeProperties);
	}
	public static Map<String,Object> parseObject(Object obj) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		return parseObject(obj, (List<String>)null);
	}
	/***
	 * 把List 转化为Map
	 * @param list
	 * @param clazz
	 * @param keyProperty : 该成员变量的值作为map的key
	 * @param valueProperty : 该成员变量的值作为map的value
	 * @return
	 */
	public static Map<String,Object> parseObjectList(List list,Class clazz,String keyProperty,String valueProperty){
		if(ValueWidget.isNullOrEmpty(list)){//容错性校验
			return null;
		}
		Map<String,Object> map=new HashMap<String, Object>();
		for(int i=0;i<list.size();i++){
			Object obj=list.get(i);
			Field keyf =null;
			Field valuef =null;
			try {
				keyf = clazz.getDeclaredField(keyProperty);
			} catch (NoSuchFieldException e) {
				keyf= getSpecifiedField(clazz.getSuperclass()/*
															 * may be null if it is
															 * Object .
															 */, keyProperty);
				// e.printStackTrace();
			}
			try {
				valuef = clazz.getDeclaredField(valueProperty);
			} catch (NoSuchFieldException e) {
				valuef= getSpecifiedField(clazz.getSuperclass()/*
															 * may be null if it is
															 * Object .
															 */, valueProperty);
				// e.printStackTrace();
			}
			keyf.setAccessible(true);
			valuef.setAccessible(true);
			try {
				map.put((String)keyf.get(obj), valuef.get(obj));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	/***
	 * 把对象中的列,类型为时间的都设置为当前时间
	 * @param obj
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void fillTimeForObj(Object obj) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		List<Field> fieldsList=getAllFieldList(obj.getClass());
		int size=fieldsList.size();
		for(int i=0;i<size;i++){
			Field f=fieldsList.get(i);
			String typeName=f.getType().getName();
			if(typeName.equals("java.sql.Timestamp")||typeName.equals("java.util.Date")){
				setObjectValue(obj, f, TimeHWUtil.getCurrentTimestamp());
			}else if(typeName.equals("java.sql.Date")){
				setObjectValue(obj, f, new java.util.Date());
			}
		}
	}

	/***
	 * 使用spring MVC保存对象时,对象的createTime(时间类型的属性)没有注入进来,
	 * <br>这时需要后台先通过id获取持久化对象,然后手动注入
	 * @param editedObj
	 * @param persistentObj
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void fillTimeForEditedObj(Object editedObj,Object persistentObj) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		if(!editedObj.getClass().getName().equals(persistentObj.getClass().getName())){
			throw new RuntimeException("Two object type should be the same ,but is different .");
		}
		List<Field> fieldsList=getAllFieldList(editedObj.getClass());
		int size=fieldsList.size();
		for(int i=0;i<size;i++){
			Field f=fieldsList.get(i);
			String typeName=f.getType().getName();
			if(editedObj instanceof java.util.Date || typeName.equals("java.sql.Timestamp")||typeName.equals("java.util.Date")||typeName.equals("java.sql.Date")){
				Object valueEdited=getObjectValue(editedObj, f);
				Object valuePersistent=getObjectValue(persistentObj, f);
				if(ValueWidget.isNullOrEmpty(valueEdited)&&(!ValueWidget.isNullOrEmpty(valuePersistent))){
					setObjectValue(editedObj, f, valuePersistent);
				}
				
			}
		}
	}
	/***
	 * 获取指定值的对象
	 * @param list
	 * @param property
	 * @param valueCompare
	 * @return
	 */
	public static Object getObjFromList(List<?> list,String property,Object valueCompare)
	{
		if(ValueWidget.isNullOrEmpty(list)){
			return null;
		}
		for(int i=0;i<list.size();i++){
			Object obj=list.get(i);
			Field keyf =null;
			keyf = getSpecifiedField(obj.getClass(), property);
			keyf.setAccessible(true);
			
			try {
				Object value22=keyf.get(obj);
				if(isSameBySimpleTypes(value22, valueCompare)){
					return obj;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/***
	 * convert map to object ,see setObjectValue(obj, map)
	 * @param map : key是对象的成员变量,其value就是成员变量的值
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 */
	public static Object convertMap2Obj(Map<String, Object> map,Class clazz) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException, IllegalArgumentException{
		if(ValueWidget.isNullOrEmpty(map)){
			return null;
		}
		Object obj=clazz.newInstance();
		setObjectValue(obj, map);
		/*for(Iterator it=map.entrySet().iterator();it.hasNext();){
			Map.Entry<String, Object> entry=(Map.Entry<String, Object>)it.next();
			String key=entry.getKey();
			Object val=entry.getValue();
		}*/
		return obj;
	}
	/***
	 * 从List中删除指定的对象
	 * @param list
	 * @param property
	 * @param valueCompare
	 */
	public static void deleteOneFromList(List<?> list,String property,Object valueCompare){
		if(ValueWidget.isNullOrEmpty(list)){
			return;
		}
		Object obj=getObjFromList(list, property, valueCompare);
		if(obj!=null){
			list.remove(obj);
		}
	}
	/***
	 * 删除成员变量<code>propertyColumn<code>值为null的对象
	 * @param list
	 * @param propertyColumn : 判断其值是否为null
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public static void deleteNullEle4List(List<?>list,String propertyColumn) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		int length=list.size();
		for(int i=0;i<length;i++){
			Object obj=list.get(i);
			Object val=getObjectValue(obj, propertyColumn);
			if(val==null){
				list.remove(obj);
				length=length-1;
				i=i-1;
			}
		}
	}
	
	/***
	 * 把指定属性都设置为null(空)
	 * @param list
	 * @param propertyColumn
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setNull4specified(List list,String propertyColumn) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		int size=list.size();
		for(int i=0;i<size;i++){
			Object obj=list.get(i);
			setObjectValue(obj, propertyColumn, null,false);
//			System.out.println(obj);
		}
	}
	/***
	 * search keyWord from List<Bean>
	 * @param list
	 * @param keyWord
	 * @param propertyName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static List search(List list,String keyWord,String propertyName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		List resultList=new ArrayList();
		int length=list.size();
		for(int i=0;i<length;i++){
			Object obj=list.get(i);
			String val=(String) getObjectValue(obj, propertyName);
			if(val.equalsIgnoreCase(keyWord)||val.contains(keyWord)){
				resultList.add(obj);
			}
		}
		return resultList;
	}
}
