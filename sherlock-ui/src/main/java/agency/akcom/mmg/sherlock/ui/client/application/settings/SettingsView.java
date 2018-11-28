package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.TokenConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.AlertType;
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

    int active = 0;

    ArrayList<DspDto> dspDtos;

   /* @UiHandler("textSecretId")
    void onSecretIdKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
            processSave();
    }*/

    @Override
    public void displayConfig(ArrayList<DspDto> dspDtos) {
        GWT.log("displayConfig");
        this.dspDtos = dspDtos;
        navTabs.clear();
        tabContent.clear();
        for (int i = 0; i < dspDtos.size(); i++) {
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
        switch (typeConnection) {
            case SECRET_ID: {
                tabPane.add(getRowConfigSecretIdHeader());
                for (int i = 0; i < curentDspDto.getConfigConnectionDtos().size(); i++) {
                    tabPane.add(getRowConfigSecretId((SecretIdConnectionDto) curentDspDto.getConfigConnectionDtos().get(i), i));
                }
                break;
            }
            case TOKEN: {
                tabPane.add(getRowConfigTokenHeader());
                for (int i = 0; i < curentDspDto.getConfigConnectionDtos().size(); i++) {
                    tabPane.add(getRowConfigToken((TokenConnectionDto) curentDspDto.getConfigConnectionDtos().get(i), i));
                }
                break;
            }
        }
        tabPane.add(getAddButton(indexDsp));
        if (indexDsp == active) {
            tabPane.setActive(true);
            tabListItem.setActive(true);
        }
        tabContent.add(tabPane);

    }

    public Row getRowConfigToken(TokenConnectionDto tokenConnectionDto, int curentIndex) {
        GWT.log("getRowConfigToken");

        Row row = new Row();

        TextBox textBoxName = new TextBox();
        TextBox textBoxToken = new TextBox();

        Button delete = getDellButton(curentIndex);
        Button save = getSaveButton(curentIndex);

        FocusPanel focusPanel = new FocusPanel();
        Text textName = new Text(tokenConnectionDto.getName());
        Text token = new Text(tokenConnectionDto.getToken());
        Alert alert = new Alert();
        Row rowAlert = new Row();

        alert.setType(AlertType.SUCCESS);
        alert.add(colum("XS_2", textName));
        alert.add(colum("XS_8", token));
        alert.setHeight("50px");
        focusPanel.add(alert);
        rowAlert.add(focusPanel);

        textBoxName.setText(tokenConnectionDto.getName());
        textBoxToken.setText(tokenConnectionDto.getToken());

        Collapse collapse = new Collapse();
        row.add(colum("XS_2", textBoxName));
        row.add(colum("XS_8", textBoxToken));
        row.add(colum("XS_1", save));
        row.add(colum("XS_1", delete));
        row.setHeight("60px");
        collapse.add(row);

        delete.addClickHandler(event -> {
            delete(tokenConnectionDto);
        });

        save.addClickHandler(event -> {
            GWT.log(" save.addClickHandler");
            Button curentButton = (Button) event.getSource();
            int indexCC = Integer.parseInt(curentButton.getId());
            int indexDsp = -1;
            for (int i = 0; i < dspDtos.size(); i++) {
                if (dspDtos.get(i).getPartner() == Partner.POCKETMATH) {
                    indexDsp = i;
                }
            }
            active = indexDsp;
            tokenConnectionDto.setName(textBoxName.getText());
            tokenConnectionDto.setToken(textBoxToken.getText());

            dspDtos.get(indexDsp).getConfigConnectionDtos().remove(indexCC);
            dspDtos.get(indexDsp).getConfigConnectionDtos().add(indexCC, tokenConnectionDto);

            getUiHandlers().onSaveClick(dspDtos.get(indexDsp));  // 1
            refresh();
        });

        focusPanel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                collapse.toggle();
            }
        });

        Row result = new Row();
        result.add(rowAlert);
        collapse.setToggle(false);
        result.add(collapse);

        return result;
    }

    public Row getRowConfigSecretId(SecretIdConnectionDto secretIdConnectionDto, int curentIndex) {
        GWT.log("getRowConfigSecretId");

        Row row = new Row();

        Alert alert = new Alert();
        TextBox textBoxName = new TextBox();
        TextBox textBoxSecretId = new TextBox();
        TextBox textBoxSecret = new TextBox();
        TextBox textBoxGrantType = new TextBox();

        Button delete = getDellButton(curentIndex);
        Button save = getSaveButton(curentIndex);

        textBoxName.setText(secretIdConnectionDto.getName());
        textBoxSecretId.setText(secretIdConnectionDto.getClientId());
        textBoxSecret.setText(secretIdConnectionDto.getClientSecret());
        textBoxGrantType.setText(secretIdConnectionDto.getGrantType());
        textBoxGrantType.setVisible(false);

        Text textName = new Text(secretIdConnectionDto.getName());
        Text textId = new Text(secretIdConnectionDto.getClientId());
        Text textSecret = new Text(secretIdConnectionDto.getClientSecret());
        FocusPanel focusPanel = new FocusPanel();
        Row rowAlert = new Row();

        alert.setType(AlertType.SUCCESS);
        alert.add(colum("XS_2", textName));
        alert.add(colum("XS_3", textId));
        alert.add(colum("XS_5", textSecret));
        alert.setHeight("50px");
        focusPanel.add(alert);
        rowAlert.add(focusPanel);

        Collapse collapse = new Collapse();
        row.add(colum("XS_2", textBoxName));
        row.add(colum("XS_3", textBoxSecretId));
        row.add(colum("XS_5", textBoxSecret));
//        row.add(colum("XS_3", textBoxGrantType));
        row.add(colum("XS_1", save));
        row.add(colum("XS_1", delete));;
        row.setHeight("60px");
        collapse.add(row);

        delete.addClickHandler(event -> {
            delete(secretIdConnectionDto);
        });
        save.addClickHandler(event -> {
            GWT.log("click save");
            Button curentButton = (Button) event.getSource();
            int indexCC = Integer.parseInt(curentButton.getId());
            int indexDsp = -1;
            for (int i = 0; i < dspDtos.size(); i++) {
                if (dspDtos.get(i).getPartner() == Partner.AVAZU) {
                    indexDsp = i;
                }
            }
            active = indexDsp;
            secretIdConnectionDto.setName(textBoxName.getText());
            secretIdConnectionDto.setClientId(textBoxSecretId.getText());
            secretIdConnectionDto.setClientSecret(textBoxSecret.getText());
            secretIdConnectionDto.setGrantType(textBoxGrantType.getText());

            dspDtos.get(indexDsp).getConfigConnectionDtos().remove(indexCC);
            dspDtos.get(indexDsp).getConfigConnectionDtos().add(indexCC, secretIdConnectionDto);

            getUiHandlers().onSaveClick(dspDtos.get(indexDsp)); //3
            refresh();
        });

        focusPanel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                collapse.toggle();
            }
        });

        Row result = new Row();
        result.add(rowAlert);
        collapse.setToggle(false);
        result.add(collapse);
        return result;
    }

    public Row getRowConfigSecretIdHeader() {
        Row row = new Row();
        row.add(colum("XS_2", "Name"));
        row.add(colum("XS_3", "Secret Id"));
        row.add(colum("XS_5", "Secret"));
//        row.add(colum("XS_3", "Grant Type"));
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
//        result.setPaddingBottom(-100);
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

    public Button getAddButton(int indexDsp) {
        DspDto curentDspDto = dspDtos.get(indexDsp);
        Button button = new Button("Add connections");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("ButtonAddOnClick");
                switch (curentDspDto.getTypeConnection()) {
                    case SECRET_ID: {
                        SecretIdConnectionDto newSecretIdConnectionDto = new SecretIdConnectionDto("ClientId", "Secret");
                        dspDtos.get(indexDsp).getConfigConnectionDtos().add(newSecretIdConnectionDto);
                        active = indexDsp;
                        break;
                    }
                    case TOKEN: {
                        TokenConnectionDto newTokenConnectionDto = new TokenConnectionDto();
                        dspDtos.get(indexDsp).getConfigConnectionDtos().add(newTokenConnectionDto);
                        active = indexDsp;
                        break;
                    }
                }
                refresh();
            }
        });
        return button;
    }

    public void delete(ConfigConnectionDto curentConnection) {
        int indexCC = getIndexCCandSetActive(curentConnection);

        dspDtos.get(active).getConfigConnectionDtos().remove(indexCC);
        getUiHandlers().onSaveClick(dspDtos.get(active)); //2
        refresh();
    }

    private int getIndexCCandSetActive(ConfigConnectionDto curentConnection) {
        int indexCC = -1;
        int indexDsp = -1;
        for (int i = 0; i < dspDtos.size(); i++) {
            int index = dspDtos.get(i).getConfigConnectionDtos().indexOf(curentConnection);
            if (index > -1) {
                indexDsp = i;
                indexCC = index;
                active = indexDsp;
            }
        }
        return indexCC;
    }

    private Button getDellButton(int curentIndex) {
        Button delete = new Button();
        delete.setIcon(IconType.REMOVE);
        delete.setColor("RED");
        delete.setId("" + curentIndex);
        return delete;
    }

    private Button getSaveButton(int curentIndex) {
        Button save = new Button();
        save.setIcon(IconType.SAVE);
        save.setColor("GREEN");
        save.setId("" + curentIndex);
        return save;
    }

    @Inject
    SettingsView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}