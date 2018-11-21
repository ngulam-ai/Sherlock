package agency.akcom.mmg.sherlock.ui.server.configConnection;

import com.googlecode.objectify.annotation.Subclass;

import javax.inject.Inject;

@Subclass(index=false)
public class SecretIdConnection extends ConfigConnection{
	private String clientId;
	private String clientSecret;
	private String grantType; 
	
	public SecretIdConnection(String name, String id, String secret, String grantType) {
		this.name = name;
		clientId = id;
		clientSecret=secret;
		this.grantType = grantType;
	}
	
	public SecretIdConnection(String id, String secret) {
		clientId = id;
		clientSecret=secret;
		grantType = "client_credentials"; // default value: client_credentials;
	}

	@Inject
	public SecretIdConnection() {
		grantType = "client_credentials";
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
}
