package agency.akcom.mmg.sherlock.ui.shared.dto;

public class AvazuConnectionDto extends ConfigConnectionDto {

	private String clientId;
	private String clientSecret;
	private String grantType;


	public AvazuConnectionDto(String clientId, String clientSecret) {

		this.clientId = clientId;
		this.clientSecret = clientSecret;
		grantType = "client_credentials"; // default value: client_credentials;
	}

	public AvazuConnectionDto(String clientId, String clientSecret, String grantType) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.grantType = grantType;
	}

	public AvazuConnectionDto() {
		grantType = "client_credentials"; // default value: client_credentials;
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

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
