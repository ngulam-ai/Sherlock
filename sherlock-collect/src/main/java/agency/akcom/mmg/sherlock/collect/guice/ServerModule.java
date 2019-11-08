package agency.akcom.mmg.sherlock.collect.guice;

import com.google.inject.AbstractModule;

import agency.akcom.mmg.sherlock.collect.dao.objectify.OfyService;

public class ServerModule extends AbstractModule {
	@Override
	protected void configure() {

		requestStaticInjection(OfyService.class);

	}
}
