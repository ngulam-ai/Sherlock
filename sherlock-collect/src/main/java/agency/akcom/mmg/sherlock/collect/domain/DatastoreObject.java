package agency.akcom.mmg.sherlock.collect.domain;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.OnSave;

import lombok.Data;

@Data
public class DatastoreObject implements IsSerializable, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private Integer version = 0;

	private Date doCreated = new Date();

	private Date doModified;

	/**
	 * Auto-increment version # whenever persisted
	 */
	@OnSave
	void onSave() {
		this.version++;
		this.doModified = new Date();
	}

	public boolean isSaved() {
		return (id != null);
	}

}
