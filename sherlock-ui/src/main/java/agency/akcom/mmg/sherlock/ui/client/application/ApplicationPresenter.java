package agency.akcom.mmg.sherlock.ui.client.application;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;

import agency.akcom.mmg.sherlock.ui.client.dispatch.AsyncCallbackImpl;
import agency.akcom.mmg.sherlock.ui.client.event.LoginResetEvent;
import agency.akcom.mmg.sherlock.ui.client.security.IsLoggedInGatekeeper;
import agency.akcom.mmg.sherlock.ui.shared.UserDto;
import agency.akcom.mmg.sherlock.ui.shared.action.SignOutAction;
import agency.akcom.mmg.sherlock.ui.shared.action.SignOutResult;

public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy>
		implements ApplicationUiHandlers {

	interface MyView extends View, HasUiHandlers<ApplicationUiHandlers> {
		void displayUser(String userInfo, String userPictureURL);
	}

	public static final NestedSlot SLOT_MAIN_CONTENT = new NestedSlot();

	@ProxyStandard
	interface MyProxy extends Proxy<ApplicationPresenter> {
	}

	private final IsLoggedInGatekeeper gatekeeper;
	private final DispatchAsync dispatcher;
	private final PlaceManager placeManager;

	@Inject
	ApplicationPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
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
		UserDto currentUser = gatekeeper.getCurrentUser();
		getView().displayUser(currentUser.getCustomerName() + " (" + currentUser.getLogin() + ")",
				currentUser.getPictureURL() + "?sz=20");
	}

	@Override
	public void onSignOutClick() {
		dispatcher.execute(new SignOutAction(), new AsyncCallbackImpl<SignOutResult>() {
			@Override
			public void onSuccess(SignOutResult result) {
				getEventBus().fireEvent(new LoginResetEvent());
				placeManager.revealCurrentPlace();
			}
		});
	}
}