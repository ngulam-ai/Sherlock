package agency.akcom.mmg.sherlock.ui.domain;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

import agency.akcom.mmg.sherlock.ui.shared.domain.DatastoreObject;

/**
 * An application user, named with a prefix to avoid confusion with GAE User type
 */

@Entity
public class AppUser extends DatastoreObject {

	private static final long serialVersionUID = 1L;

	@Index
	private String email;
	@Index
	private String googleId;

	private User goggleUser;

	private boolean isAdmin;

	private String pictureURL;

	@Load
	@Index
	private Ref<Customer> customer;

	public AppUser() {

	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Customer getCustomer() {
		return customer.get();
	}

	public void setCustomer(Customer customer) {
		this.customer = Ref.create(customer);
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User getGoggleUser() {
		return goggleUser;
	}

	public void setGoggleUser(User goggleUser) {
		this.goggleUser = goggleUser;
	}

	public String getPictureURL() {
		return pictureURL != null ? pictureURL : "/images/no-pic.gif";
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}
}
