package agency.akcom.mmg.sherlock.ui.client.widget;

import agency.akcom.mmg.sherlock.ui.client.application.settings.SettingsView;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Text;


public class ExtRow extends Row {

    ConfigConnectionDto curentConnection;
    Partner partner;
    boolean resultCheckConncetion = false;
    Text textCheckCoonection = new Text();
    Alert alert = new Alert();
    Row rowAlert = new Row();
    Row rowEdit = new Row();
    Collapse collapse = new Collapse();
    Button delete;
    Button save;
    SettingsView parent;

    public ExtRow() {
        GWT.log("ExtRow");
    }

    public void configuration(Partner partner, SettingsView parent) {
        this.partner = partner;
        this.parent = parent;
    }

    protected void init() {
        GWT.log("ExtRow.init");
        checkAlert();
        createRowAlert();
        add(rowAlert);
        createRowEdit();
        collapse.add(rowEdit);
        collapse.setToggle(false);
        add(collapse);
        if (!resultCheckConncetion) {
            checkConnection();
        }
    }

    protected void createRowEdit() {
    }

    protected void createRowAlert() {
    }

    protected void checkAlert() {
        if (resultCheckConncetion) {
            alert.setType(AlertType.SUCCESS);
            textCheckCoonection.setText("Valid");
        } else {
            alert.setType(AlertType.DANGER);
            textCheckCoonection.setText("Invalid");
        }
    }

    protected Column colum(String size, Widget child) {
        Column result = new Column(size);
        result.add(child);
        return result;
    }

    protected void createDellButton() {
        delete = new Button();
        delete.setIcon(IconType.REMOVE);
        delete.setColor("RED");
    }

    protected void createSaveButton() {
        save = new Button();
        save.setIcon(IconType.SAVE);
        save.setColor("GREEN");
    }

    protected void checkConnection() {
        parent.checkConnection(this);
    }

    public Partner getPartner() {
        return partner;
    }

    public boolean isResultCheckConncetion() {
        return resultCheckConncetion;
    }

    public void setResultCheckConncetion(boolean resultCheckConncetion) {
        this.resultCheckConncetion = resultCheckConncetion;
        checkAlert();
    }

    public Button getDelete() {
        return delete;
    }

    public Button getSave() {
        return save;
    }

    public ConfigConnectionDto getCurentConnection() {
        return curentConnection;
    }

    protected String getSecurityStr(String input) {
        String result = "";
        int length = input.length();
        if (length > 4) {
            for (int i = 0; i < length - 4; i++) {
                result = result + "*";
            }
            result = result + input.substring(length-4);
        } else {
            result = input;
        }
        return result;
    }

    protected boolean checkSecurityStr(String input1, String input2){
        return input1.equals(input2);
    }
}
