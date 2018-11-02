package agency.akcom.mmg.sherlock.ui.server.auth;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;

public class AuthModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(CurrentAppUserProvider.class).in(Singleton.class);
	}
}
