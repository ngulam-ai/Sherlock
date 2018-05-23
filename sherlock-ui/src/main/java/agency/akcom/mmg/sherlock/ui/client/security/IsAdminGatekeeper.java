package agency.akcom.mmg.sherlock.ui.client.security;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class IsAdminGatekeeper extends IsLoggedInGatekeeper {

	@Inject
	public IsAdminGatekeeper(EventBus eventBus) {
		super(eventBus);
	}

	@Override
	public boolean canReveal() {
		return super.canReveal() && getCurrentUser().isAdmin();
	}

}
