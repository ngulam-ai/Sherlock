package agency.akcom.mmg.sherlock.ui.shared.dto;

public abstract class ConfigConnectionDto extends Dto{
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConfigConnectionDto() {
	}
}
