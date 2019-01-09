package agency.akcom.mmg.sherlock.ui.client.widget;

import agency.akcom.mmg.sherlock.ui.client.application.settings.SettingsView;
import agency.akcom.mmg.sherlock.ui.shared.dto.TokenConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Text;


public class TokenRow extends ExtRow {
    TokenConnectionDto curentConnection;
    TextBox textBoxName = new TextBox();
    TextBox textBoxToken = new TextBox();
    Text textName = new Text();
    Text textToken = new Text();

    public TokenRow() {
        super();
        GWT.log("TokenRow");
    }

    public void configuration(TokenConnectionDto curentConnection, Partner partner, SettingsView parent) {
        GWT.log("TokenRow.configuration");
        this.curentConnection = curentConnection;
        this.parent = parent;
        this.partner = partner;
        init();
    }

    @Override
    protected void createRowEdit() {

        createDellButton();
        createSaveButton();
        saveClickHandler();
        deleteClickHandler();

        textBoxName.setText(curentConnection.getName());
        textBoxToken.setText(getSecurityStr(curentConnection.getToken()));

        rowEdit.add(colum("XS_2", textBoxName));
        rowEdit.add(colum("XS_8", textBoxToken));
        rowEdit.add(colum("XS_1", save));
        rowEdit.add(colum("XS_1", delete));
        rowEdit.setHeight("60px");
    }

    @Override
    protected void createRowAlert() {
        FocusPanel focusPanel = new FocusPanel();

        setTexts();

        alert.add(colum("XS_2", textName));
        alert.add(colum("XS_9", textToken));
        alert.add(colum("XS_1", textCheckCoonection));
        alert.setHeight("50px");
        focusPanel.add(alert);
        rowAlert.add(focusPanel);

        focusPanel.addClickHandler(event -> collapse.toggle());
    }

    public void saveClickHandler() {
        save.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                save();
            }
        });
    }

    public void deleteClickHandler() {
        delete.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                delete();
            }
        });
    }

    public void save() {
        resultCheckConncetion = false;
        checkAlert();
        parent.deleteStorage(this);
        curentConnection.setName(textBoxName.getText());
        if (!checkSecurityStr(textToken.getText(), textBoxToken.getText())) {
            curentConnection.setToken(textBoxToken.getText());
        }
        textBoxToken.setText(getSecurityStr(curentConnection.getToken()));
        parent.saveStorage(this);
        parent.saveChangesInDao();
        setTexts();
        checkConnection();
        GWT.log("save");
        parent.refresh();
        GWT.log("save2");
    }

    public void delete() {
        parent.deleteStorage(this);
        parent.saveChangesInDao();
        parent.refresh();
    }

    private void setTexts() {
        textName.setText(curentConnection.getName());
        textToken.setText(getSecurityStr(curentConnection.getToken()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenRow tokenRow = (TokenRow) o;

        if (curentConnection != null ? !curentConnection.equals(tokenRow.curentConnection) : tokenRow.curentConnection != null)
            return false;
        if (textBoxName != null ? !textBoxName.equals(tokenRow.textBoxName) : tokenRow.textBoxName != null)
            return false;
        if (textBoxToken != null ? !textBoxToken.equals(tokenRow.textBoxToken) : tokenRow.textBoxToken != null)
            return false;
        if (textName != null ? !textName.equals(tokenRow.textName) : tokenRow.textName != null) return false;
        return textToken != null ? textToken.equals(tokenRow.textToken) : tokenRow.textToken == null;
    }

    @Override
    public int hashCode() {
        int result = curentConnection != null ? curentConnection.hashCode() : 0;
        result = 31 * result + (textBoxName != null ? textBoxName.hashCode() : 0);
        result = 31 * result + (textBoxToken != null ? textBoxToken.hashCode() : 0);
        result = 31 * result + (textName != null ? textName.hashCode() : 0);
        result = 31 * result + (textToken != null ? textToken.hashCode() : 0);
        return result;
    }

    public TokenConnectionDto getCurentConnection() {
        return curentConnection;
    }
}
