package agency.akcom.mmg.sherlock.ui.shared.action;

import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

@GenDispatch(isSecure = false, serviceName = UnsecuredActionImpl.DEFAULT_SERVICE_NAME)
public class AddDsp {
    @In(1)
    String name;
    @In(2)
    String partnerName;

    @Out(1)
    DspDto dspDto;
}
