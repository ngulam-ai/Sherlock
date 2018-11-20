package agency.akcom.mmg.sherlock.collect;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

import agency.akcom.mmg.sherlock.collect.audience.AudUserChild;
import agency.akcom.mmg.sherlock.collect.dao.AudUserChildDao;
import agency.akcom.mmg.sherlock.collect.dao.AudUserDao;
import agency.akcom.mmg.sherlock.collect.domain.AudUser;
import agency.akcom.mmg.sherlock.collect.domain.IDField;
import agency.akcom.mmg.sherlock.collect.domain.Scope;

public class CompareFieldAudUsers {
	static List<String> fieldNamesList = AudienceService.getFieldNames(AudUser.class);
	
	public static boolean compareFields(AudUser user1, AudUser user2) {
		List<String> ignoreFields = AudUserDao.getFieldNamesSpace(AudUser.class, Scope.class);
		List<String> ignoreList = Arrays.asList("log", "latestHitTime", "doModified");
		ignoreFields.addAll(ignoreList);
		fieldNamesList.removeAll(ignoreFields);

		boolean equalsAudUserFields = compare(user1, user2, fieldNamesList);
		if(!equalsAudUserFields) {
			return false;
		}
		List<String> scopeNames = AudUserDao.getFieldNamesSpace(AudUser.class, Scope.class);
		for(String name : scopeNames) {
			Ref refUser1 = AudUserDao.getRef(user1, name);
			AudUserChild childUser1 = (AudUserChild) refUser1.get();
			
			Ref refUser2 = AudUserDao.getRef(user2, name);
			AudUserChild childUser2 = (AudUserChild) refUser2.get();
			
			List<String> fieldNamesList = AudienceService.getFieldNames(childUser1.getClass());
			fieldNamesList.removeAll(ignoreList);
			
			boolean equals = compare(childUser1, childUser2, fieldNamesList);
			if(!equals) {
				return false;
			}
		}
		return true;
	}
	
//	public static boolean compareBackUpFields(AudUser user1, Object object) {
//		EmbeddedEntity ent = (EmbeddedEntity) object;
//		Map<String, Map> fieldsMap = AudienceService.getField(AudienceService.names.get(0));
//		return compare(user1, ent.getProperties(), fieldsMap);
//	}
	
	public static boolean compare(Object user1, Object user2, List<String> namesSpace) {
		boolean equalsField = true;
		for(String fieldName : namesSpace) {

			Object fromFieldsValue = AudUserDao.getFieldValue(user1, fieldName);
			Object entityFieldsValue = AudUserDao.getFieldValue(user2, fieldName);
			
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
