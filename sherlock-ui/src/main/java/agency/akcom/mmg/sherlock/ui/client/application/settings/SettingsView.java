package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.TokenConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Text;

import javax.inject.Inject;
import java.util.ArrayList;

class SettingsView extends ViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {
    interface Binder extends UiBinder<Widget, SettingsView> {
    }

    @UiField
    NavTabs navTabs;

    @UiField
    TabContent tabContent;

    ArrayList<DspDto> dspDtos;

   /* @UiHandler("textSecretId")
    void onSecretIdKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
            processSave();
    }*/

    private void processSave() {
        GWT.log("processSave");
//        getUiHandlers().onSaveClick(dspDtos);
    }

    @Override
    public void displayConfig(ArrayList<DspDto> dspDtos) {
        GWT.log("displayConfig");
        this.dspDtos = dspDtos;
        navTabs.clear();
        tabContent.clear();
        for (int i=0;i<dspDtos.size();i++) {
            displayConfigDsp(i);
        }
    }


    public void displayConfigDsp(int indexDsp) {
        DspDto curentDspDto = dspDtos.get(indexDsp);
        GWT.log("displayConfigDsp");

        String name = curentDspDto.getName();

        //header
        TabListItem tabListItem = new TabListItem(name);
        tabListItem.setDataTarget("#" + name);
        navTabs.add(tabListItem);

        //body
        TabPane tabPane = new TabPane();
        tabPane.setId(name);

        TypeConnection typeConnection = curentDspDto.getTypeConnection();
        switch (typeConnection){
            case SECRET_ID:{
               tabPane.add(getRowConfigSecretIdHeader());
               for (ConfigConnectionDto configConnectionDto : curentDspDto.getConfigConnectionDtos()) {
                   tabPane.add(getRowConfigSecretId((SecretIdConnectionDto) configConnectionDto));
               }
               break;
           }
            case TOKEN:{
                tabPane.add(getRowConfigTokenHeader());
                for (ConfigConnectionDto configConnectionDto : curentDspDto.getConfigConnectionDtos()){
                    tabPane.add(getRowConfigToken((TokenConnectionDto) configConnectionDto));
                }
                break;
            }
        }
        tabPane.add(getAddButton(indexDsp));
        tabContent.add(tabPane);

    }

    public Row getRowConfigToken(TokenConnectionDto tokenConnectionDto){
        GWT.log("getRowConfigToken");

        Row row = new Row();

        TextBox textBoxName = new TextBox();
        TextBox textBoxToken = new TextBox();

        Button delete = new Button();
        delete.setIcon(IconType.REMOVE);
        delete.setColor("RED");
        Button save = new Button();
        save.setIcon(IconType.SAVE);
        save.setColor("GREEN");

        textBoxName.setText(tokenConnectionDto.getName());
        textBoxToken.setText(tokenConnectionDto.getToken());

        row.add(colum("XS_2", textBoxName));
        row.add(colum("XS_8", textBoxToken));
        row.add(colum("XS_1", save));
        row.add(colum("XS_1", delete));

        delete.addClickHandler(event -> {
            int indexCC = -1;
            int indexDsp = -1;
            for (int i = 0; i < dspDtos.size(); i++) {
                int index = dspDtos.get(i).getConfigConnectionDtos().indexOf(tokenConnectionDto);
                if (index > -1) {
                    indexDsp = i;
                    indexCC = index;
                }
            }

            dspDtos.get(indexDsp).getConfigConnectionDtos().remove(indexCC);
            getUiHandlers().onSaveClick(dspDtos.get(indexDsp)); //4
        });

        save.addClickHandler(event -> {
            GWT.log(" save.addClickHandler");
            int indexCC = -1;
            int indexDsp = -1;
            for (int i = 0; i < dspDtos.size(); i++) {
                int index = dspDtos.get(i).getConfigConnectionDtos().indexOf(tokenConnectionDto);
                if (index > -1) {
                    indexDsp = i;
                    indexCC = index;

                }
            }

            tokenConnectionDto.setName(textBoxName.getText());
            tokenConnectionDto.setToken(textBoxToken.getText());

            dspDtos.get(indexDsp).getConfigConnectionDtos().remove(indexCC);
            dspDtos.get(indexDsp).getConfigConnectionDtos().add(indexCC, tokenConnectionDto);

            getUiHandlers().onSaveClick(dspDtos.get(indexDsp));  // 1
        });

        return row;
    }

