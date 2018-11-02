package agency.akcom.mmg.sherlock.ui.shared.dto;

public class UserDto extends Dto {

	private static final long serialVersionUID = 1L;

	private Boolean isLoggedIn;
	private String login;
	private String pictureURL;
	private boolean isAdmin;

	private String customerName;
	private String customerDescription;

	public UserDto() {
		// not logged in user default values
		isLoggedIn = false;
		isAdmin = false;
	}

	public void setAttributes(Boolean isLoggedIn, Long id, String login, boolean isAdmin, String pictureURL,
			String customerName, String customerDescription) {
		setId(id);
		this.isLoggedIn = isLoggedIn;
		this.login = login;
		this.isAdmin = isAdmin;
		this.pictureURL = pictureURL;
		this.customerName = customerName;
		this.customerDescription = customerDescription;
	}

	public String getLogin() {
		return login;
	}

	public Boolean isLoggedIn() {
		return isLoggedIn;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public String getCustomerDescription() {
		return customerDescription;
	}

	public void copyFrom(UserDto userToCopy) {
		isLoggedIn = userToCopy.isLoggedIn;

		setId(userToCopy.getId());
		login = userToCopy.login;
		pictureURL = userToCopy.pictureURL;
		isAdmin = userToCopy.isAdmin;

		customerName = userToCopy.customerName;
		customerDescription = userToCopy.customerDescription;
	}
}
