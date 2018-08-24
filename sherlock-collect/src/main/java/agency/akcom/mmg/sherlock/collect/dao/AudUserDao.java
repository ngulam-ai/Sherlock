package agency.akcom.mmg.sherlock.collect.dao;

import static agency.akcom.mmg.sherlock.collect.dao.objectify.OfyService.ofy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.googlecode.objectify.cmd.Query;

import agency.akcom.mmg.sherlock.collect.domain.AudUser;
import agency.akcom.mmg.sherlock.collect.domain.IDField;

public class AudUserDao extends BaseDao<AudUser> {

	public AudUserDao() {
		super(AudUser.class);
	}

	public Set<AudUser> setAllMatched(AudUser tmpUser) {
		// TODO implement match by all possible UIds one by one
		Set<AudUser> userSet = new HashSet();

		// 1: get all available IDs
		List<String> nameIDsFields = getListFieldNameID(tmpUser);

		// 2: Use this for finding in datastore
		//https://stackoverflow.com/questions/5393571/making-or-queries-in-google-app-engine-data-model  about that you can not query with "OR"
		for (int i = 0; i < nameIDsFields.size(); i++) {
			Object FieldValue = getFieldValue(tmpUser, nameIDsFields.get(i));
			if (FieldValue != null) {
				Query<AudUser> q = ofy().load().type(AudUser.class);
				q = q.filter(nameIDsFields.get(i), FieldValue);
				// Get double Users if they have two or more identical TypeID
				userSet.addAll(q.list());
			}
		}
		return userSet;
	}

	/**
	 * @return List names with annotation "IDField"
	 */
	public static List<String> getListFieldNameID(AudUser tmpUser) {
		Field[] fields = tmpUser.getClass().getDeclaredFields();
		List<String> nameIDsFields = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(IDField.class)) {
				nameIDsFields.add(fields[i].getName());
			}
		}
		return nameIDsFields;
	}

	public static Object getFieldValue(AudUser audUser, String nameField) {
		Object value = null;
		Field field = null;
		try {
			field = audUser.getClass().getDeclaredField(nameField);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		field.setAccessible(true);
		try {
			value = field.get(audUser);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
}
