package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.shared.dto.AvazuConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Text;

import javax.inject.Inject;

class SettingsView extends ViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {
	interface Binder extends UiBinder<Widget, SettingsView> {
	}

	/*@UiField
	Button buttonCreate;*/

	@UiField
	Button buttonSave;

	@UiField
	TextBox textSecretId;

	@UiField
	TextBox textSecret;

	@UiField
	TextBox textGrantType;

	@UiField
	Text nameText;

	@UiHandler("textSecretId")
	void onSecretIdKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode()== KeyCodes.KEY_ENTER)
			processSave();
	}

	@UiHandler("textSecret")
	void onSecretKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode()==KeyCodes.KEY_ENTER)
			processSave();
	}

	@UiHandler("buttonSave")
	void onButtonSecret(ClickEvent event)  {
		DspDto dsp = new DspDto();  //tempory
		dsp.setName("Avazu");
		GWT.log(event.toDebugString());
		getUiHandlers().onSaveClick(dsp);
	}

	/*@UiHandler("buttonCreate")
	void onButtonCreate(ClickEvent event)  {
		Window.alert("Create");
	}*/


	private void processSave() {
		DspDto dspDto = new DspDto();  //tempory
		dspDto.setName("Avazu");
		GWT.log("YEEEE1");
		getUiHandlers().onSaveClick(dspDto);
	}

	@Override
	public void displayConfig(DspDto dspDto) {
		TypeConnection typeConnection = dspDto.getConfigConnectionDtos().get(0).getTypeConnection();
		GWT.log(String.valueOf(typeConnection));
		if (typeConnection == TypeConnection.SECRET_ID) {
			AvazuConnectionDto avazuConnectionDto = (AvazuConnectionDto) dspDto.getConfigConnectionDtos().get(0);
			nameText.setText(dspDto.getName());
			displayConfigWithSecret(avazuConnectionDto);
		}
	}

	public void displayConfigWithSecret(AvazuConnectionDto avazuConnectionDto) {
		textSecretId.setText(avazuConnectionDto.getClientId());
		textSecret.setText(avazuConnectionDto.getClientSecret());
		textGrantType.setText(avazuConnectionDto.getGrantType());
		GWT.log(avazuConnectionDto.getClientId());
	}



	@Inject
	SettingsView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));

	}

}