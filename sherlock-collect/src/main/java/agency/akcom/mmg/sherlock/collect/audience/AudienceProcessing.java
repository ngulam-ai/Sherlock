package agency.akcom.mmg.sherlock.collect.audience;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import agency.akcom.mmg.sherlock.collect.domain.JsonField;

public class AudienceProcessing {
		
	// Set fields in class with "JsonField" annotation
	public static void setFieldsWithAnnotation(Object clazz, JSONObject reqJson) {
		Field[] fieldsArr = clazz.getClass().getDeclaredFields();
		for (int i = 0; i < fieldsArr.length; i++) {
			boolean annotation = checkExistenceAnnotation(fieldsArr[i]);
			if (annotation) {
				String[] jsonFieldName = fieldsArr[i].getAnnotation(JsonField.class).name().split(",");
				String jsonValue = getJsonValue(jsonFieldName, reqJson);
				fieldsArr[i].setAccessible(true);
				try {
					fieldsArr[i].set(clazz, jsonValue);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	// Checking JsonField annotation is exist in field
	public static boolean checkExistenceAnnotation(Field f) {
		Annotation[] annotations = f.getDeclaredAnnotations();
		for (int i = 0; i < annotations.length; i++) {
			if (annotations[i] instanceof JsonField) {
				return true;
			}
		}
		return false;
	}
	
	public static String getJsonValue(String[] jsonFieldName, JSONObject reqJson) {
		String value = null;
		for (int i = 0; i < jsonFieldName.length; i++) {
			try {
				value = reqJson.getString(jsonFieldName[i].trim());
				if (!value.isEmpty()) {
					return value;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return value;
	}
	
	public static Date getHitTime(JSONObject reqJson) {
		Date date = new Date();
		date.setTime(reqJson.getLong("time"));
		return date;
	}
}
