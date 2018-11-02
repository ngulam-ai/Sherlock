package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import agency.akcom.mmg.sherlock.ui.server.dao.AppUserDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.DeleteUserAction;
import agency.akcom.mmg.sherlock.ui.shared.action.DeleteUserResult;

public class DeleteUserHandler extends MyAbstractActionHandler<DeleteUserAction, DeleteUserResult> {
	@Inject
	public DeleteUserHandler() {
		super(DeleteUserAction.class);
	}

	@Override
	public DeleteUserResult execute(DeleteUserAction action, ExecutionContext context) throws ActionException {

		new AppUserDao().delete(action.getId());

		return new DeleteUserResult();
	}

}
