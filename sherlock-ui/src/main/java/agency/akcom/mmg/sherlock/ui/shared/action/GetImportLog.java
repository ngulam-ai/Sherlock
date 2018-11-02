package agency.akcom.mmg.sherlock.ui.shared.action;

import java.util.List;

import com.gwtplatform.dispatch.annotation.Out;

import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;

//@GenDispatch(isSecure = false, serviceName = UnsecuredActionImpl.DEFAULT_SERVICE_NAME)
public class GetImportLog {
	
	@Out(1)
	List<ImportLog> logs;

}
