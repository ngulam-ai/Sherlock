package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.configConnection.SecretIdConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.ChangeDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.ChangeDspResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import com.google.gwt.core.client.GWT;
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
        for (ConfigConnectionDto configConnectionDto : dspDto.getConfigConnectionDtos()) {
//            switch (dspDto.getTypeConnection().toString()) {
//                case "SECRET_ID":
            SecretIdConnectionDto secretIdConnectionDto = (SecretIdConnectionDto) configConnectionDto;
            SecretIdConnection secretIdConnection =
                    new SecretIdConnection(
                            secretIdConnectionDto.getName(),
                            secretIdConnectionDto.getClientId(),
                            secretIdConnectionDto.getClientSecret(),
                            secretIdConnectionDto.getGrantType());
            configConnections.add(secretIdConnection);
//            }
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
        GWT.log("ChangeDspResult " + dspDto.getName());
        dspDao.save(dsp);
        return new ChangeDspResult(dspDto);
    }
}
