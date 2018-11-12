package agency.akcom.mmg.sherlock.ui.shared.dto;

import java.util.ArrayList;

import agency.akcom.mmg.sherlock.ui.server.configConnection.AvazuConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;

public class DspDto extends Dto {
	
	private Partner partner;
	private String name;
	private ArrayList<ConfigConnectionDto> configConnectionDtos;
	
	public void setAttributes(Long id, Partner partner,String name,ArrayList<ConfigConnectionDto> configConnectionDtos) {
	    this.setId(id);
		this.partner = partner;
		this.name = name;
		this.configConnectionDtos = configConnectionDtos;
	}

	public void setAttributes(Partner partner, String name, TypeConnection typeConnection) {
		switch (typeConnection) {
			case SECRET_ID : configConnectionDtos.add(new AvazuConnectionDto());
				break;
		}
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
}
