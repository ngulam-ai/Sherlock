package agency.akcom.mmg.sherlock.ui.shared.domain;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.OnSave;

public class DatastoreObject implements Serializable {

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public boolean isSaved() {
		return (id != null);
	}

	public Date getDoCreated() {
		return doCreated;
	}

	public Date getDoModified() {
		return doModified;
	}
}
