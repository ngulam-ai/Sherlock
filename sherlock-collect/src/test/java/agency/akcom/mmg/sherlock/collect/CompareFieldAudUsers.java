package agency.akcom.mmg.sherlock.collect;

import java.util.Map;

import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;

import agency.akcom.mmg.sherlock.collect.dao.AudUserDao;
import agency.akcom.mmg.sherlock.collect.domain.AudUser;

public class CompareFieldAudUsers {
	public static boolean compareFields(AudUser user1, Entity user2) {
		return compare(user1, user2.getProperties(), AudienceService.AUDUSER_FIELD_NAMES);
	}
	
	public static boolean compareBackUpFields(AudUser user1, Object object) {
		EmbeddedEntity ent = (EmbeddedEntity) object;
		return compare(user1, ent.getProperties(), AudienceService.AUDUSER_FIELD_NAMES) ;
	}
	
	public static boolean compare(Object trueFields, Map<String, Object> entityFieldsMap, Map<String, Map> fieldsMap) {
		boolean equalsField = true;
		for (String fieldName : fieldsMap.keySet()) {

			Object fromFieldsValue = AudUserDao.getFieldValue(trueFields, fieldName);
			Object entityFieldsValue = entityFieldsMap.get(fieldName);
			
			if (fieldsMap.get(fieldName) == null) {
				String val1 = String.valueOf(fromFieldsValue);
				String val2 = String.valueOf(entityFieldsValue);
				
				if(fromFieldsValue != null && entityFieldsValue != null) {
					equalsField = val1.equalsIgnoreCase(val2);
					if(!equalsField) {
						System.out.println("Field name is " + fieldName);
						System.out.println("have to this " + val1);
						System.out.println("entity field " + val2);
					}
				} else if (fromFieldsValue == null && entityFieldsValue == null) {
					continue;
				} else {
					System.out.println("Field name is " + fieldName);
					System.out.println("have to this " + val1);
					System.out.println("entity field " + val2);
					return false;
				}
			} else {
				EmbeddedEntity ent = (EmbeddedEntity) entityFieldsValue;
				equalsField = compare(fromFieldsValue, ent.getProperties(), fieldsMap.get(fieldName));
			}
			if(!equalsField) {
				System.out.println("Class name is " + fieldName);
				return false;
			}
		}
		return equalsField;
	}
	
	public static boolean compareAudUser(Object fromFields, Object toFields, Map<String, Map> fieldsMap) {
		boolean saveBackUpAudUser = false;
		for (String fieldName : fieldsMap.keySet()) {

			Object fromFieldsValue = AudUserDao.getFieldValue(fromFields, fieldName);
			Object toFieldsValue = AudUserDao.getFieldValue(toFields, fieldName);

			if (fieldsMap.get(fieldName) == null) {
				if (fromFieldsValue != null) {
					if (toFieldsValue != null) {
						saveBackUpAudUser = !fromFieldsValue.toString().equalsIgnoreCase(toFieldsValue.toString());
					}
				}
			} else {
				boolean differentField = compareAudUser(fromFieldsValue, toFieldsValue, fieldsMap.get(fieldName));
				if (differentField) {
					saveBackUpAudUser = true;
				}
			}
		}
		return saveBackUpAudUser;
	}
}
