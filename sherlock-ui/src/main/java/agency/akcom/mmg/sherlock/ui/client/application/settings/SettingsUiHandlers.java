package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import com.gwtplatform.mvp.client.UiHandlers;

import java.util.ArrayList;

public interface SettingsUiHandlers extends UiHandlers {
    void onSaveClick(DspDto dspDto);
}
