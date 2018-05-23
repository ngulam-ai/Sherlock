package agency.akcom.mmg.sherlock.ui.shared.action;

import com.gwtplatform.dispatch.annotation.In;

//@GenDispatch(isSecure = false, serviceName = UnsecuredActionImpl.DEFAULT_SERVICE_NAME)
public class DeleteUser {

	@In(1)
	Long Id;
}
