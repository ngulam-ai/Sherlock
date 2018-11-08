package agency.akcom.mmg.sherlock.ui.server.configConnection;

public class AvazuConnection extends ConfigConnection{
	private TypeConnection typeConnection;
	private String clientId;
	private String clientSecret;
	private String grantType; 
	
	public AvazuConnection(String id, String secret, String grantType) {
		clientId = id;
		clientSecret=secret;
		this.grantType = grantType;
		typeConnection = TypeConnection.SECRET_ID;
	}
	
	public AvazuConnection(String id, String secret) {
		clientId = id;
		clientSecret=secret;
		grantType = "client_credentials"; // default value: client_credentials;
		typeConnection = TypeConnection.SECRET_ID;
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
