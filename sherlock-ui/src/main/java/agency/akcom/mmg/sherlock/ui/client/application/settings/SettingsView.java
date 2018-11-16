package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.shared.dto.AvazuConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
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
import java.util.ArrayList;

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

    DspDto curentDsp;
    ArrayList<DspDto> dspDtos;

    @UiHandler("textSecretId")
    void onSecretIdKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
            processSave();
    }

    @UiHandler("textSecret")
    void onSecretKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
            processSave();
    }

    @UiHandler("buttonSave")
    void onButtonSecret(ClickEvent event) {
        DspDto dspDto = new DspDto();  //tempory
        dspDto.setName("Avazu");
        GWT.log("onButtonSecret");
        if (getUiHandlers() != null) {
            GWT.log("getUiHandlers no NULL");
            getUiHandlers().onSaveClick(dspDtos);
        }
    }

	/*@UiHandler("buttonCreate")
	void onButtonCreate(ClickEvent event)  {
		Window.alert("Create");
	}*/


    private void processSave() {
        int indexDsp = findIndexDspDto(curentDsp.getPartner());
        if (indexDsp>-1){
            switch (dspDtos.get(indexDsp).getTypeConnection()){
                case SECRET_ID: {
                    AvazuConnectionDto avazuConnectionDto = new AvazuConnectionDto();
                    avazuConnectionDto.setClientId(textSecretId.getText());
                    avazuConnectionDto.setClientSecret(textSecret.getText());
                    avazuConnectionDto.setGrantType(textGrantType.getText());
                    dspDtos.get(indexDsp).getConfigConnectionDtos().add(avazuConnectionDto);
                }
                break;
            }
        }
        GWT.log("processSave");
        getUiHandlers().onSaveClick(dspDtos);
    }

    @Override
    public void displayConfig(ArrayList<DspDto> dspDtos) {
        this.dspDtos = dspDtos;
        DspDto dspDto = dspDtos.get(0);
        this.curentDsp = dspDto;
        TypeConnection typeConnection = dspDto.getTypeConnection();
        GWT.log("displayConfig");
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
        GWT.log("displayConfigWithSecret");
    }


    @Inject
    SettingsView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));

    }

    public int findIndexDspDto(Partner partner){
        for(int i =0;i<dspDtos.size();i++){
            if (dspDtos.get(i).getPartner()==partner)
                return i;
        }
        return -1;
    }

}