package agency.akcom.mmg.sherlock.collect.domain;

import java.util.ArrayList;
import java.util.List;

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

	private List<AudUser> backupAudUserList;

	public BackupAudUser(AudUser user) {
		uid = user.getUid();
		backupAudUserList = new ArrayList<>();
		backupAudUserList.add(user);
	}

	public boolean addAudUser(AudUser user) {
		return backupAudUserList.add(user);
	}

}
