package agency.akcom.mmg.sherlock.ui.server.configConnection;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public abstract class ConfigConnection {
	@Id Long id;
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
