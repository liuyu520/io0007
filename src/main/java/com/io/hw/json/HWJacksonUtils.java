package com.io.hw.json;

import com.string.widget.util.ValueWidget;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HWJacksonUtils {
    protected static final Logger logger = Logger.getLogger(HWJacksonUtils.class);
    private static ObjectMapper mapper =null;

	public static ObjectMapper getObjectMapper(){
		if(mapper==null){
			mapper = new ObjectMapper();
		}
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
		return mapper;
	}
	/***
	 * 用于jsonp调用
	 * @param map : 用于构造json数据
	 * @param callback : 回调的javascript方法名
	 * @param filters : <code>SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
				.serializeAllExcept("content");
		FilterProvider filters = new SimpleFilterProvider().addFilter(
				Constant2.SIMPLEFILTER_JACKSON_PAPERNEWS, theFilter);</code>
	 * @return : js函数名(json字符串)
	 */
	public static String getJsonP(Object map,String callback,FilterProvider filters)
	{
		if(ValueWidget.isNullOrEmpty(map)){
			return null;
		}
		String content = null;
		if(map instanceof String){
			content=(String)map;
		}else{
		ObjectMapper mapper = getObjectMapper();
		
		ObjectWriter writer=null;
		try {
			if(filters!=null){
				writer=mapper.writer(filters);
			}else{
				writer=mapper.writer();
			}
			content=writer.writeValueAsString(map);
			logger.info(content);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		if(ValueWidget.isNullOrEmpty(callback)){
			return content;
		}
		return callback+"("+content+");";
	}
	/***
	 * jackson没有过滤
	 * @param map
	 * @param callback
	 * @return
	 */
	public static String getJsonP(Object map,String callback)
	{
		return getJsonP(map, callback, null);
	}
	public static String getJsonP(Object map)
	{
		return getJsonP(map, null, null);
	}
	/***
	 * 
	 * @param key
	 * @param value2
	 * @param callback
	 * @return : js函数名(json字符串)
	 */
	public static String getJsonP(String key ,Object value2,String callback){
		Map map = new HashMap();
		map.put(key, value2);
		return getJsonP(map, callback);
	}
	/**   
     * 获取泛型的Collection Type  
     * @param collectionClass 泛型的Collection   
     * @param elementClasses 元素类   
     * @return JavaType Java类型   
     * @since 1.0   
     */  
	public static JavaType getCollectionType(ObjectMapper mapper,Class<?> collectionClass, Class<?>... elementClasses) {   
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
    } 
	/***
	 * 反序列化
	 * @param jsonInput
	 * @param clazz
	 * @return
	 */
	public static Object deSerialize(String jsonInput,Class clazz){
        if (ValueWidget.isNullOrEmpty(jsonInput)) {
            return null;
        }
        Object obj=null;
		try {
			ObjectMapper mapper = getObjectMapper();
//			DeserializationConfig deserializationConfig=mapper.getDeserializationConfig();
//			mapper.setDeserializationConfig(deserializationConfig)
			obj = mapper.readValue(jsonInput, clazz);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/***
	 * 反序列化
	 * @param jsonInput
	 * @param clazz
	 * @return
	 */
	public static List deSerializeList(String jsonInput,Class clazz){
		List obj=null;
		try {
			ObjectMapper mapper = getObjectMapper();
			JavaType javaType = getCollectionType(mapper,ArrayList.class, clazz); 
			obj = mapper.readValue(jsonInput, javaType);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static Object unSerialize(String jsonInput,Class clazz){
		return deSerialize(jsonInput, clazz);
	}
}
