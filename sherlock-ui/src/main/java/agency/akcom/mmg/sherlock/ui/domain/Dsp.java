package agency.akcom.mmg.sherlock.ui.domain;

import java.util.ArrayList;
import com.googlecode.objectify.annotation.Entity;
import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.shared.domain.DatastoreObject;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;

@Entity
public class Dsp extends DatastoreObject {
	
	private static final long serialVersionUID = 1L;
	private Partner partner;
	private String name;
	private ArrayList<ConfigConnection> configConnections;

	public Dsp() {}

	public Dsp(Partner partner, String name) {
		this.partner = partner;
		this.name = name;
	}

	public ArrayList<ConfigConnection> getConfigConnections() {
		return configConnections;
	}

	public void setConfigConnections(ArrayList<ConfigConnection> configConnections) {
		this.configConnections = configConnections;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}
}