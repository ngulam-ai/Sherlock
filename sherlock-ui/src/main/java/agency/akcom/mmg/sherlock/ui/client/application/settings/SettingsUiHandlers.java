package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.client.widget.ExtRow;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface SettingsUiHandlers extends UiHandlers {
    void onSaveClick(DspDto dspDto);
    void CheckConfigConnections(ExtRow curentRow);
}
