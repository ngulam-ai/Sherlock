package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Text;

import javax.inject.Inject;
import java.util.ArrayList;

class SettingsView extends ViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {
    interface Binder extends UiBinder<Widget, SettingsView> {
    }

    //    Button buttonSave = new Button("Save");
    Button buttonAdd = new Button("Add connections");

//    @UiField
    Panel panel = new Panel();

    @UiField
    AnchorListItem anchorList;

    @UiField
            AnchorListItem test2;

    DspDto curentDsp;
    ArrayList<DspDto> dspDtos;

   /* @UiHandler("textSecretId")
    void onSecretIdKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
            processSave();
    }

    @UiHandler("textSecret")
    void onSecretKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
            processSave();
    }*/


    private void processSave() {
        int indexDsp = findIndexDspDto(curentDsp.getPartner());
        if (indexDsp > -1) {
//            switch (dspDtos.get(indexDsp).getTypeConnection()){
//                case SECRET_ID: {
//            SecretIdConnectionDto secretIdConnectionDto = new SecretIdConnectionDto();
//            secretIdConnectionDto.setClientId(textSecretId.getText());
//            secretIdConnectionDto.setClientSecret(textSecret.getText());
//            secretIdConnectionDto.setGrantType(textGrantType.getText());
//            dspDtos.get(indexDsp).getConfigConnectionDtos().clear();
//            dspDtos.get(indexDsp).getConfigConnectionDtos().add(secretIdConnectionDto);
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
        this.curentDsp = dspDtos.get(0);
//        TypeConnection typeConnection = dspDto.getTypeConnection();
//        if (typeConnection == TypeConnection.SECRET_ID) {

        PanelHeader panelHeader = new PanelHeader();
        Heading heading = new Heading(HeadingSize.H3, curentDsp.getName());
        panelHeader.add(heading);
        panel.add(panelHeader);

        for (int i = 0; i < curentDsp.getConfigConnectionDtos().size(); i++) {
            SecretIdConnectionDto secretIdConnectionDto = (SecretIdConnectionDto) curentDsp.getConfigConnectionDtos().get(i);
            displayConfigWithSecret(secretIdConnectionDto);
        }

        panel.add(buttonAdd);
        anchorList.add(panel);
        Label label = new Label("dssd");
//        AnchorListItem anchorListItem = new AnchorListItem("test");
        test2.add(label);

//        panel.add(buttonSave);
    }

    public void displayConfigWithSecret(SecretIdConnectionDto secretIdConnectionDto) {
        GWT.log("displayConfigWithSecret");

        TextBox textBoxName = new TextBox();
        TextBox textBoxSecretId = new TextBox();
        TextBox textBoxSecret = new TextBox();
        TextBox textBoxGrantType = new TextBox();
        Button delete = new Button();
        delete.setIcon(IconType.REMOVE);
        delete.setColor("RED");
        Button save = new Button();
        save.setIcon(IconType.SAVE);
        save.setColor("GREEN");

        textBoxName.setText(secretIdConnectionDto.getName());
        textBoxSecretId.setText(secretIdConnectionDto.getClientId());
        textBoxSecret.setText(secretIdConnectionDto.getClientSecret());
        textBoxGrantType.setText(secretIdConnectionDto.getGrantType());

        PanelBody panelBody = new PanelBody();
        Row row = new Row();

        row.add(colum("XS_2", "Name", textBoxName));
        row.add(colum("XS_2", "Secret Id", textBoxSecretId));
        row.add(colum("XS_3", "Secret", textBoxSecret));
        row.add(colum("XS_3", "Grant Type", textBoxGrantType));
        row.add(colum("XS_1", save));
        row.add(colum("XS_1", delete));
        Text text = new Text("ID: " + secretIdConnectionDto.getId());
        row.add(text);

        panelBody.add(row);
        panel.add(panelBody);
        delete.addClickHandler(event -> {
            int indexCC = curentDsp.getConfigConnectionDtos().indexOf(secretIdConnectionDto);
            int indexDsp = dspDtos.indexOf(curentDsp);

            curentDsp.getConfigConnectionDtos().remove(indexCC);
            dspDtos.remove(indexDsp);
            dspDtos.add(indexDsp, curentDsp);

            getUiHandlers().onSaveClick(dspDtos);
        });
        save.addClickHandler(event -> {
            int indexCC = curentDsp.getConfigConnectionDtos().indexOf(secretIdConnectionDto);
            int indexDsp = dspDtos.indexOf(curentDsp);

            secretIdConnectionDto.setName(textBoxName.getText());
            secretIdConnectionDto.setClientId(textBoxSecretId.getText());
            secretIdConnectionDto.setClientSecret(textBoxSecret.getText());
            secretIdConnectionDto.setGrantType(textBoxGrantType.getText());

            curentDsp.getConfigConnectionDtos().remove(indexCC);
            curentDsp.getConfigConnectionDtos().add(indexCC, secretIdConnectionDto);

            dspDtos.remove(indexDsp);
            dspDtos.add(indexDsp, curentDsp);

            getUiHandlers().onSaveClick(dspDtos);
        });
    }


    public int findIndexDspDto(Partner partner) {
        for (int i = 0; i < dspDtos.size(); i++) {
            if (dspDtos.get(i).getPartner() == partner)
                return i;
        }
        return -1;
    }

    private Column colum(String size, String name, Widget child) {
        Column result = new Column(size);
        Text text = new Text(name);
        result.add(text);
        result.add(child);
        return result;
    }

    private Column colum(String size, Widget child) {
        Column result = new Column(size);
        result.add(child);
        return result;
    }

    private Column colum(String size, String name) {
        Column result = new Column(size);
        Text text = new Text(name);
        result.add(text);
        return result;
    }

    private void addConfigConnections() {
        GWT.log("addConfigConnections");
        SecretIdConnectionDto newSecretIdConnectionDto = new SecretIdConnectionDto("ClientId", "Secret");
        curentDsp.getConfigConnectionDtos().add(newSecretIdConnectionDto);
    }

    private void refresh() {
        GWT.log("refresh");
        panel.clear();
        displayConfig(dspDtos);
    }

    @Inject
    SettingsView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));

        buttonAdd.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("ButtonSaveOnClick");
                addConfigConnections();
                refresh();
            }
        });

//        buttonSave.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                GWT.log("onButtonSecret");
//                processSave();
////                if (getUiHandlers() != null) {
////                    GWT.log("getUiHandlers no NULL");
////            getUiHandlers().onSaveClick(dspDtos);
////                }
//            }
//        });

    }
}