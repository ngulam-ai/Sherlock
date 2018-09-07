package agency.akcom.mmg.sherlock.collect.dao;

import agency.akcom.mmg.sherlock.collect.domain.AudUser;
import agency.akcom.mmg.sherlock.collect.domain.BackupAudUser;

public class BackupAudUserDao extends BaseDao<BackupAudUser> {

	public BackupAudUserDao() {
		super(BackupAudUser.class);
	}

	public void saveBackup(AudUser backupAudUser, AudUser replacedAudUser) {
		this.save(new BackupAudUser(backupAudUser, replacedAudUser));
	}

}
