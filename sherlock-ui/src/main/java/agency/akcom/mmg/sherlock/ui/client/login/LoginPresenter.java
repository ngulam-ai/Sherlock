package agency.akcom.mmg.sherlock.ui.client.login;

import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import agency.akcom.mmg.sherlock.ui.client.dispatch.AsyncCallbackImpl;
import agency.akcom.mmg.sherlock.ui.client.event.LoginResetEvent;
import agency.akcom.mmg.sherlock.ui.client.place.NameTokens;
import agency.akcom.mmg.sherlock.ui.client.security.IsLoggedInGatekeeper;
import agency.akcom.mmg.sherlock.ui.shared.action.SignOutAction;
import agency.akcom.mmg.sherlock.ui.shared.action.SignOutResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.UserDto;

public class LoginPresenter extends Presenter<LoginPresenter.MyView, LoginPresenter.MyProxy>
		implements LoginUiHandlers {
	interface MyView extends View, HasUiHandlers<LoginUiHandlers> {

		void prepareView(UserDto userDto);
	}

	@ProxyStandard
	@NameToken(NameTokens.LOGIN)
	@NoGatekeeper
	interface MyProxy extends ProxyPlace<LoginPresenter> {
	}

	IsLoggedInGatekeeper gatekeeper;
	private final DispatchAsync dispatcher;
	private final PlaceManager placeManager;

	@Inject
	LoginPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
			final IsLoggedInGatekeeper gatekeeper, final DispatchAsync dispatcher, final PlaceManager placeManager) {
		super(eventBus, view, proxy, RevealType.Root);

		this.gatekeeper = gatekeeper;
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;

		getView().setUiHandlers(this);
	}

	@Override
	protected void onReset() {
		super.onReset();

		getView().prepareView(gatekeeper.getCurrentUser());
	}

	@Override
	public void onSignInClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInWithGoogleClick() {
		Window.Location.replace("/withGoogle");
	}

	@Override
	public void onSignOutClick() {
		dispatcher.execute(new SignOutAction(), new AsyncCallbackImpl<SignOutResult>() {
			@Override
			public void onSuccess(SignOutResult result) {
				getEventBus().fireEvent(new LoginResetEvent());
				placeManager.revealDefaultPlace();
			}
		});
	}
}