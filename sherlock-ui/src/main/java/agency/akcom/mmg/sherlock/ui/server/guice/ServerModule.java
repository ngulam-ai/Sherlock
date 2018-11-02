package agency.akcom.mmg.sherlock.ui.server.guice;


import com.google.inject.AbstractModule;

import agency.akcom.mmg.sherlock.ui.server.auth.AuthModule;
import agency.akcom.mmg.sherlock.ui.server.dao.objectify.OfyService;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyHandlerModule;

public class ServerModule extends AbstractModule {
	@Override
	protected void configure() {
		
		requestStaticInjection(OfyService.class);
		
		install(new AuthModule());
		install(new MyHandlerModule());

	}
}
