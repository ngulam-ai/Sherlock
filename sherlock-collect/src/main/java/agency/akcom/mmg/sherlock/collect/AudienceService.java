package agency.akcom.mmg.sherlock.collect;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

import agency.akcom.mmg.sherlock.collect.audience.AudUserChild;
import agency.akcom.mmg.sherlock.collect.audience.AudienceProcessing;
import agency.akcom.mmg.sherlock.collect.audience.Demography;
import agency.akcom.mmg.sherlock.collect.audience.Geography;
import agency.akcom.mmg.sherlock.collect.dao.AudUserChildDao;
import agency.akcom.mmg.sherlock.collect.dao.AudUserDao;
import agency.akcom.mmg.sherlock.collect.dao.BackupAudUserDao;
import agency.akcom.mmg.sherlock.collect.domain.AudUser;
import agency.akcom.mmg.sherlock.collect.domain.IDField;
import agency.akcom.mmg.sherlock.collect.domain.JsonField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AudienceService {
	public static final List<String> AUDUSER_UID_FIELD_NAMES = AudUserDao.getFieldNamesSpace(AudUser.class, IDField.class);
	public static final List<Class<? extends AudUserChild>> names = Arrays.asList(Geography.class, Demography.class);
	
	public static void processUIds(JSONObject reqJson) throws Exception { //TODO to write normal throws
		//1. Create AudUser with UIDs
		//2. load AudUser by UIDs
		//2.1 compare UIDs
		//2.1.1 No conflicts UIDs -> sign same UID and update scope (save old UID for history)
		//2.1.2 Conflict UID:
		//In loop: every AudUser:
		//2.1.2.1 Try find next where no conflict -> 2.1.1 and if we merging found AudUser to this, need delete found from loop for next iter! (next search in array)
		//2.1.2.2 Just save as is -> continue;
		
		AudUserDao dao = new AudUserDao();
		AudUser tmpUser = new AudUser(reqJson);
		Set<AudUser> users = dao.setAllMatched(tmpUser);
		String uid = null;
		boolean mustSaveAudUser = true;

		if (users.isEmpty()) {
			log.info("No matched user records found - create new user record. uid: " + tmpUser.getUid());
			// Just store new record
			tmpUser.setDoModified(new Date());
			tmpUser = dao.saveAndReturn(tmpUser);

		} else {
			uid = getLatestUid(users);
			tmpUser.setUid(uid);

			Iterator<AudUser> iterator = users.iterator();
			while (iterator.hasNext()) {
				AudUser comparedUser = iterator.next();

				boolean conflictID = checkConflictID(tmpUser, comparedUser);

				if (conflictID) {
					if (mustSaveAudUser) {
						dao.save(tmpUser);
					}
					tmpUser = comparedUser;
					continue;
				}

				mustSaveAudUser = false;
				comparedUser.setLast_uid(comparedUser.getUid());
				comparedUser.setUid(uid);

				Date tmpUserDate = getLatestTime(tmpUser);
				Date comparedUserDate = getLatestTime(comparedUser);
				log.info("Mergering AudUsers with uid : " + uid);
				if (tmpUserDate.after(comparedUserDate)) {
					if (tmpUser.getId() != null) {
						dao.delete(tmpUser);
					}
					mergeUID(tmpUser, comparedUser);
					mergeScope(tmpUser, comparedUser);
					comparedUser.setFrequency(comparedUser.getFrequency() + tmpUser.getFrequency());
					tmpUser = comparedUser;
				} else {
					mergeUID(comparedUser, tmpUser);
					mergeScope(comparedUser, tmpUser);
					tmpUser.setFrequency(comparedUser.getFrequency() + tmpUser.getFrequency());
					dao.delete(comparedUser);
				}
				dao.save(tmpUser);
			}
		}
		
		// replace uid in any case before sending to BQ
		reqJson.put("uid", tmpUser.getUid());
	}
	
	// Creating children, saving they and setting child ref to tmpUser
	public static void createScope(AudUser tmpUser, JSONObject reqJson) throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Date hitTime = AudienceProcessing.getHitTime(reqJson);

		for (Class<? extends AudUserChild> child : AudienceService.names) {
			Class<? extends AudUserChild> clazz = child;
			Constructor<? extends AudUserChild> constructor = clazz.getConstructor(JSONObject.class);

			AudUserChild instance = constructor.newInstance(reqJson);
			instance.setParentUid(tmpUser.getUid());
			instance.setHitTime(hitTime);

			AudUserChildDao childDao = new AudUserChildDao();
			Key<AudUserChild> key = childDao.saveAndReturnKey(instance);

			String nameField = instance.getClass().getSimpleName().toLowerCase();
			// setting ref in instance
			AudienceService.setFieldValue(tmpUser, Ref.create(key), nameField);
		}
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
		for (String fieldName : AUDUSER_UID_FIELD_NAMES) {
			if (fieldName.equals("uid")) {
				continue;
			}
			String tmpUserValue = (String) AudUserDao.getFieldValue(tmpUser, fieldName);
			String checkingUserValue = (String) AudUserDao.getFieldValue(checkingUser, fieldName);
			if (checkingUserValue != null && tmpUserValue != null) {
				if (!tmpUserValue.equalsIgnoreCase(checkingUserValue)) {
					log.warn("conflict field:" + fieldName + " 1:" + tmpUserValue + " 2:" + checkingUserValue);
					return true;
				}
			}
		}
		return false;
	}

	public static void mergeScope(AudUser from, AudUser to) throws NoSuchFieldException, SecurityException {
		for (Class<? extends AudUserChild> scope : names) {
			Ref refFrom = AudUserDao.getRef(from, scope.getSimpleName().toLowerCase());
			AudUserChild childFrom = (AudUserChild) refFrom.get();

			Ref refTo = AudUserDao.getRef(to, scope.getSimpleName().toLowerCase());
			AudUserChild childTo = (AudUserChild) refTo.get();

			List<String> fieldNames = AudUserDao.getFieldNamesSpace(scope, JsonField.class); // TODO review for more
																								// speed;

			for (String fieldName : fieldNames) {
				Object fromFieldsValue = AudUserDao.getFieldValue(childFrom, fieldName);
				if (fromFieldsValue != null) {
					setFieldValue(childTo, fromFieldsValue, fieldName);
				}
			}

			AudUserChildDao childDao = new AudUserChildDao();
			Key<AudUserChild> key = childDao.saveAndReturnKey(childTo);

			String nameField = childTo.getClass().getSimpleName().toLowerCase();
			// setting ref in instance
			setFieldValue(to, Ref.create(key), nameField);
		}
	}

	public static void mergeUID(AudUser from, AudUser to) throws NoSuchFieldException, SecurityException {
		for (String fieldName : AUDUSER_UID_FIELD_NAMES) {
			if (fieldName.equals("uid")) {
				continue;
			}
			Object fromFieldsValue = AudUserDao.getFieldValue(from, fieldName);
			if (fromFieldsValue != null) {
				setFieldValue(to, fromFieldsValue, fieldName);
			}
		}
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
			if (fieldName.equalsIgnoreCase("log") || fieldName.equalsIgnoreCase("uid")
					|| fieldName.equalsIgnoreCase("latestHitTime") || fieldName.equalsIgnoreCase("uid_adserver")
					|| fieldName.equalsIgnoreCase("doModified")) {
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

	public static List<String> getFieldNames(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<String> names = new ArrayList(fields.length);

		for (Field field : fields) {
			names.add(field.getName());
		}
		return names;
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
