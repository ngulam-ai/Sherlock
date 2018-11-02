package agency.akcom.mmg.sherlock.ui.domain;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

import agency.akcom.mmg.sherlock.ui.shared.domain.DatastoreObject;

@Entity
public class Customer extends DatastoreObject {

	private static final long serialVersionUID = 1L;

	@Index
	private String name;
	private String description;

	// stored as user attributes
	@Ignore
	private String email;
	@Ignore
	private String pictureURL;
	@Ignore
	private Boolean isAdmin;
	// --------------

	@Load
	List<Ref<AppUser>> users = new ArrayList<Ref<AppUser>>();

	public Customer() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Ref<AppUser>> getUsers() {
		return users;
	}

	public void setUsers(List<Ref<AppUser>> users) {
		this.users = users;
	}

	public String getEmail() {
		if (email != null)
			return email;
		else
			return users.get(0).get().getEmail();
	}

	public void setEmail(String email) {
		if (users.size() == 0)
			this.email = email;
		else
			users.get(0).get().setEmail(email);
	}

	public String getPictureURL() {
		if (pictureURL != null)
			return pictureURL;
		else
			return users.get(0).get().getPictureURL();
	}

	public void setPictureURL(String pictureURL) {
		if (users.size() == 0)
			this.pictureURL = pictureURL;
		else
			users.get(0).get().setPictureURL(pictureURL);
	}

	public void setAdmin(Boolean isAdmin) {
		if (users.size() == 0)
			this.isAdmin = isAdmin;
		else
			users.get(0).get().setAdmin(isAdmin);

	}

	public boolean isAdmin() {
		if (isAdmin != null)
			return isAdmin;
		else
			return users.get(0).get().isAdmin();
	}

	public AppUser getUser() {
		if (users.size() == 0)
			users.add(Ref.create(new AppUser()));
		return users.get(0).get();
	}
}
