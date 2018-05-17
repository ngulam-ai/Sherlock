package agency.akcom.mmg.sherlock.ui.client.security;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

import agency.akcom.mmg.sherlock.ui.client.event.LoginAuthenticatedEvent;
import agency.akcom.mmg.sherlock.ui.client.event.LoginResetEvent;
import agency.akcom.mmg.sherlock.ui.shared.UserDto;

public class IsLoggedInGatekeeper implements Gatekeeper {
	private final EventBus eventBus;
	private UserDto userDto;

	@Inject
	public IsLoggedInGatekeeper(final EventBus eventBus) {
		this.eventBus = eventBus;
		this.eventBus.addHandler(LoginAuthenticatedEvent.getType(),
				new LoginAuthenticatedEvent.LoginAuthenticatedHandler() {
					@Override
					public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
						userDto = event.getCurrentUser();
					}
				});
		this.eventBus.addHandler(LoginResetEvent.getType(), new LoginResetEvent.LoginResetHandler() {
			@Override
			public void onLoginReset(LoginResetEvent event) {
				userDto = null;
			}
		});
	}

	@Override
	public boolean canReveal() {
		return (userDto != null) && userDto.isLoggedIn();
	}

	public UserDto getCurrentUser() {
		return userDto;
	}
}