package agency.akcom.mmg.sherlock.collect.dao;

import static agency.akcom.mmg.sherlock.collect.dao.objectify.OfyService.ofy;

import java.util.List;

import com.googlecode.objectify.cmd.Query;

import agency.akcom.mmg.sherlock.collect.domain.AudUser;

public class AudUserDao extends BaseDao<AudUser> {

	public AudUserDao() {
		super(AudUser.class);
	}

	public List<AudUser> listAllMatched(AudUser tmpUser) {
		// TODO implement match by all possible UIds one by one

		Query<AudUser> q = ofy().load().type(AudUser.class);

		// only by userId (uid) for now
		q = q.filter("uid", tmpUser.getUid());

		return q.list();

	}

}
