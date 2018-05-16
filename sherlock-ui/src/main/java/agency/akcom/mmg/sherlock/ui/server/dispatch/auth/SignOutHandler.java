package agency.akcom.mmg.sherlock.ui.server.dispatch.auth;

import javax.inject.Inject;

import agency.akcom.mmg.sherlock.ui.shared.action.SignOutAction;
import agency.akcom.mmg.sherlock.ui.shared.action.SignOutResult;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import agency.akcom.mmg.sherlock.ui.server.auth.CurrentAppUserProvider;

public class SignOutHandler extends AbstractAuthHandler<SignOutAction, SignOutResult> {
	@Inject
	public SignOutHandler(CurrentAppUserProvider currentAppUserProvider) {
		super(SignOutAction.class, currentAppUserProvider);
	}

	@Override
	public SignOutResult execute(SignOutAction action, ExecutionContext context) throws ActionException {
		currentAppUserProvider.set(null);
		return new SignOutResult();
	}

}
