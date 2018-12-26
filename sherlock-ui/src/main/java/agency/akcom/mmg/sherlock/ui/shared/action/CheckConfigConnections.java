package agency.akcom.mmg.sherlock.ui.shared.action;

import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

//@GenDispatch(isSecure = false, serviceName = UnsecuredActionImpl.DEFAULT_SERVICE_NAME)
public class CheckConfigConnections {

    @In(1)
    Partner partner;
    @In(2)
    ConfigConnectionDto configConnectionDto;

    @Out(1)
    Boolean result;
}
