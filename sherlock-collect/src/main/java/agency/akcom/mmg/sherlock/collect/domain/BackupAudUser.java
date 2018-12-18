package agency.akcom.mmg.sherlock.collect.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Slf4j
public class BackupAudUser extends DatastoreObject {

	@Index
	private String uid;
	
	private AudUser backupAudUser;
	
	private AudUser replacedAudUser;
	
	public BackupAudUser(AudUser backupAudUser, AudUser replacedAudUser) {
		uid = backupAudUser.getUid();
		this.backupAudUser = backupAudUser;
		this.replacedAudUser = replacedAudUser;
	}

}
