package agency.akcom.mmg.sherlock.ui.shared.dto;

import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnectionDto;

public class ConfigConnectionDto extends Dto{
	TypeConnectionDto typeConnectionDto;

	public TypeConnectionDto getTypeConnectionDto() {
		return typeConnectionDto;
	}

	public void setTypeConnectionDto(TypeConnectionDto typeConnectionDto) {
		this.typeConnectionDto = typeConnectionDto;
	}

	public ConfigConnectionDto() {
	}
}
