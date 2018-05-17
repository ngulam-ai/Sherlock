package agency.akcom.mmg.sherlock.ui.client.login;

import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import agency.akcom.mmg.sherlock.ui.client.place.NameTokens;

public class LoginPresenter extends Presenter<LoginPresenter.MyView, LoginPresenter.MyProxy> implements LoginUiHandlers {
	interface MyView extends View, HasUiHandlers<LoginUiHandlers> {
	}

	@ProxyStandard
	@NameToken(NameTokens.LOGIN)
	interface MyProxy extends ProxyPlace<LoginPresenter> {
	}

	@Inject
	LoginPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, RevealType.Root);

		getView().setUiHandlers(this);

	}

	@Override
	public void onSignInClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInWithGoogleClick() {
		Window.Location.replace("/withGoogle");
	}
}