package agency.akcom.mmg.sherlock.collect;

import java.util.List;

import org.json.JSONObject;

import agency.akcom.mmg.sherlock.collect.dao.AudUserDao;
import agency.akcom.mmg.sherlock.collect.domain.AudUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AudienceService {

	public static void processUIds(JSONObject reqJson) {
		// TODO Just create a new record, later will add the real logic of processing.

		// TODO Find all matched user records
		AudUserDao dao = new AudUserDao();
		AudUser tmpUser = new AudUser(reqJson);
		List<AudUser> users = dao.listAllMatched(tmpUser);

		if (users.isEmpty()) {
			log.info("No matched user records found - create new user record");
			// Just store new record
			tmpUser = dao.saveAndReturn(tmpUser);

		} else if (users.size() == 1) {
			log.info("Just one user record found - update if needed (count visits at least)");
			// just count for now
			users.get(0).increaseFrequency();
			tmpUser = dao.saveAndReturn(users.get(0));

			// TODO implement more complex logic
			// UC 1,2,4: 1 match is found - update (or not), query UID, replace it before
			// the insert in BQ
			// UC 5: 2 (or more) matches are found - merge/stitch this two (or more) and
			// assign the one unique UID

		} else {
			log.info("More than one user record found -  merge/stitch them");
			// TODO implement more complex logic
			// UC 1,2,4: 1 match is found - update (or not), query UID, replace it before
			// the insert in BQ
			// UC 5: 2 (or more) matches are found - merge/stitch this two (or more) and
			// assign the one unique UID

		}

		// And finally, replace uid in any case before sending to BQ
		reqJson.put("uid", tmpUser.getUid());

	}

}
