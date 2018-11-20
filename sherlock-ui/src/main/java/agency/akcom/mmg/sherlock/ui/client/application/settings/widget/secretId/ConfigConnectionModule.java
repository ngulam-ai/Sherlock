
package agency.akcom.mmg.sherlock.ui.client.application.settings.widget.secretId;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ConfigConnectionModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindSingletonPresenterWidget(ConfigConnectionPresenter.class, ConfigConnectionPresenter.MyView.class, ConfigConnectionView.class);
    }
}
