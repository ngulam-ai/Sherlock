package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.configConnection.AvazuConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.GetAllDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetAllDspResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.AvazuConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import java.util.ArrayList;

public class GetAllDspHandler extends MyAbstractActionHandler<GetAllDspAction, GetAllDspResult> {
    public GetAllDspHandler() {
        super(GetAllDspAction.class);
    }

    @Override
    public GetAllDspResult execute(GetAllDspAction action, ExecutionContext context) throws ActionException {
        DspDao dspDao = new DspDao();
        ArrayList<Dsp> dsps = new ArrayList<Dsp>(dspDao.listAll());
        ArrayList<DspDto> dspDtos = new ArrayList<>();
        DspDto dspDto = new DspDto();
        for (Dsp dsp : dsps) {
            ArrayList<ConfigConnectionDto> configConnectionDtos = new ArrayList<>();
            for (ConfigConnection configConnection : dsp.getConfigConnections()) {
                switch (configConnection.getTypeConnection()) {
                    case SECRET_ID:
                        AvazuConnection avazuConnection = (AvazuConnection) configConnection;
                        AvazuConnectionDto avazuConnectionDto = new AvazuConnectionDto(avazuConnection.getTypeConnection(), avazuConnection.getClientId(), avazuConnection.getClientSecret(), avazuConnection.getGrantType());
                        configConnectionDtos.add(avazuConnectionDto);
                }
            }
            dspDto.setAttributes(dsp.getId(),dsp.getPartner(), dsp.getName(), configConnectionDtos);
            dspDtos.add(dspDto);
        }
        return new GetAllDspResult(dspDtos);
    }
}