    public Row getRowConfigSecretId(SecretIdConnectionDto secretIdConnectionDto) {
        GWT.log("getRowConfigSecretId");

        Row row = new Row();

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
        textBoxGrantType.setVisible(false);

        row.add(colum("XS_2", textBoxName));
        row.add(colum("XS_2", textBoxSecretId));
        row.add(colum("XS_3", textBoxSecret));
        row.add(colum("XS_3", textBoxGrantType));
        row.add(colum("XS_1", save));
        row.add(colum("XS_1", delete));

        delete.addClickHandler(event -> {
            int indexCC = -1;
            int indexDsp = -1;
            for (int i = 0; i < dspDtos.size(); i++) {
                int index = dspDtos.get(i).getConfigConnectionDtos().indexOf(secretIdConnectionDto);
                if (index > -1) {
                    indexDsp = i;
                    indexCC = index;
                }
            }

            dspDtos.get(indexDsp).getConfigConnectionDtos().remove(indexCC);
            getUiHandlers().onSaveClick(dspDtos.get(indexDsp)); //2
        });
        save.addClickHandler(event -> {
            int indexCC = -1;
            int indexDsp = -1;
            for (int i = 0; i < dspDtos.size(); i++) {
                int index = dspDtos.get(i).getConfigConnectionDtos().indexOf(secretIdConnectionDto);
                if (index > -1) {
                    indexDsp = i;
                    indexCC = index;

                }
            }

            secretIdConnectionDto.setName(textBoxName.getText());
            secretIdConnectionDto.setClientId(textBoxSecretId.getText());
            secretIdConnectionDto.setClientSecret(textBoxSecret.getText());
            secretIdConnectionDto.setGrantType(textBoxGrantType.getText());

            dspDtos.get(indexDsp).getConfigConnectionDtos().remove(indexCC);
            dspDtos.get(indexDsp).getConfigConnectionDtos().add(indexCC, secretIdConnectionDto);

            getUiHandlers().onSaveClick(dspDtos.get(indexDsp)); //3
        });
        return row;
    }

    public Row getRowConfigSecretIdHeader() {
        Row row = new Row();
        row.add(colum("XS_2", "Name"));
        row.add(colum("XS_2", "Secret Id"));
        row.add(colum("XS_3", "Secret"));
        row.add(colum("XS_3", "Grant Type"));
        return row;
    }

    public Row getRowConfigTokenHeader() {
        Row row = new Row();
        row.add(colum("XS_2", "Name"));
        row.add(colum("XS_8", "Token"));
        return row;
    }

    public int findIndexDspDto(Partner partner) {
        for (int i = 0; i < dspDtos.size(); i++) {
            if (dspDtos.get(i).getPartner() == partner)
                return i;
        }
        return -1;
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

    private void refresh() {
        GWT.log("refresh");
        displayConfig(dspDtos);
    }

    public Button getAddButton(int indexDsp){
        DspDto curentDspDto = dspDtos.get(indexDsp);
        Button button = new Button("Add connections");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("ButtonAddOnClick");
                GWT.log("index: "+indexDsp);
                switch (curentDspDto.getTypeConnection()){
                    case SECRET_ID:{
                        SecretIdConnectionDto newSecretIdConnectionDto = new SecretIdConnectionDto("ClientId", "Secret");
                        dspDtos.get(indexDsp).getConfigConnectionDtos().add(newSecretIdConnectionDto);
                        break;
                    }
                    case TOKEN:{
                        TokenConnectionDto newTokenConnectionDto = new TokenConnectionDto();
                        dspDtos.get(indexDsp).getConfigConnectionDtos().add(newTokenConnectionDto);
                        break;
                    }
                }
                refresh();
            }
        });
        return button;
    }

    @Inject
    SettingsView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}