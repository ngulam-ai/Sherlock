package agency.akcom.mmg.sherlock.ui.shared.dto;

import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;

import java.util.ArrayList;

public class DspDto extends Dto {
	
	private Partner partner;
	private String name;
	private TypeConnection typeConnection;
	private ArrayList<ConfigConnectionDto> configConnectionDtos;
	
	public void setAttributes(Long id, Partner partner,String name, TypeConnection typeConnection, ArrayList<ConfigConnectionDto> configConnectionDtos) {
	    this.setId(id);
		this.partner = partner;
		this.name = name;
		this.typeConnection = typeConnection;
		this.configConnectionDtos = configConnectionDtos;
	}

	public void setAttributes(Partner partner, String name, TypeConnection typeConnection) {
		this.typeConnection = typeConnection;
		this.partner = partner;
		this.name = name;
	}


	public Partner getPartner() {
		return partner;
	}

	public String getName() {
		return name;
	}

    public ArrayList<ConfigConnectionDto> getConfigConnectionDtos() {
        return configConnectionDtos;
    }

    public void setConfigConnectionDtos(ArrayList<ConfigConnectionDto> configConnectionDtos) {
        this.configConnectionDtos = configConnectionDtos;
    }

    public void copyFrom(DspDto dspToCopy) {
		partner = dspToCopy.getPartner();
		name = dspToCopy.getName();
		configConnectionDtos = dspToCopy.getConfigConnectionDtos();
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TypeConnection getTypeConnection() {
		return typeConnection;
	}

	public void setTypeConnection(TypeConnection typeConnection) {
		this.typeConnection = typeConnection;
	}
}
