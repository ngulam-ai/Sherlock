package agency.akcom.mmg.sherlock.ui.shared.action;

import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import agency.akcom.mmg.sherlock.ui.shared.UserDto;

// @GenDispatch(isSecure = false, serviceName = UnsecuredActionImpl.DEFAULT_SERVICE_NAME)
public class SetUserAdmin {

	@In(1)
	Long id;
	@In(2)
	Boolean value;

	@Out(1)
	UserDto userDto;

}
