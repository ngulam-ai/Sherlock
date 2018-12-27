package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.client.widget.ExtRow;
import agency.akcom.mmg.sherlock.ui.client.widget.SecretIdRow;
import agency.akcom.mmg.sherlock.ui.client.widget.TokenRow;
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
import org.gwtbootstrap3.client.ui.html.Text;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;

public class SettingsView extends ViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {
    interface Binder extends UiBinder<Widget, SettingsView> {
    }

    @UiField
    NavTabs navTabs;

    @UiField
    TabContent tabContent;


    int activeDsp = 0;
    ArrayList<DspDto> dspDtos;
    HashMap<ConfigConnectionDto, ExtRow> storage = new HashMap<>();

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
                    SecretIdRow secretIdRow = (SecretIdRow) getRow(curentDspDto.getConfigConnectionDtos().get(i), curentDspDto.getPartner());
                    tabPane.add(secretIdRow);
                }
                break;
            }
            case TOKEN: {
                tabPane.add(getRowConfigTokenHeader());
                for (int i = 0; i < curentDspDto.getConfigConnectionDtos().size(); i++) {
                    TokenRow tokenRow = (TokenRow) getRow(curentDspDto.getConfigConnectionDtos().get(i), curentDspDto.getPartner());
                    tabPane.add(tokenRow);
                }
                break;
            }
        }
        tabPane.add(getAddButton(indexDsp));
        if (indexDsp == activeDsp) {
            tabPane.setActive(true);
            tabListItem.setActive(true);
        }
        tabContent.add(tabPane);
    }


    public Row getRowConfigSecretIdHeader() {
        Row row = new Row();
        row.add(colum("XS_2", "Name"));
        row.add(colum("XS_3", "Secret Id"));
        row.add(colum("XS_5", "Secret"));
        return row;
    }

    public Row getRowConfigTokenHeader() {
        Row row = new Row();
        row.add(colum("XS_2", "Name"));
        row.add(colum("XS_8", "Token"));
        return row;
    }

    private Column colum(String size, String name) {
        Column result = new Column(size);
        Text text = new Text(name);
        result.add(text);
        return result;
    }

    public void refresh() {
        GWT.log("refresh");
        displayConfig(dspDtos);
    }

    public Button getAddButton(int indexDsp) {
        DspDto curentDspDto = dspDtos.get(indexDsp);
        Button button = new Button("Add connections");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                switch (curentDspDto.getTypeConnection()) {
                    case SECRET_ID: {
                        SecretIdConnectionDto newSecretIdConnectionDto = new SecretIdConnectionDto("Client_id", "Client_secret");
                        newSecretIdConnectionDto.setName("Name");
                        dspDtos.get(indexDsp).getConfigConnectionDtos().add(newSecretIdConnectionDto);
                        activeDsp = indexDsp;
                        break;
                    }
                    case TOKEN: {
                        TokenConnectionDto newTokenConnectionDto = new TokenConnectionDto("Name", "Token");
                        dspDtos.get(indexDsp).getConfigConnectionDtos().add(newTokenConnectionDto);
                        activeDsp = indexDsp;
                        break;
                    }
                }
                refresh();
            }
        });
        return button;
    }

    public void deleteStorage(ExtRow curentRow) {
        GWT.log("deleteStorage");
        setActiveDsp(curentRow.getPartner());
        ConfigConnectionDto configConnectionDto = curentRow.getCurentConnection();
        int indexCC = dspDtos.get(activeDsp).getConfigConnectionDtos().indexOf(configConnectionDto);
        if (indexCC != -1) {
            dspDtos.get(activeDsp).getConfigConnectionDtos().remove(indexCC);
        }
        storage.remove(configConnectionDto);
    }

    public void saveStorage(ExtRow curentRow) {
        GWT.log("saveStorage");
        setActiveDsp(curentRow.getPartner());
        ConfigConnectionDto curentConnection = curentRow.getCurentConnection();
//        SecretIdConnectionDto oldConfigConnectionDto = (SecretIdConnectionDto) storage.entrySet().stream().filter(x -> x.getValue().equals(curentRow)).findFirst().get().getKey();
        storage.put(curentConnection, curentRow);
        dspDtos.get(activeDsp).getConfigConnectionDtos().add(curentConnection);
        checkConnection(curentRow);
    }

    private void setActiveDsp(Partner partner) {
        activeDsp = getIndexDsp(partner);
    }

    private int getIndexDsp(Partner partner) {
        int result = -1;
        for (int i = 0; i < dspDtos.size(); i++) {
            if (dspDtos.get(i).getPartner() == partner) {
                result = i;
            }
        }
        return result;
    }

    public void checkConnection(ExtRow curentRow) {
        getUiHandlers().CheckConfigConnections(curentRow);
    }

    private ExtRow getRow(ConfigConnectionDto curentConfigConnectionDto, Partner partner) {
        GWT.log("getRow");
        if (storage.containsKey(curentConfigConnectionDto)) {
            return storage.get(curentConfigConnectionDto);
        } else {
            switch (partner) {
                case AVAZU: {
                    SecretIdRow secretIdRow = new SecretIdRow();
                    secretIdRow.configuration((SecretIdConnectionDto) curentConfigConnectionDto, partner, this);
                    storage.put(curentConfigConnectionDto, secretIdRow);
                    return secretIdRow;
                }
                case POCKETMATH: {
                    TokenRow tokenRow = new TokenRow();
                    tokenRow.configuration((TokenConnectionDto) curentConfigConnectionDto, partner, this);
                    storage.put(curentConfigConnectionDto, tokenRow);
                    return tokenRow;
                }
            }
        }
        return null;
    }

    public void saveChangesInDao() {
        getUiHandlers().onSaveClick(dspDtos.get(activeDsp));
    }

    @Inject
    SettingsView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}