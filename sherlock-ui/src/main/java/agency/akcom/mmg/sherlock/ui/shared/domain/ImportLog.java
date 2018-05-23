package agency.akcom.mmg.sherlock.ui.shared.domain;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;

import agency.akcom.mmg.sherlock.ui.shared.enums.ImportStatus;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;

@Entity
public class ImportLog extends DatastoreObject {

	private static final long serialVersionUID = 1L;
	
	private Partner partner;
	private Date start = new Date();
	private Date end;
	private ImportStatus status = ImportStatus.FAILURE; // if not set as success later

	public ImportLog() {

	}

	public ImportLog(Partner partner) {
		this.partner = partner;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public ImportStatus getStatus() {
		return status;
	}

	public void setStatus(ImportStatus status) {
		this.status = status;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}


}
