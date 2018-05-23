package agency.akcom.mmg.sherlock.ui.shared.action;

import java.util.List;

import com.gwtplatform.dispatch.annotation.Out;

import agency.akcom.mmg.sherlock.ui.shared.dto.UserDto;

//@GenDispatch(isSecure = false, serviceName = UnsecuredActionImpl.DEFAULT_SERVICE_NAME)
public class GetUsers {

	@Out(1)
	List<UserDto> users;
}
