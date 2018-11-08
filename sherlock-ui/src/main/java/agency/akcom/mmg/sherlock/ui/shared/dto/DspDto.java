package agency.akcom.mmg.sherlock.ui.shared.dto;

import java.util.ArrayList;

import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;

public class DspDto extends Dto {
	
	Partner partner;
	String name;
	ArrayList<ConfigConnectionDto> configConnectionDtos;
	
	public void setAttributes(Partner partner,String name,ArrayList<ConfigConnectionDto> configConnectionDtos) {
		this.partner = partner;
		this.name = name;
		this.configConnectionDtos = configConnectionDtos;
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
}
