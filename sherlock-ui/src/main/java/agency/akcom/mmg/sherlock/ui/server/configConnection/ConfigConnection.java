package agency.akcom.mmg.sherlock.ui.server.configConnection;

public abstract class ConfigConnection {
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConfigConnection(String name) {
		this.name = name;
	}

	public ConfigConnection() {
	}
}
