package agency.akcom.mmg.sherlock.ui.client.application;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

import agency.akcom.mmg.sherlock.ui.client.application.home.HomeModule;

public class ApplicationModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		install(new HomeModule());

		bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
				ApplicationPresenter.MyProxy.class);
	}
}