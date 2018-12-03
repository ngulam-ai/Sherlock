package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.configConnection.SecretIdConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.TokenConnection;
import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.GetAllDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetAllDspResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.TokenConnectionDto;
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
        for (Dsp dsp : dsps) {
            DspDto dspDto = new DspDto();
            ArrayList<ConfigConnectionDto> configConnectionDtos = new ArrayList<>();
            if (dsp.getConfigConnections() != null && dsp.getConfigConnections().size() > 0) {
                for (ConfigConnection configConnection : dsp.getConfigConnections()) {
                    switch (dsp.getTypeConnection()) {
                        case SECRET_ID:
                            SecretIdConnection secretIdConnection = (SecretIdConnection) configConnection;
                            SecretIdConnectionDto secretIdConnectionDto = new SecretIdConnectionDto(secretIdConnection.getName(), secretIdConnection.getClientId(), secretIdConnection.getClientSecret(), secretIdConnection.getGrantType());
                            configConnectionDtos.add(secretIdConnectionDto);
                            break;
                        case TOKEN:
                            TokenConnection tokenConnection = (TokenConnection) configConnection;
                            TokenConnectionDto tokenConnectionDto = new TokenConnectionDto(tokenConnection.getName(), tokenConnection.getToken());
                            configConnectionDtos.add(tokenConnectionDto);
                            break;
                    }
                }
            }
            dspDto.setAttributes(dsp.getId(), dsp.getPartner(), dsp.getName(), dsp.getTypeConnection(), configConnectionDtos);
            dspDtos.add(dspDto);
        }
        return new GetAllDspResult(dspDtos);
    }
}
