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
import org.gwtbootstrap3.client.ui.*;


import javax.inject.Inject;

class SettingsView extends ViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {
	interface Binder extends UiBinder<Widget, SettingsView> {
	}

	@UiField
	Button buttonSave;

	@UiField
	TextBox textSecretId;

	@UiField
	TextBox textSecret;

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
		DspDto dsp = new DspDto();  //tepory
		dsp.setName("Avazu");
		GWT.log(event.toDebugString());
		getUiHandlers().onSaveClick(dsp);
	}


	private void processSave() {
		DspDto dsp = new DspDto();  //tepory
		dsp.setName("Avazu");
		GWT.log("YEEEE1");
		getUiHandlers().onSaveClick(dsp);
	}

	@Override
	public void displayConfig(DspDto dspDto) {
		TypeConnection typeConnection = dspDto.getConfigConnectionDtos().get(0).getTypeConnection();
		GWT.log(String.valueOf(typeConnection));
		if (typeConnection == TypeConnection.SECRET_ID) {
			AvazuConnectionDto avazuConnectionDto = (AvazuConnectionDto) dspDto.getConfigConnectionDtos().get(0);
			displayConfigWithSecret(avazuConnectionDto);
		}
	}

	public void displayConfigWithSecret(AvazuConnectionDto avazuConnectionDto) {
		textSecretId.setText(avazuConnectionDto.getClientId());
		textSecret.setText(avazuConnectionDto.getClientSecret());
//		inputGroup.add(textSecretId);
//		inputGroup.add(textSecret);
//		inputGroup.add(buttonSave);
		GWT.log(avazuConnectionDto.getClientId());
//		anchorListItem.add(inputGroup);
	}



	@Inject
	SettingsView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));

	}

}