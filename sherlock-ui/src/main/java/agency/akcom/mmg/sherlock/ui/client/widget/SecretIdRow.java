package agency.akcom.mmg.sherlock.ui.client.widget;

import agency.akcom.mmg.sherlock.ui.client.application.settings.SettingsView;
import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Text;

public class SecretIdRow extends ExtRow {
    SecretIdConnectionDto curentConnection;

    TextBox textBoxName = new TextBox();
    TextBox textBoxSecretId = new TextBox();
    TextBox textBoxSecret = new TextBox();
    Text textName =new Text();
    Text textId =new Text();
    Text textSecret =new Text();

    public SecretIdRow() {
    }

    public void configuration(SecretIdConnectionDto curentConnection, Partner partner, SettingsView parent) {
        GWT.log("SecretIdRow.configuration");
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
        textBoxSecretId.setText(curentConnection.getClientId());
        textBoxSecret.setText(curentConnection.getClientSecret());

        rowEdit.add(colum("XS_2", textBoxName));
        rowEdit.add(colum("XS_3", textBoxSecretId));
        rowEdit.add(colum("XS_5", textBoxSecret));
        rowEdit.add(colum("XS_1", save));
        rowEdit.add(colum("XS_1", delete));
        rowEdit.setHeight("60px");
    }

    @Override
    protected void createRowAlert() {
        FocusPanel focusPanel = new FocusPanel();

        setTexts();

        alert.add(colum("XS_2", textName));
        alert.add(colum("XS_3", textId));
        alert.add(colum("XS_5", textSecret));
        alert.add(colum("XS_2",textCheckCoonection));
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
//                fireEvent(new SaveConfigConnectionEvent());
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

    private void save(){
        resultCheckConncetion=false;
        checkAlert();
        parent.deleteStorage(this);
        curentConnection.setName(textBoxName.getText());
        curentConnection.setClientId(textBoxSecretId.getText());
        curentConnection.setClientSecret(textBoxSecret.getText());
        parent.saveStorage(this);
        parent.saveChangesInDao();
        setTexts();
        checkConnection();
        parent.refresh();
    }

    private void delete(){
        parent.deleteStorage(this);
        parent.saveChangesInDao();
        parent.refresh();
    }

    private void setTexts(){
        textName.setText(curentConnection.getName());
        textId.setText(curentConnection.getClientId());
        textSecret.setText(curentConnection.getClientSecret());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SecretIdRow that = (SecretIdRow) o;

        if (curentConnection != null ? !curentConnection.equals(that.curentConnection) : that.curentConnection != null)
            return false;
        if (textBoxName != null ? !textBoxName.equals(that.textBoxName) : that.textBoxName != null) return false;
        if (textBoxSecretId != null ? !textBoxSecretId.equals(that.textBoxSecretId) : that.textBoxSecretId != null)
            return false;
        if (textBoxSecret != null ? !textBoxSecret.equals(that.textBoxSecret) : that.textBoxSecret != null)
            return false;
        if (textName != null ? !textName.equals(that.textName) : that.textName != null) return false;
        if (textId != null ? !textId.equals(that.textId) : that.textId != null) return false;
        return textSecret != null ? textSecret.equals(that.textSecret) : that.textSecret == null;
    }

    @Override
    public int hashCode() {
        int result = curentConnection != null ? curentConnection.hashCode() : 0;
        result = 31 * result + (textBoxName != null ? textBoxName.hashCode() : 0);
        result = 31 * result + (textBoxSecretId != null ? textBoxSecretId.hashCode() : 0);
        result = 31 * result + (textBoxSecret != null ? textBoxSecret.hashCode() : 0);
        result = 31 * result + (textName != null ? textName.hashCode() : 0);
        result = 31 * result + (textId != null ? textId.hashCode() : 0);
        result = 31 * result + (textSecret != null ? textSecret.hashCode() : 0);
        return result;
    }

    public SecretIdConnectionDto getCurentConnection() {
        return curentConnection;
    }
}
