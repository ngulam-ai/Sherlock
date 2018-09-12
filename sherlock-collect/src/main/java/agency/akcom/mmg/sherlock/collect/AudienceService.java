package agency.akcom.mmg.sherlock.collect;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import agency.akcom.mmg.sherlock.collect.dao.AudUserDao;
import agency.akcom.mmg.sherlock.collect.dao.BackupAudUserDao;
import agency.akcom.mmg.sherlock.collect.domain.AudUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AudienceService {
	public static final Map<String, Map> AUDUSER_FIELD_NAMES = getField(AudUser.class);

	public static void processUIds(JSONObject reqJson) throws Exception { //TODO to write normal throws

		AudUserDao dao = new AudUserDao();
		BackupAudUserDao buckupDao = new BackupAudUserDao();
		AudUser tmpUser = new AudUser(reqJson);
		Set<AudUser> users = dao.setAllMatched(tmpUser);
		String uid = null;
		boolean mustSaveAudUser = true;

		if (users.isEmpty()) {
			log.info("No matched user records found - create new user record");
			// Just store new record
			tmpUser = dao.saveAndReturn(tmpUser);

		} else {
			uid = getLatestUid(users);
			tmpUser.setUid(uid);

			Iterator<AudUser> iterator = users.iterator();
			while (iterator.hasNext()) {
				AudUser comparedUser = iterator.next();
				comparedUser.setUid(uid);

				AudUser backupUser = getCopy(comparedUser);

				boolean conflictID = checkConflictID(tmpUser, comparedUser);

				if (conflictID) {
					if (mustSaveAudUser) {
						dao.save(tmpUser);
					}
					tmpUser = comparedUser;
				} else {
					// Merged and updated field fresh data
					Date tmpUserDate = getLatestTime(tmpUser);
					Date comparedUserDate = getLatestTime(comparedUser);
					log.info("Mergering AudUsers with uid : " + uid);
					if (tmpUserDate.after(comparedUserDate)) {
						if (tmpUser.getId() != null) {
							dao.delete(tmpUser);
						}
						mustSaveAudUser = mergeAudUsersFields(tmpUser, comparedUser, AUDUSER_FIELD_NAMES);
						tmpUser = comparedUser;
					} else {
						mustSaveAudUser = mergeAudUsersFields(comparedUser, tmpUser, AUDUSER_FIELD_NAMES);
						dao.delete(comparedUser);
					}

					// To save if field has different value
					// Or to delete previously version AudUser after merge
					if (mustSaveAudUser) {
						buckupDao.saveBackup(backupUser, tmpUser);
					}

					mustSaveAudUser = false;
				}

				dao.save(tmpUser);

			}
		}

		// replace uid in any case before sending to BQ
		reqJson.put("uid", tmpUser.getUid());

	}

	private static Date getLatestTime(AudUser audUser) {
		Date d = audUser.getLatestHitTime();
		if (d == null) {
			d = audUser.getDoModified();
		}
		return d;
	}

	private static AudUser getCopy(AudUser comparedUser) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream ous = new ObjectOutputStream(baos);
		ous.writeObject(comparedUser);
		ous.close();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (AudUser) ois.readObject();
	}

	public static boolean checkConflictID(AudUser tmpUser, AudUser checkingUser) {
		List<String> fieldNameIDList = AudUserDao.getListFieldNameID(tmpUser);
		for (int i = 0; i < fieldNameIDList.size(); i++) {
			String fieldNameID = fieldNameIDList.get(i);
			String tmpUserValue = (String) AudUserDao.getFieldValue(tmpUser, fieldNameID);
			String checkingUserValue = (String) AudUserDao.getFieldValue(checkingUser, fieldNameID);
			if (checkingUserValue != null && tmpUserValue != null) {
				if (!tmpUserValue.equalsIgnoreCase(checkingUserValue)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * merges fields from fromFields to toFields
	 * 
	 * @return true if must save backup AudUser to history (difference fields)
	 */
	public static boolean mergeAudUsersFields(Object fromFields, Object toFields, Map<String, Map> fieldsMap) {
		boolean saveBackUpAudUser = false;
		for (String fieldName : fieldsMap.keySet()) {

			Object fromFieldsValue = null;
			try {
				fromFieldsValue = AudUserDao.getFieldValue(fromFields, fieldName);
			} catch (NullPointerException e) {
				continue;
			}

			Object toFieldsValue = AudUserDao.getFieldValue(toFields, fieldName);

			if (fieldName.equalsIgnoreCase("frequency")) {
				toFieldsValue = (long) fromFieldsValue + (long) toFieldsValue;
				setFieldValue(toFields, toFieldsValue, fieldName);
				continue;
			}

			boolean differentField = false;
			if (fieldsMap.get(fieldName) == null) {
				if (fromFieldsValue != null) {
					if (toFieldsValue != null) {
						differentField = !fromFieldsValue.toString().equalsIgnoreCase(toFieldsValue.toString());
					}
					setFieldValue(toFields, fromFieldsValue, fieldName);
				}
			} else {
				try {
					differentField = mergeAudUsersFields(fromFieldsValue, toFieldsValue, fieldsMap.get(fieldName));
				} catch (NullPointerException e) {
					setFieldValue(toFields, fromFieldsValue, fieldName);
				}
			}
			if (differentField) {
				saveBackUpAudUser = true;
			}
		}
		return saveBackUpAudUser;
	}

	public static void setFieldValue(Object obj, Object value, String fieldName) {
		Field declaredField = null;
		try {
			declaredField = obj.getClass().getDeclaredField(fieldName);
			declaredField.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			declaredField.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Map<String, Map> getField(Class<?> clazz) {

		Map<String, Map> fieldNamesMap = new HashMap<String, Map>();

		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {

			String fieldName = f.getName();
			if (fieldName.equalsIgnoreCase("log") || fieldName.equalsIgnoreCase("uid") || fieldName.equalsIgnoreCase("latestHitTime")) {
				continue;
			}

			Map<String, Map> map = null;

			for (Class<?> c : clazz.getDeclaredClasses()) {
				String className = c.getSimpleName();
				if (fieldName.equalsIgnoreCase(className)) {
					map = getField(c);
				}
			}

			if (map == null) {
				fieldNamesMap.put(fieldName, null);
			} else {
				fieldNamesMap.put(fieldName, map);
			}
		}
		return fieldNamesMap;
	}

	/**
	 * return latest uid by Date from Set users
	 */
	private static String getLatestUid(Set<AudUser> users) {
		Iterator<AudUser> iterator = users.iterator();
		AudUser user = iterator.next();
		String uid = user.getUid();
		Date date = user.getDoCreated();
		while (iterator.hasNext()) {
			user = iterator.next();
			if (date.after(user.getDoCreated())) {
				date = user.getDoCreated();
				uid = user.getUid();
			}
		}
		return uid;
	}

}
