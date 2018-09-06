package agency.akcom.mmg.sherlock.collect.dao;

import static agency.akcom.mmg.sherlock.collect.dao.objectify.OfyService.ofy;

import com.googlecode.objectify.cmd.Query;

import agency.akcom.mmg.sherlock.collect.domain.AudUser;
import agency.akcom.mmg.sherlock.collect.domain.BackupAudUser;

public class BackupAudUserDao extends BaseDao<BackupAudUser> {

	public BackupAudUserDao() {
		super(BackupAudUser.class);
	}

	// TODO to think about the limits
	// https://cloud.google.com/datastore/docs/concepts/limits
	public void saveBackup(AudUser user) {
		Query<BackupAudUser> q = ofy().load().type(BackupAudUser.class);
		q = q.filter("uid", user.getUid());
		if (q.list().size() != 0) {
			BackupAudUser backup = q.list().get(0);
			backup.addAudUser(user);
			this.save(backup);
		} else {
			this.save(new BackupAudUser(user));
		}
	}

}
