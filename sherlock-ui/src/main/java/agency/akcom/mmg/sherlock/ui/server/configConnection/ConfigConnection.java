package agency.akcom.mmg.sherlock.ui.server.configConnection;

import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;

public abstract class ConfigConnection {
	TypeConnection typeConnection;

	public TypeConnection getTypeConnection() {
		return typeConnection;
	}
}
