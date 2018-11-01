package agency.akcom.mmg.sherlock.ui.server.configConnection;

public class AvazuConnection extends ConfigConnection{
	private TypeConnection type_connection;
	private String client_id;
	private String client_secret;
	private String grant_type; 
	
	public void AvazuConnection(String id,String secret,String grant_type) {
		client_id = id;
		client_secret=secret;
		this.grant_type = grant_type;
		type_connection = TypeConnection.SECRET_ID;
	}
	
	public void AvazuConnection(String id,String secret) {
		client_id = id;
		client_secret=secret;
		grant_type = "client_credentials"; // default value: client_credentials;
		type_connection = TypeConnection.SECRET_ID;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}
	
	
}
