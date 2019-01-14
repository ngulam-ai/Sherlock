package agency.akcom.mmg.sherlock.ui.client.widget;

import agency.akcom.mmg.sherlock.ui.client.application.settings.SettingsView;
import agency.akcom.mmg.sherlock.ui.shared.dto.EmailPasswordConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Text;

public class EmailPasswordRow extends ExtRow {
    EmailPasswordConnectionDto curentConnection;

    TextBox textBoxName = new TextBox();
    TextBox textBoxEmail = new TextBox();
    TextBox textBoxPassword = new TextBox();
    Text textName = new Text();
    Text textEmail = new Text();
    Text textPassword = new Text();

    public EmailPasswordRow() {
    }

    public void configuration(EmailPasswordConnectionDto curentConnection, Partner partner, SettingsView parent) {
        GWT.log("EmailPasswordRow.configuration");
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
        textBoxEmail.setText(curentConnection.getEmail());
        textBoxPassword.setText(curentConnection.getPassword());

        rowEdit.add(colum("XS_2", textBoxName));
        rowEdit.add(colum("XS_3", textBoxEmail));
        rowEdit.add(colum("XS_5", textBoxPassword));
        rowEdit.add(colum("XS_1", save));
        rowEdit.add(colum("XS_1", delete));
        rowEdit.setHeight("60px");
    }

    @Override
    protected void createRowAlert() {
        FocusPanel focusPanel = new FocusPanel();

        setTexts();

        alert.add(colum("XS_2", textName));
        alert.add(colum("XS_3", textEmail));
        alert.add(colum("XS_6", textPassword));
        alert.add(colum("XS_1",textCheckCoonection));
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
        curentConnection.setEmail(textBoxEmail.getText());
        curentConnection.setPassword(textBoxPassword.getText());
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
        textEmail.setText(curentConnection.getEmail());
        textPassword.setText(curentConnection.getPassword());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailPasswordRow that = (EmailPasswordRow) o;

        if (curentConnection != null ? !curentConnection.equals(that.curentConnection) : that.curentConnection != null)
            return false;
        if (textBoxName != null ? !textBoxName.equals(that.textBoxName) : that.textBoxName != null) return false;
        if (textBoxEmail != null ? !textBoxEmail.equals(that.textBoxEmail) : that.textBoxEmail != null)
            return false;
        if (textBoxPassword != null ? !textBoxPassword.equals(that.textBoxPassword) : that.textBoxPassword != null)
            return false;
        if (textName != null ? !textName.equals(that.textName) : that.textName != null) return false;
        if (textEmail != null ? !textEmail.equals(that.textEmail) : that.textEmail != null) return false;
        return textPassword != null ? textPassword.equals(that.textPassword) : that.textPassword == null;
    }

    @Override
    public int hashCode() {
        int result = curentConnection != null ? curentConnection.hashCode() : 0;
        result = 31 * result + (textBoxName != null ? textBoxName.hashCode() : 0);
        result = 31 * result + (textBoxEmail != null ? textBoxEmail.hashCode() : 0);
        result = 31 * result + (textBoxPassword != null ? textBoxPassword.hashCode() : 0);
        result = 31 * result + (textName != null ? textName.hashCode() : 0);
        result = 31 * result + (textEmail != null ? textEmail.hashCode() : 0);
        result = 31 * result + (textPassword != null ? textPassword.hashCode() : 0);
        return result;
    }

    public EmailPasswordConnectionDto getCurentConnection() {
        return curentConnection;
    }
}
