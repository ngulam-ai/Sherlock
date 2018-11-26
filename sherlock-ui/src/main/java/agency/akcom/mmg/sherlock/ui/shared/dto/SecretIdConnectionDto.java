package agency.akcom.mmg.sherlock.ui.shared.dto;

import java.util.Objects;

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
		return Objects.equals(clientId, that.clientId) &&
				Objects.equals(clientSecret, that.clientSecret) &&
				Objects.equals(grantType, that.grantType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(clientId, clientSecret, grantType);
	}
}
