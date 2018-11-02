package agency.akcom.mmg.sherlock.ui.client.gin;

import com.gwtplatform.dispatch.rpc.client.gin.RpcDispatchAsyncModule;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.shared.proxy.RouteTokenFormatter;

import agency.akcom.mmg.sherlock.ui.client.application.ApplicationModule;
import agency.akcom.mmg.sherlock.ui.client.login.LoginModule;
import agency.akcom.mmg.sherlock.ui.client.place.NameTokens;

/**
 * See more on setting up the PlaceManager on <a href="// See more on:
 * https://github.com/ArcBees/GWTP/wiki/PlaceManager">DefaultModule's >
 * DefaultPlaceManager</a>
 */
public class ClientModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		install(new DefaultModule.Builder().tokenFormatter(RouteTokenFormatter.class).build());
		install(new RpcDispatchAsyncModule.Builder().build());
		
		install(new ApplicationModule());
		install(new LoginModule());

		// DefaultPlaceManager Places
		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.HOME);
		bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.HOME);
		bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.LOGIN);
	}
}