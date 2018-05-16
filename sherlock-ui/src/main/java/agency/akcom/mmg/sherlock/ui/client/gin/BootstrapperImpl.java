package agency.akcom.mmg.sherlock.ui.client.gin;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

import agency.akcom.mmg.sherlock.ui.client.dispatch.AsyncCallbackImpl;
import agency.akcom.mmg.sherlock.ui.client.event.LoginAuthenticatedEvent;
import agency.akcom.mmg.sherlock.ui.shared.UserDto;
import agency.akcom.mmg.sherlock.ui.shared.action.GetCurrentUserAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetCurrentUserResult;

public class BootstrapperImpl implements Bootstrapper {
	private final PlaceManager placeManager;
	private final DispatchAsync dispatcher;

	private final UserDto userDto;

	private final EventBus eventBus;

	@Inject
	public BootstrapperImpl(final EventBus eventBus, final PlaceManager placeManager, final DispatchAsync dispatcher,
			final UserDto userDto) {
		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.userDto = userDto;

		this.eventBus = eventBus;
	}

	@Override
	public void onBootstrap() {
		getCurrentUser();
	}

	private void getCurrentUser() {

		dispatcher.execute(new GetCurrentUserAction(), new AsyncCallbackImpl<GetCurrentUserResult>() {
			@Override
			public void onSuccess(GetCurrentUserResult result) {
				onGetCurrentUserSuccess(result.getCurrentUserDto());
			}

			@Override
			public void onFailure(Throwable caught) {
				super.onFailure(caught);
				placeManager.revealCurrentPlace();
			}
		});
	}

	private void onGetCurrentUserSuccess(UserDto currentUserDto) {

		userDto.copyFrom(currentUserDto);

		eventBus.fireEvent(new LoginAuthenticatedEvent(userDto));

		placeManager.revealCurrentPlace();
		// TODO have we reveal direct to the desired place or first visit login page?

		// placeManager.revealPlace(new PlaceRequest(NameTokens.getLogin()));
	}

}
