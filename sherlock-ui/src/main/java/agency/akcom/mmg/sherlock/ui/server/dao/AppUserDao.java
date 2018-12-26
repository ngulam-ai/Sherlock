package agency.akcom.mmg.sherlock.ui.server.dao;

import agency.akcom.mmg.sherlock.ui.domain.AppUser;
import agency.akcom.mmg.sherlock.ui.shared.TooManyResultsException;

public class AppUserDao extends BaseDao<AppUser> {

	public AppUserDao() {
		super(AppUser.class);
	}

	@Override
	public void delete(Long id) {
		// TODO temporary, until I have only one user per customer
		// user will be deleted along with the Customer
		new CustomerDao().delete(get(id).getCustomer());

		// TODO am I also have to deleteStorage User StoredCredential?
	};

	public AppUser findByGoogleId(String googleId) throws TooManyResultsException {
		return getByProperty("googleId", googleId);
	}

	public AppUser findByEmail(String email) throws TooManyResultsException {
		return getByProperty("email", email);
	}
}
