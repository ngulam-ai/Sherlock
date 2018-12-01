package agency.akcom.mmg.sherlock.ui.shared.dto;

public class SecretIdConnectionDto extends ConfigConnectionDto {

	private String clientId;
	private String clientSecret;
	private String grantType;


	public SecretIdConnectionDto(String clientId, String clientSecret) {

		this.clientId = clientId;
		this.clientSecret = clientSecret;
		grantType = "client_credentials"; // default value: client_credentials;
	}

	public SecretIdConnectionDto(String name, String clientId, String clientSecret, String grantType) {
		this.name = name;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.grantType = grantType;
	}

	public SecretIdConnectionDto() {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SecretIdConnectionDto that = (SecretIdConnectionDto) o;

		if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) return false;
		if (clientSecret != null ? !clientSecret.equals(that.clientSecret) : that.clientSecret != null) return false;
		return grantType != null ? grantType.equals(that.grantType) : that.grantType == null;
	}

	@Override
	public int hashCode() {
		int result = clientId != null ? clientId.hashCode() : 0;
		result = 31 * result + (clientSecret != null ? clientSecret.hashCode() : 0);
		result = 31 * result + (grantType != null ? grantType.hashCode() : 0);
		return result;
	}
}
