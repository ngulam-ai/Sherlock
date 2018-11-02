package agency.akcom.mmg.sherlock.ui.server.auth;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpSession;

import agency.akcom.mmg.sherlock.ui.domain.AppUser;
import agency.akcom.mmg.sherlock.ui.server.dao.AppUserDao;

public class CurrentAppUserProvider implements Provider<AppUser>, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String currentUserIdKey = "CUIDK";

	private final Provider<HttpSession> sessionProvider;

	@Inject
	public CurrentAppUserProvider(Provider<HttpSession> sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	@Override
	public AppUser get() {
		HttpSession session = sessionProvider.get();
		Long id = (Long) session.getAttribute(currentUserIdKey);
		AppUser appUser = (id != null) ? (new AppUserDao()).get(id) : null;
		return appUser;
	}

	public void set(AppUser appUser) {
		HttpSession session = sessionProvider.get();
		session.setAttribute(currentUserIdKey, (appUser != null ? appUser.getId() : null));
	}
}
