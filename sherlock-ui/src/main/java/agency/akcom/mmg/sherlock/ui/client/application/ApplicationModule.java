package agency.akcom.mmg.sherlock.ui.client.application;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

import agency.akcom.mmg.sherlock.ui.client.application.home.HomeModule;
import agency.akcom.mmg.sherlock.ui.client.application.log.LogModule;
import agency.akcom.mmg.sherlock.ui.client.application.users.UsersModule;

public class ApplicationModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		install(new HomeModule());
		install(new UsersModule());
		install(new LogModule());

		bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
				ApplicationPresenter.MyProxy.class);
	}
}