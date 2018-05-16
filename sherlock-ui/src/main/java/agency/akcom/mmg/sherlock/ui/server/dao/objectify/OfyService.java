package agency.akcom.mmg.sherlock.ui.server.dao.objectify;

import javax.inject.Inject;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class OfyService {
	@Inject
	public static void setObjectifyFactory(OfyFactory factory) {
		ObjectifyService.setFactory(factory);
	}

	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}

	public static OfyFactory factory() {
		return (OfyFactory) ObjectifyService.factory();
	}
}
