package agency.akcom.mmg.sherlock.ui.server.dispatch.validator;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.rpc.shared.Action;
import com.gwtplatform.dispatch.rpc.shared.Result;
import com.gwtplatform.dispatch.shared.ActionException;

import agency.akcom.mmg.sherlock.ui.domain.AppUser;
import agency.akcom.mmg.sherlock.ui.server.auth.CurrentAppUserProvider;

public class LoggedInActionValidator implements ActionValidator {

	CurrentAppUserProvider currentAppUserProvider;

	@Inject
	public LoggedInActionValidator(final CurrentAppUserProvider currentAppUserProvider) {
		this.currentAppUserProvider = currentAppUserProvider;
	}

	@Override
	public boolean isValid(Action<? extends Result> action) throws ActionException {
		if (currentAppUserProvider == null)
			throw new ActionException("currentAppUserProvider is null");

		AppUser appUser = currentAppUserProvider.get();

		return appUser != null;
	}

}
