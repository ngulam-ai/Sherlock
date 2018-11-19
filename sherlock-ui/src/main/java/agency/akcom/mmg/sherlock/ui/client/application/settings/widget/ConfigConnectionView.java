
package agency.akcom.mmg.sherlock.ui.client.application.settings.widget;

import agency.akcom.mmg.sherlock.ui.shared.dto.AvazuConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.ArrayList;

public class ConfigConnectionView extends ViewWithUiHandlers<ConfigConnectionUiHandlers> implements ConfigConnectionPresenter.MyView {
    interface Binder extends UiBinder<Widget, ConfigConnectionView> {
    }

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

    @Inject
    ConfigConnectionView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public void displayConfig(ArrayList<DspDto> dspDtos) {
        GWT.log("displayConfig");
        this.dspDtos = dspDtos;
        this.curentDsp =  dspDtos.get(0);
//        TypeConnection typeConnection = dspDto.getTypeConnection();
//        if (typeConnection == TypeConnection.SECRET_ID) {
        GWT.log("displayConfigIf");
        AvazuConnectionDto avazuConnectionDto = (AvazuConnectionDto) curentDsp.getConfigConnectionDtos().get(0);
        nameText.setText(curentDsp.getName());
        displayConfigWithSecret(avazuConnectionDto);
//        }
    }

    public void displayConfigWithSecret(AvazuConnectionDto avazuConnectionDto) {
        textSecretId.setText(avazuConnectionDto.getClientId());
        textSecret.setText(avazuConnectionDto.getClientSecret());
        textGrantType.setText(avazuConnectionDto.getGrantType());
        GWT.log("displayConfigWithSecret");
    }
}
