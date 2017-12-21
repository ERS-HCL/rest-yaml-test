package com.github.rest.yaml.test.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {
	private static ObjectMapper mapper = new ObjectMapper();

	public static String toJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		}  catch (Exception e) {
			throw new TestException("Can not convert object to json: "+ object.getClass().getName(), e);
		}
	}
	
	public static <T> T toObject(Map<?, ?> map, Class<T> klass) {
		T object = null;
		try {
			String json = mapper.writeValueAsString(map);
			object = mapper.readValue(json, klass);
		} catch (Exception e) {
			throw new TestException("Exception parsing class" + klass.getName(), e);
		}
		return object;
	}

	public static <T> List<T> toObject(List<Map<?, ?>> list, Class<T> klass) {
		List<T> objects = new ArrayList<T>();
		for (Map<?, ?> map : list) {
			T object = toObject(map, klass);
			objects.add(object);
		}
		return objects;
	}
}
