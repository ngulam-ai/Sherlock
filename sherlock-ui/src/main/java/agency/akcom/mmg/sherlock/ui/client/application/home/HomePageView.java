package agency.akcom.mmg.sherlock.ui.client.application.home;

import java.util.ArrayList;

import javax.inject.Inject;

import org.gwtbootstrap3.client.ui.FormGroup;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.configConnection.AvazuConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.TypeConnection;

import org.gwtbootstrap3.client.ui.*;

class HomePageView extends ViewImpl implements HomePagePresenter.MyView {
	interface Binder extends UiBinder<Widget, HomePageView> {
	}

	@UiField
	NavPills dspNav;

	@UiField
	InputGroupButton inputGroup;
	
	@UiField
	Button buttonSecretId;
	
	@UiField
	Button buttonSecret;
	
	@UiField
	TextBox textSecretId;
	
	@UiField
	TextBox textSecret;
	
	@UiField
	AnchorListItem anchorListItem; 
	

	/*
	 * @Override private void displayDsp(ArrayList<Dsp> dsps) {
	 * 
	 * }
	 */

	@Override
	public void displayConfig(Dsp dsp) {
		TypeConnection typeConnection = dsp.getConfigConnections().get(0).getTypeConnection();
		if (typeConnection == TypeConnection.SECRET_ID) {
			displayConfigWithSecret();
		}
	}
	
	@Override
	public void displayConfigWithSecret(AvazuConnection avazuConnection) {
		inputGroup.add(textSecretId);
		inputGroup.add(buttonSecretId);
		inputGroup.add(textSecret);
		inputGroup.add(buttonSecret);
		anchorListItem.add(inputGroup);
	}

	@Inject
	HomePageView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
}