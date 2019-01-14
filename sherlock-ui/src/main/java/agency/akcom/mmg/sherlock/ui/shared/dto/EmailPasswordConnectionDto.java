package agency.akcom.mmg.sherlock.ui.shared.dto;

public class EmailPasswordConnectionDto extends ConfigConnectionDto {

	private String email;
	private String password;

	public EmailPasswordConnectionDto(String email, String password) {

		this.email = email;
		this.password = password;
	}

	public EmailPasswordConnectionDto(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public EmailPasswordConnectionDto() {
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EmailPasswordConnectionDto that = (EmailPasswordConnectionDto) o;

		if (password != null ? !password.equals(that.password) : that.password != null) return false;
		return email != null ? email.equals(that.email) : that.email == null;
	}

	@Override
	public int hashCode() {
		int result = email != null ? email.hashCode() : 0;
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}
}
