package agency.akcom.mmg.sherlock.collect;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import agency.akcom.mmg.sherlock.collect.dao.AudUserDao;
import agency.akcom.mmg.sherlock.collect.domain.AudUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AudienceService {

	public static void processUIds(JSONObject reqJson) {
		// TODO Just create a new record, later will add the real logic of processing.

		AudUserDao dao = new AudUserDao();
		AudUser tmpUser = new AudUser(reqJson);
		Set<AudUser> users = dao.setAllMatched(tmpUser);
		String uid = null;
		boolean needToSaveTmpUser = true;

		if (users.isEmpty()) {
			log.info("No matched user records found - create new user record");
			// Just store new record
			tmpUser = dao.saveAndReturn(tmpUser);

		} else {
			uid = getLatestUid(users);

			Iterator<AudUser> iterator = users.iterator();
			while (iterator.hasNext()) {
				AudUser comparedUser = iterator.next();
				comparedUser.setUid(uid);
				tmpUser.setUid(uid);

				boolean conflictID = checkConflictID(tmpUser, comparedUser);

				if (conflictID) {
					if (needToSaveTmpUser) {
						tmpUser.increaseFrequency();
						dao.save(tmpUser);
					}
					tmpUser = comparedUser;
				} else {
					if (!needToSaveTmpUser) {
						dao.delete(tmpUser);
					}

					tmpUser = mergeAudUsersID(tmpUser, comparedUser);

					// TODO merge another fields + increaseFrequency + update fields fresh data?
					// TODO TO WRITE TESTS!!!

					needToSaveTmpUser = false;
				}

				dao.save(tmpUser);

			}

		}

		// replace uid in any case before sending to BQ
		reqJson.put("uid", tmpUser.getUid());

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
	 * merges IDs fields from tmpUser to mergedUser
	 * 
	 * @return merged AudUser
	 */
	public static AudUser mergeAudUsersID(AudUser tmpUser, AudUser mergedUser) {
		List<String> fieldNameIDList = AudUserDao.getListFieldNameID(tmpUser);
		for (int i = 0; i < fieldNameIDList.size(); i++) {
			String fieldNameID = fieldNameIDList.get(i);
			String tmpUserValue = (String) AudUserDao.getFieldValue(tmpUser, fieldNameID);
			String checkingUserValue = (String) AudUserDao.getFieldValue(mergedUser, fieldNameID);
			if (checkingUserValue == null && tmpUserValue != null) {
				Field declaredField = null;
				try {
					declaredField = mergedUser.getClass().getDeclaredField(fieldNameIDList.get(i));
					declaredField.setAccessible(true);
				} catch (NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					declaredField.set(mergedUser, tmpUserValue);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return mergedUser;
	}

	/**
	 * return latest by Date uid from Set users
	 */
	// TODO to think about get ordered set by date from DataStore
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
