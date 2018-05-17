package agency.akcom.mmg.sherlock.ui.client.login;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

class LoginView extends ViewWithUiHandlers<LoginUiHandlers> implements LoginPresenter.MyView {
	interface Binder extends UiBinder<Widget, LoginView> {
	}

	@Inject
	LoginView(final Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("signInWithGoogle")
	public void onSignInWithGoogleClick(ClickEvent event) {
		getUiHandlers().onSignInWithGoogleClick();
	}

}