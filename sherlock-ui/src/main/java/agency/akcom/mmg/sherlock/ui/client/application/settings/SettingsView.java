package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
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
        GWT.log("onButtonSecret");
        processSave();
        if (getUiHandlers() != null) {
            GWT.log("getUiHandlers no NULL");
//            getUiHandlers().onSaveClick(dspDtos);
        }

    }


    private void processSave() {
        int indexDsp = findIndexDspDto(curentDsp.getPartner());
        if (indexDsp>-1){
//            switch (dspDtos.get(indexDsp).getTypeConnection()){
//                case SECRET_ID: {
                    SecretIdConnectionDto secretIdConnectionDto = new SecretIdConnectionDto();
                    secretIdConnectionDto.setClientId(textSecretId.getText());
                    secretIdConnectionDto.setClientSecret(textSecret.getText());
                    secretIdConnectionDto.setGrantType(textGrantType.getText());
                    dspDtos.get(indexDsp).getConfigConnectionDtos().clear();
                    dspDtos.get(indexDsp).getConfigConnectionDtos().add(secretIdConnectionDto);
//                }
//                break;
//            }
        }
        GWT.log("processSave");
        getUiHandlers().onSaveClick(dspDtos);
    }

    @Override
    public void displayConfig(ArrayList<DspDto> dspDtos) {
        GWT.log("displayConfig");
        this.dspDtos = dspDtos;
        this.curentDsp =  dspDtos.get(0);
//        TypeConnection typeConnection = dspDto.getTypeConnection();
//        if (typeConnection == TypeConnection.SECRET_ID) {
            GWT.log("displayConfigIf");
            SecretIdConnectionDto secretIdConnectionDto = (SecretIdConnectionDto) curentDsp.getConfigConnectionDtos().get(0);
            nameText.setText(curentDsp.getName());
            displayConfigWithSecret(secretIdConnectionDto);
//        }
    }

    public void displayConfigWithSecret(SecretIdConnectionDto secretIdConnectionDto) {
        textSecretId.setText(secretIdConnectionDto.getClientId());
        textSecret.setText(secretIdConnectionDto.getClientSecret());
        textGrantType.setText(secretIdConnectionDto.getGrantType());
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