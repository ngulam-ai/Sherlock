package agency.akcom.mmg.sherlock.ui.client.login;

import javax.inject.Inject;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Well;
import org.gwtbootstrap3.client.ui.html.Text;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import agency.akcom.mmg.sherlock.ui.shared.dto.UserDto;

class LoginView extends ViewWithUiHandlers<LoginUiHandlers> implements LoginPresenter.MyView {
	interface Binder extends UiBinder<Widget, LoginView> {
	}
	
	@UiField
	Button signInWithGoogle;
	
	@UiField
	Text nameText;
	
	@UiField
	Well notApprovedWarn;

	@Inject
	LoginView(final Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("signInWithGoogle")
	public void onSignInWithGoogleClick(ClickEvent event) {
		getUiHandlers().onSignInWithGoogleClick();
	}

	@Override
	public void prepareView(UserDto userDto) {
		if (userDto != null && userDto.isLoggedIn() && !userDto.isAdmin()) {
			signInWithGoogle.setVisible(false);
			nameText.setText(userDto.getCustomerName());
			notApprovedWarn.setVisible(true);			
		}		
	}
	
	//@UiHandler("signOut")
	public void onSignOutClick(ClickEvent event) {
		getUiHandlers().onSignOutClick();
	}
}