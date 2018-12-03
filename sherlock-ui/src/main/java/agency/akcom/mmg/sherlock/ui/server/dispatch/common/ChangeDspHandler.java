package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.SecretIdConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.TokenConnection;
import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.ChangeDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.ChangeDspResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.TokenConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import java.util.ArrayList;

public class ChangeDspHandler extends MyAbstractActionHandler<ChangeDspAction, ChangeDspResult> {
    public ChangeDspHandler() {
        super(ChangeDspAction.class);
    }

    @Override
    public ChangeDspResult execute(ChangeDspAction action, ExecutionContext context) throws ActionException {
        if (action.getDspDto() == null)
            return new ChangeDspResult(null);

        DspDao dspDao = new DspDao();
        DspDto dspDto = action.getDspDto();
        ArrayList<ConfigConnection> configConnections = new ArrayList<>();
        if (dspDto.getConfigConnectionDtos().size() > 0) {
            for (ConfigConnectionDto configConnectionDto : dspDto.getConfigConnectionDtos()) {
                switch (dspDto.getTypeConnection()) {
                    case SECRET_ID: {
                        SecretIdConnectionDto secretIdConnectionDto = (SecretIdConnectionDto) configConnectionDto;
                        SecretIdConnection secretIdConnection =
                                new SecretIdConnection(
                                        secretIdConnectionDto.getName(),
                                        secretIdConnectionDto.getClientId(),
                                        secretIdConnectionDto.getClientSecret(),
                                        secretIdConnectionDto.getGrantType());
                        configConnections.add(secretIdConnection);
                        break;
                    }
                    case TOKEN: {
                        TokenConnectionDto tokenConnectionDto = (TokenConnectionDto) configConnectionDto;
                        TokenConnection tokenConnection =
                                new TokenConnection(
                                        tokenConnectionDto.getName(),
                                        tokenConnectionDto.getToken());
                        configConnections.add(tokenConnection);
                        break;
                    }
                }
            }
        }
        Dsp dsp;
        try {
            dsp = dspDao.get(dspDto.getId());
            dspDao.delete(dsp.getId());
        } catch (Exception e) {                           //com.gwtplatform.dispatch.rpc.shared.ServiceException
            dsp = new Dsp(
                    dspDto.getPartner(),
                    dspDto.getName(),
                    dspDto.getTypeConnection(),
                    configConnections);
        }
        dsp.setConfigConnections(configConnections);
        dspDao.save(dsp);
        dspDto = copyDspToDspDto(dsp);
        return new ChangeDspResult(dspDto);
    }

    private DspDto copyDspToDspDto(Dsp dsp) {
        DspDto result = new DspDto();
        result.setAttributes(dsp.getId(), dsp.getPartner(), dsp.getName(), dsp.getTypeConnection(), copyConnectionToDto(dsp.getConfigConnections(), dsp.getTypeConnection()));
        return result;
    }

    private ArrayList<ConfigConnectionDto> copyConnectionToDto(ArrayList<ConfigConnection> configConnections, TypeConnection typeConnection) {
        ArrayList<ConfigConnectionDto> result = new ArrayList<ConfigConnectionDto>();
        if (configConnections != null && configConnections.size() > 0) {
            for (ConfigConnection curentConnection : configConnections) {
                switch (typeConnection) {
                    case SECRET_ID: {
                        SecretIdConnection curentSecretIdConnection = (SecretIdConnection) curentConnection;
                        SecretIdConnectionDto secretIdConnectionDto = new SecretIdConnectionDto();

                        secretIdConnectionDto.setId(curentSecretIdConnection.getId());
                        secretIdConnectionDto.setName(curentSecretIdConnection.getName());
                        secretIdConnectionDto.setClientId(curentSecretIdConnection.getClientId());
                        secretIdConnectionDto.setClientSecret(curentSecretIdConnection.getClientSecret());
                        secretIdConnectionDto.setGrantType(curentSecretIdConnection.getGrantType());
                        result.add(secretIdConnectionDto);
                        break;
                    }
                    case TOKEN: {
                        TokenConnection curentTokenConnection = (TokenConnection) curentConnection;
                        TokenConnectionDto tokenConnectionDto = new TokenConnectionDto();

                        tokenConnectionDto.setId(curentTokenConnection.getId());
                        tokenConnectionDto.setName(curentTokenConnection.getName());
                        tokenConnectionDto.setToken(curentTokenConnection.getToken());

                        result.add(tokenConnectionDto);
                        break;
                    }
                }
            }
        }
        return result;
    }
}

