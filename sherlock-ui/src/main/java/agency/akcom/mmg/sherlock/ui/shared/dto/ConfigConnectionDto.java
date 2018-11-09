package agency.akcom.mmg.sherlock.ui.shared.dto;

import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;

public class ConfigConnectionDto extends Dto{
	TypeConnection typeConnection;

	public TypeConnection getTypeConnection() {
		return typeConnection;
	}

	public void setTypeConnection(TypeConnection typeConnection) {
		this.typeConnection = typeConnection;
	}

	public ConfigConnectionDto() {
	}
}
