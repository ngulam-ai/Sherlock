package agency.akcom.mmg.sherlock.ui.client.application.log;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class LogModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(LogPresenter.class, LogPresenter.MyView.class, LogView.class, LogPresenter.MyProxy.class);
	}
}