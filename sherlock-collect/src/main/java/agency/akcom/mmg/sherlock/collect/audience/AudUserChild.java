package agency.akcom.mmg.sherlock.collect.audience;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import agency.akcom.mmg.sherlock.collect.domain.AudUser;

@Entity
public abstract class AudUserChild {
	String uid;
	Date hitDate;
	@Id Long id;
	Key<AudUser> key;
	
	public void setParentUid(String uid) {
		this.uid = uid;
	}
	
	public void setHitTime(Date date) {
		this.hitDate = date;
	}
	
	public void setKey(Key<AudUser> key) {
		this.key = key;
	}
	
	public Key getKey(){
		return key;
	}
}
