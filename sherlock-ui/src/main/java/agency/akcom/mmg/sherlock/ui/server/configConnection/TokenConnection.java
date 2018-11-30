package agency.akcom.mmg.sherlock.ui.server.configConnection;

import com.googlecode.objectify.annotation.Subclass;

import javax.inject.Inject;

@Subclass(index=false)
public class TokenConnection extends ConfigConnection {
    String token;

    @Inject
    public TokenConnection() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenConnection(String name, String token) {
        this.name = name;
        this.token = token;
    }
}
