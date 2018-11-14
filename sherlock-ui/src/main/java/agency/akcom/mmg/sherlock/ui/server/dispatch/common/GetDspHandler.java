package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.configConnection.AvazuConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.GetDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetDspResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.AvazuConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import java.util.ArrayList;


public class GetDspHandler extends MyAbstractActionHandler<GetDspAction, GetDspResult> {
    public GetDspHandler() {
        super(GetDspAction.class);
    }

    @Override
    public GetDspResult execute(GetDspAction action, ExecutionContext context) throws ActionException {
        if (action.getId() == null)
            return new GetDspResult(null);
        DspDao dspDao = new DspDao();
        Dsp dsp = dspDao.get(action.getId());
        DspDto dspDto = new DspDto();
        ArrayList<ConfigConnectionDto> configConnectionDtos = new ArrayList<>();
        for (ConfigConnection configConnection : dsp.getConfigConnections()) {
            switch (dsp.getTypeConnection()) {
                case SECRET_ID:
                    AvazuConnection avazuConnection = (AvazuConnection) configConnection;
                    AvazuConnectionDto avazuConnectionDto = new AvazuConnectionDto(avazuConnection.getClientId(), avazuConnection.getClientSecret(), avazuConnection.getGrantType());
                    configConnectionDtos.add(avazuConnectionDto);
            }
        }
        dspDto.setAttributes(dsp.getId(), dsp.getPartner(), dsp.getName(), dsp.getTypeConnection(), configConnectionDtos);
        return new GetDspResult(dspDto);
    }
}
