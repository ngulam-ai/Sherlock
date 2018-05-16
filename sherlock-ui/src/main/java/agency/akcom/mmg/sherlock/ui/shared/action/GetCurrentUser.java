package agency.akcom.mmg.sherlock.ui.shared.action;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

import agency.akcom.mmg.sherlock.ui.shared.UserDto;

//@GenDispatch(isSecure = false, serviceName = UnsecuredActionImpl.DEFAULT_SERVICE_NAME)
public class GetCurrentUser {

	@Out(1)
	UserDto currentUserDto;
}
