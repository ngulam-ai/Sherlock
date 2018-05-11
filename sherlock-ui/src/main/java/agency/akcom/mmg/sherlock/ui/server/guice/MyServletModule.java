package agency.akcom.mmg.sherlock.ui.server.guice;

import javax.inject.Singleton;

import com.google.inject.servlet.ServletModule;

import agency.akcom.mmg.sherlock.ui.server.CronServlet;
import agency.akcom.mmg.sherlock.ui.server.GreetingServiceImpl;

public class MyServletModule extends ServletModule {

	@Override
	public void configureServlets() {

		// Objectify filter
		// bind(ObjectifyFilter.class).in(Singleton.class);
		// filter("/*").through(ObjectifyFilter.class);

		// my servlets
		bind(GreetingServiceImpl.class).in(Singleton.class);
		serve("/sherlockui/greet").with(GreetingServiceImpl.class);

		bind(CronServlet.class).in(Singleton.class);
		serve("/cron").with(CronServlet.class);

	}

}