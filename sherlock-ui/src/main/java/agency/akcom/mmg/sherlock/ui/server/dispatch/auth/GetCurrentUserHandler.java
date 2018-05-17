package agency.akcom.mmg.sherlock.ui.server.dispatch.auth;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import agency.akcom.mmg.sherlock.ui.server.auth.CurrentAppUserProvider;
import agency.akcom.mmg.sherlock.ui.shared.action.GetCurrentUserAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetCurrentUserResult;

public class GetCurrentUserHandler extends AbstractAuthHandler<GetCurrentUserAction, GetCurrentUserResult> {
	@Inject
	public GetCurrentUserHandler(CurrentAppUserProvider currentAppUserProvider) {
		super(GetCurrentUserAction.class, currentAppUserProvider);
	}

	@Override
	public GetCurrentUserResult execute(GetCurrentUserAction action, ExecutionContext context) throws ActionException {
		return new GetCurrentUserResult(getCurrentUserDto());
	}

}
