package agency.akcom.mmg.sherlock.ui.server.configConnection;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Subclass;

@Subclass
public abstract class ConfigConnection {
    @Id
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
