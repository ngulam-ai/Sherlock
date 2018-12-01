package agency.akcom.mmg.sherlock.ui.shared.action;

import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

import java.util.ArrayList;

//@GenDispatch(isSecure = false, serviceName = UnsecuredActionImpl.DEFAULT_SERVICE_NAME)
public class GetAllDsp {

    @Out(1)
    ArrayList<DspDto> dspDtos;
}
