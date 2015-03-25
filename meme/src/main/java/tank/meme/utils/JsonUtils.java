package tank.meme.utils;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * 
 * @author tank
 *
 */
public class JsonUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);
	public static ObjectMapper objectMapper = new ObjectMapper();

	public static String toJson(Object object) {
		if (object == null) {
			LOGGER.error("对象为空不能转换json:{}", object);
			return null;
		}
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			LOGGER.error("将对象序列化成JSON字符串时发生解析异常:{}", e);
		}
		return null;
	}

	/**
	 * 将指定的JSON格式的字符串解析成指定类型的对象
	 * 
	 * @param <T>
	 *            返回的对象类型
	 * @param json
	 *            要解析成对象的JSON格式字符串
	 * @param type
	 *            指定要解析成对象的类型
	 * @return 返回解析对象
	 * @throws JsonDeserializeException
	 *             非检查型异常,表示解析过程中产生的错误
	 */
	public static <T> T toBean(String json, Class<T> type) {
		if (StringUtils.isEmpty(json)) {
			LOGGER.error("对象为空不能转换json:{}", json);
			return null;
		}
		try {
			return objectMapper.readValue(json, type);
		} catch (JsonParseException e) {
			LOGGER.error("解析异常:{}", e);
		} catch (JsonMappingException e) {
			LOGGER.error("字段映射异常:{}", e);
		} catch (IOException e) {
			LOGGER.error("IO异常:{}", e);
		}
		return null;
	}

	/**
	 * 将指定的JSON格式的字符串解析成指定类型的对象
	 * 
	 * @param <T>
	 *            返回的对象类型
	 * @param json
	 *            要解析成对象的JSON格式字符串
	 * @param valueTypeRef
	 *            指定要解析成对象的类型
	 * @return 返回解析对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(String json, TypeReference<?> valueTypeRef) {
		if (StringUtils.isEmpty(json)) {
			LOGGER.error("对象为空不能转换json:{}", json);
			return null;
		}
		try {
			return (T) objectMapper.readValue(json, valueTypeRef);
		} catch (JsonParseException e) {
			LOGGER.error("解析异常:{}", e);
		} catch (JsonMappingException e) {
			LOGGER.error("字段映射异常:{}", e);
		} catch (IOException e) {
			LOGGER.error("IO异常:{}", e);
		}
		return null;
	}

	public static JsonNode toJsonNode(String json) {
		if (StringUtils.isEmpty(json)) {
			LOGGER.error("对象为空不能转换json:{}", json);
			return null;
		}
		try {
			return objectMapper.readValue(json, JsonNode.class);
		} catch (IOException e) {
			LOGGER.error("将JSON字符串解析成对象时发生解析异常:{}", e);
		}
		return null;
	}

	public static JsonNode toArrayNode(String json) {
		if (StringUtils.isEmpty(json)) {
			LOGGER.error("对象为空不能转换json:{}", json);
			return null;
		}
		try {
			return objectMapper.readValue(json, ArrayNode.class);
		} catch (IOException e) {
			LOGGER.error("将JSON字符串解析成对象时发生解析异常:{}", e);
		}
		return null;
	}
}
