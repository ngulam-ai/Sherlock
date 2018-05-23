package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import agency.akcom.mmg.sherlock.ui.domain.AppUser;
import agency.akcom.mmg.sherlock.ui.server.ServerUtils;
import agency.akcom.mmg.sherlock.ui.server.dao.AppUserDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.GetUsersAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetUsersResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.UserDto;

public class GetUsersHandler extends MyAbstractActionHandler<GetUsersAction, GetUsersResult> {
	@Inject
	public GetUsersHandler() {
		super(GetUsersAction.class);
	}

	@Override
	public GetUsersResult execute(GetUsersAction action, ExecutionContext context) throws ActionException {
		List<AppUser> appUsers = new AppUserDao().listAll();

		List<UserDto> userDtos = new ArrayList<UserDto>(appUsers.size());

		for (AppUser appUser : appUsers) {
			userDtos.add(ServerUtils.createUserDto(null, appUser));
		}

		return new GetUsersResult(userDtos);
	}

}
