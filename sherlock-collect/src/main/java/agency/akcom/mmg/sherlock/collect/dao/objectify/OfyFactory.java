package agency.akcom.mmg.sherlock.collect.dao.objectify;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.inject.Injector;
import com.googlecode.objectify.ObjectifyFactory;

import agency.akcom.mmg.sherlock.collect.domain.AudUser;

/**
 * Our version of ObjectifyFactory which integrates with Guice. You could and
 * convenience methods here too.
 *
 * @author Jeff Schnitzer
 */
@Singleton
public class OfyFactory extends ObjectifyFactory {
	private Injector injector;

	/** Register our entity types */
	@Inject
	public OfyFactory(Injector injector) {
		this.injector = injector;

		long time = System.currentTimeMillis();

		this.register(AudUser.class);

		long millis = System.currentTimeMillis() - time;
		System.out.println("Registration took " + millis + " millis");
	}

	/** Use guice to make instances instead! */
	@Override
	public <T> T construct(Class<T> type) {
		return injector.getInstance(type);
	}
}