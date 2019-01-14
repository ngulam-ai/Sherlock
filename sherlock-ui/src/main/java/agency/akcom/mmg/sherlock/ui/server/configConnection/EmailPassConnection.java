package agency.akcom.mmg.sherlock.ui.server.configConnection;

import com.googlecode.objectify.annotation.Subclass;

import javax.inject.Inject;

@Subclass(index=false)
public class EmailPassConnection extends ConfigConnection{
	private String email;
	private String password;

	public EmailPassConnection(String name, String id, String secret) {
		this.name = name;
		email = id;
		password =secret;
	}

	public EmailPassConnection(String id, String secret) {
		email = id;
		password =secret;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
