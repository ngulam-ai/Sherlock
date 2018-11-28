
package agency.akcom.mmg.sherlock.ui.client.application.settings.widget.secretId;

import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Text;

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

    SecretIdConnectionDto curentSecretIdConnectionDto;

    @Inject
    ConfigConnectionView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public void displayConfig(SecretIdConnectionDto dspDtos) {
        GWT.log("displayConfig");
        nameText.setText(curentSecretIdConnectionDto.getName());
        textSecretId.setText(curentSecretIdConnectionDto.getClientId());
        textSecret.setText(curentSecretIdConnectionDto.getClientSecret());
        textGrantType.setText(curentSecretIdConnectionDto.getGrantType());
    }

}
