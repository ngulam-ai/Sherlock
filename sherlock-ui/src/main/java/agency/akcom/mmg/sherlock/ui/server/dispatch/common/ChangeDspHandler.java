package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.configConnection.AvazuConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.ChangeDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.ChangeDspResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.AvazuConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
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

        for (ConfigConnectionDto configConnectionDto : dspDto.getConfigConnectionDtos()) {
            switch (dspDto.getTypeConnection()) {
                case SECRET_ID:
                    AvazuConnectionDto avazuConnectionDto = (AvazuConnectionDto) configConnectionDto;
                    AvazuConnection avazuConnection = new AvazuConnection(avazuConnectionDto.getClientId(), avazuConnectionDto.getClientSecret(), avazuConnectionDto.getGrantType());
                    configConnections.add(avazuConnection);
            }
        }
        Dsp dsp = new Dsp(dspDto.getId(), dspDto.getPartner(), dspDto.getName(), dspDto.getTypeConnection(), configConnections);
        dspDao.save(dsp);
        return new ChangeDspResult(dspDto);
    }
}