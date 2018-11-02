package agency.akcom.mmg.sherlock.collect.guice;

import javax.inject.Singleton;

import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyFilter;

import agency.akcom.mmg.sherlock.collect.CollectServlet;

public class MyServletModule extends ServletModule {

	@Override
	public void configureServlets() {
		

		// collect servlet
		bind(CollectServlet.class).in(Singleton.class);
		serve("/collect").with(CollectServlet.class);


		// Objectify filter
		bind(ObjectifyFilter.class).in(Singleton.class);
		filter("/*").through(ObjectifyFilter.class);


	}

}