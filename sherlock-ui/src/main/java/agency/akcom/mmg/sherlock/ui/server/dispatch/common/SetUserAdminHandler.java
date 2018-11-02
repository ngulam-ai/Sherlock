package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import agency.akcom.mmg.sherlock.ui.domain.AppUser;
import agency.akcom.mmg.sherlock.ui.server.ServerUtils;
import agency.akcom.mmg.sherlock.ui.server.dao.AppUserDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.SetUserAdminAction;
import agency.akcom.mmg.sherlock.ui.shared.action.SetUserAdminResult;

public class SetUserAdminHandler extends MyAbstractActionHandler<SetUserAdminAction, SetUserAdminResult> {
	@Inject
	public SetUserAdminHandler() {
		super(SetUserAdminAction.class);
	}

	@Override
	public SetUserAdminResult execute(SetUserAdminAction action, ExecutionContext context) throws ActionException {

		AppUserDao appUserDao = new AppUserDao();

		AppUser appUser = appUserDao.get(action.getId());

		appUser.setAdmin(action.getValue());

		return new SetUserAdminResult(ServerUtils.createUserDto(null, appUserDao.saveAndReturn(appUser)));

	}

}
