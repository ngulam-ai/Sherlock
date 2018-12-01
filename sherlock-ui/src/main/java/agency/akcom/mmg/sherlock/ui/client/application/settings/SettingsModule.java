package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.client.application.settings.widget.secretId.ConfigConnectionModule;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SettingsModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(SettingsPresenter.class, SettingsPresenter.MyView.class, SettingsView.class, SettingsPresenter.MyProxy.class);
		bind(SettingsUiHandlers.class).to(SettingsPresenter.class);
	}
}