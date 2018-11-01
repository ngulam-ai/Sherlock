package agency.akcom.mmg.sherlock.ui.domain;

import java.util.ArrayList;

import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.shared.domain.DatastoreObject;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;

public class Dsp extends DatastoreObject {
	Partner partner;
	String name;
	ArrayList<ConfigConnection> configConnections; 

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